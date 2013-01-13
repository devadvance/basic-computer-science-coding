/*
Copyright (c) 2012, Matt Joseph, Arielle Garcia
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


University of Pittsburgh
CS1555
Term Project
*/

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class facesApp
{
	// These variables get used throughout the entire program, so declare them here
	private Connection connection; // Holds the JDBC connection
	private PreparedStatement statement; // Used to create an instance of the connection
	private ResultSet resultSet; // Holds the result of a query
	private String query; // Holds the string query that we need
	private String db_username, db_password;
	private Scanner input; // Scanner for the whole class
	private int loggedInUser; // Maintins the current user's ID
	private java.sql.Date last_login;
	private SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy"); // Formats the date
	String dateString; // Holds the formatted date for a query
	
	/**
	* Constructor to allow facesApp session instantiation.
	* 
	* facesApp constructor is called by the main method to run 
	* the actual facesApp logic.
	* 
	*/
	public facesApp() {
		int result;
		input = new Scanner(System.in);
		int command = 0;
		
		db_username = "mgj7"; // This is your username in oracle
		db_password = "3542684"; // This is your password in oracle
		
		try {
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			String url = "jdbc:oracle:thin:@db10.cs.pitt.edu:1521:dbclass"; 
			connection = DriverManager.getConnection(url, db_username, db_password); 
			//create a connection to DB on db10.cs.pitt.edu
		}
		catch(Exception Ex) { // What to do with any exceptions
			System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
			Ex.printStackTrace();
		}
		
		System.out.println("\nWelcome to Faces at Pitt!");
		while(true) {
			System.out.println("\nWhat would you like to do?\n+++++++++++++++++++++++++");
			System.out.println("1 - Login\n2 - Register\n3 - Quit\n+++++++++++++++++++++++++");
			
			System.out.print("Enter the number of what you would like to do: ");
			do {
				if (input.hasNextInt()) {
					command = input.nextInt();
					break;
				}
				else {
					System.out.println("You did not enter a number, try again.");
					input.nextLine();
				}
			}while(true);

			if (command == 1) {
				input.nextLine();
				result = this.login();
				if (result == 1) {
					mainScreen();
				}
				else {
					System.out.println("You entered an invalid email or password!");
				}

			}
			else if (command == 2) {
				input.nextLine();
				result = this.register();
				if (result == 1) {
					System.out.println("You are now registered!");
				}

			}
			else if (command == 3) {
				System.out.println("Goodbye!");
				try {
					connection.close();
				}
				catch (Exception Ex) {
					System.out.println("Error closing connection.  Machine Error: " + Ex.toString());
				}
				return;
			}
			else {
				System.out.println("You entered an invalid option. Try again.");
			}
			input.nextLine();
		}
	}
	
	/**
	* Main selection screen of facesApp.
	* 
	* mainScreen is where the user can select what they want to do 
	* after they have logged in. This is also where they return to 
	* after running different tasks or other options. Does not return 
	* anything.
	*/
	private void mainScreen() {
		int menuChoice;
		int choice_return;		
		while(true) {
			System.out.println("\nWhat would you like to do?\n+++++++++++++++++++++++++");
			System.out.println("1 - Send a message\n2 - Add a friend\n3 - View my Messages\n" + 
				"4 - Display Friends\n5 - Find User\n6 - Confirm Friend Requests\n" + 
				"7 - Three Degrees of Separation\n8 - Join a Group\n9 - Create a group\n10 - My Statistics\n11 - Drop Account\n12 - Logout\n+++++++++++++++++++++++++");
			System.out.print("Enter the numbered value: ");
			do {
					if (input.hasNextInt()) {
						menuChoice = input.nextInt();
						break;
					}
					else {
						System.out.println("You did not enter a number, try again.");
						input.nextLine();
					}
				}while(true);

			switch (menuChoice) {
				case 1: // Send a message
					choice_return = sendMessage();
					break;
				case 2: // Add a friend
					choice_return = addFriend();
					break;
				case 3: // Display messages (All or New)
					choice_return = displayMessages();
					break;
				case 4: // Display Friends
					choice_return = displayFriends();
					break;
				case 5: // Find User
					choice_return = findUser();
					break;
				case 6: // Confirm Friend Request
					choice_return = confirmFriends();
					break;
				case 7: // Three Degrees of Separation
					choice_return = degreesSeparation();
					break;
				case 8: // Join a group
					choice_return = joinGroup();
					break;
				case 9: // Create a group
					choice_return = createGroup();
					break;
				case 10: // My Statistics
					choice_return = myStats();
					break;
				case 11: // Drop Account
					choice_return = dropAccount();
					if (choice_return == 1) {
						System.out.println("\nYOU DELETED YOUR ACCOUNT!!!\nPress ENTER to continue.\n");
						return;
					}
					System.out.println("\nYou didn\'t delete your account...\n");
					break;
				case 12: // Logout
					choice_return = logoutProcedure();
					return;
				default:
					break;
			}
		}

	}

	/**
	* Allows the user to create a group.
	* 
	* joinGroup is used to create a new group. Makes sure that 
	* the group does not already exist.
	* 
	* @return int containing the exit status of the method
	*/
	private int createGroup() {
		boolean continueLoop;
		String newName;
		int createResult;

		// Clear scanner buffer
		input.nextLine();

		System.out.print("Enter a name for the new group: ");
		newName = input.nextLine();

		System.out.println("Enter a description for the group. Leave a blank line to end:");

		StringBuilder buildDescription = new StringBuilder();
		String tempString;

		continueLoop = true;
		//tempString = input.nextLine();
		do {
			tempString = input.nextLine();

			if (tempString.equals("")) {
				continueLoop = false;
			}
			else {
				buildDescription.append(tempString);
			}
		} while(continueLoop);

		try {
			query = "INSERT INTO groups VALUES(seq_gID.nextval, ?, ?)";
			statement = connection.prepareStatement(query);
			statement.setString(1, newName);
			statement.setString(2, buildDescription.toString());

			createResult = statement.executeUpdate();

		}
		catch (Exception Ex) {
			if ((Ex.toString()).indexOf("unique") != -1) {
				System.out.println("This group already exists!");
			}
			else {
				System.out.println("Error registering.  Machine Error: " + Ex.toString());
			}

			System.out.println("Returning to main menu.\n\n");

			return 0;
		}

		System.out.println("New group created successfully!\nReturning to main menu.\n");
		return 1;
	}

	/**
	* Verify the input date is valid.
	* 
	* checkDate verifies that a date input by the user is valid and 
	* in the correct format.
	* 
	* @param inputDate - String containing the date input by the user
	* @return int returns 1 if the date is valid, otherwise return 0
	*/
	private int checkDate(String inputDate) {
		// Declare and (optionally) initialize variables
		String checkMonth;
		int checkDay = 0;
		int checkYear = 0;

		// If the input contains no hyphen to split on
		if (inputDate.indexOf('-') == -1) {
			System.out.println("Invalid date format!");
			return 0;
		}

		// Split the input date
		String[] dateArray = inputDate.split("-");

		// If the dateArray does not contain 3 values
		if (dateArray.length != 3) {
			System.out.println("Invalid number of values in date!");
			return 0;
		}
		// Set up the checkMonth variable now that it's safe to do so
		checkMonth = dateArray[1];

		// Do this in a try-catch to make sure they are integer values
		try {
			checkDay = Integer.parseInt(dateArray[0]);
			checkYear = Integer.parseInt(dateArray[2]);
		}
		catch (Exception Ex) {
			// If they're not both integers, return 0
			System.out.println("You didn\'t enter numbers for the date or year!");
			return 0;
		}

		// Check if the month is one of the correct values.
		// If so, check to make sure the date/year range is correct for that month
		if (checkMonth.equals("JAN")) {
			if ((checkDay >= 1) && (checkDay <= 31) && (checkYear >= 0)) {
				return 1;
			}
		}
		else if (checkMonth.equals("FEB")) {
			if ((checkDay >= 1) && (checkDay <= 29) && (checkYear >= 0)) {
				// If it is a leap year and the 29th, or not the 29th, then it's okay
				if (((checkDay == 29) && ((checkYear % 4) == 0)) || (checkDay != 29)) {
					return 1;
				}	
			}
		}
		else if (checkMonth.equals("MAR")) {
			if ((checkDay >= 1) && (checkDay <= 31) && (checkYear >= 0)) {
				return 1;
			}
		}
		else if (checkMonth.equals("APR")) {
			if ((checkDay >= 1) && (checkDay <= 30) && (checkYear >= 0)) {
				return 1;
			}
		}
		else if (checkMonth.equals("MAY")) {
			if ((checkDay >= 1) && (checkDay <= 31) && (checkYear >= 0)) {
				return 1;
			}
		}
		else if (checkMonth.equals("JUN")) {
			if ((checkDay >= 1) && (checkDay <= 30) && (checkYear >= 0)) {
				return 1;
			}
		}
		else if (checkMonth.equals("JUL")) {
			if ((checkDay >= 1) && (checkDay <= 31) && (checkYear >= 0)) {
				return 1;
			}
		}
		else if (checkMonth.equals("AUG")) {
			if ((checkDay >= 1) && (checkDay <= 31) && (checkYear >= 0)) {
				return 1;
			}
		}
		else if (checkMonth.equals("SEP")) {
			if ((checkDay >= 1) && (checkDay <= 30) && (checkYear >= 0)) {
				return 1;
			}
		}
		else if (checkMonth.equals("OCT")) {
			if ((checkDay >= 1) && (checkDay <= 31) && (checkYear >= 0)) {
				return 1;
			}
		}
		else if (checkMonth.equals("NOV")) {
			if ((checkDay >= 1) && (checkDay <= 30) && (checkYear >= 0)) {
				return 1;
			}
		}
		else if (checkMonth.equals("DEC")) {
			if ((checkDay >= 1) && (checkDay <= 31) && (checkYear >= 0)) {
				return 1;
			}
		}
		else {
			// Failed the month checks
			System.out.println("Invalid month entered!");
			return 0;
		}

		// Return 0 if it failed the day/year checks
		System.out.println("Invalid day or year!");
		return 0;
	}

	/**
	* Confirm one or more pending friend requests.
	* 
	* confirmFriends allows the current user to confirm one, multiple, 
	* or all of their pending friend request (that is, requests they have 
	* received, NOT sent). Any unconfirmed requests get deleted at the 
	* end of this method.
	* 
	* @return int containing the exit status of the method
	*/
	private int confirmFriends() {
		int pendingChoice = 0;
		int numRequests = 0;
		dateString = df.format(new java.util.Date(System.currentTimeMillis()));
		ArrayList<Integer> fromIDList = new ArrayList<Integer>();
		ArrayList<String> messageList = new ArrayList<String>();
		ArrayList<Integer> existingFriends;
		int confirmResult = 0;

		// Grab your existing friends as a comparison.
		existingFriends = getMyFriends(false);

		try {
			query = "SELECT fromID, message FROM pendingfriends WHERE toID = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, loggedInUser);
			
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				fromIDList.add(resultSet.getInt(1));
				messageList.add(resultSet.getString(2));
			}

			if (fromIDList.size() == 0) {
				System.out.println("You have no pending friend requests!\n");
				return 1;
			}

			System.out.println("\nHere are your pending friend requests: ");
			System.out.println("Request Number - from UserID - Friend Request Message\n+++++++++++++++++++++++++");

			for (numRequests = 1; numRequests <= fromIDList.size(); numRequests++) {
				System.out.println(numRequests + " - UserID:" + fromIDList.get(numRequests - 1) + "  -  " + messageList.get(numRequests - 1));
			}
			System.out.println("");

			System.out.print("+++++++++++++++++++++++++\nEnter 1 to confirm specific request or 2 to confirm all, or 0 to skip and remove all: ");
			do {
				if (input.hasNextInt()) {
					pendingChoice = input.nextInt();
					break;
				}
				else {
					System.out.println("You did not enter a number, try again.");
					input.nextLine();
				}
			}while(true);

			if (pendingChoice == 1) { // Confirm specific requests
				while ((fromIDList.size() > 0) && (pendingChoice != 0)) {
					System.out.print("Enter the request number you would like to confirm.\nOr enter 0 to quit: ");
					do {
						if (input.hasNextInt()) {
							pendingChoice = input.nextInt();
							break;
						}
						else {
							System.out.println("You did not enter a number, try again.");
							input.nextLine();
						}
					}while(true);

					System.out.println("You entered " + pendingChoice);

					if (pendingChoice == 0) {
						continue;
					}
					else if ((pendingChoice > fromIDList.size()) || (pendingChoice < 0)) {
						System.out.println("You entered an invalid choice!");
						continue;
					}

					if (existingFriends.contains(fromIDList.get(pendingChoice - 1))) {
						System.out.println("This person is already your friend!\nIgnore this request for now, it will be removed.");
						continue;
					}
					
					// Move the friend relation into the friends table
					query = "INSERT INTO friends VALUES(?, ?, ?, ?)";
					statement = connection.prepareStatement(query);
					statement.setInt(1, fromIDList.get(pendingChoice - 1));
					statement.setInt(2, loggedInUser);
					statement.setString(3, dateString);
					statement.setString(4, messageList.get(pendingChoice - 1));
			
					confirmResult = statement.executeUpdate();

					// Now remove the pending request from the DB
					query = "DELETE pendingfriends WHERE fromID = ? AND toID = ?";
					statement = connection.prepareStatement(query);
					statement.setInt(1, fromIDList.get(pendingChoice - 1));
					statement.setInt(2, loggedInUser);

					confirmResult = statement.executeUpdate();

					// Also remove it from the two ArrayLists
					fromIDList.remove(pendingChoice - 1);
					messageList.remove(pendingChoice - 1);

					// If there are still more requests, print them and redo the loop
					if (fromIDList.size() > 0) {
						System.out.println("\n\nHere are your remaining pending friend requests:");
						System.out.println("Request Number - from UserID - Friend Request Message\n+++++++++++++++++++++++++");

						for (numRequests = 1; numRequests <= fromIDList.size(); numRequests++) {
							System.out.println(numRequests + " - UserID:" + fromIDList.get(numRequests - 1) + "  -  " + messageList.get(numRequests - 1));
						}
						System.out.println("");

						// Update my friends list
						existingFriends = getMyFriends(false);
					}
					else {
						System.out.println("\nNo more requests!\n");
						break;
					}
				}
			}
			else { // Confirm all
				// Move all of the friend relations into the friends table
				for (int i = 0; i < fromIDList.size(); i++) {
					
					if (existingFriends.contains(fromIDList.get(i))) {
						continue;
					}

					query = "INSERT INTO friends VALUES(?, ?, ?, ?)";
					statement = connection.prepareStatement(query);
					statement.setInt(1, fromIDList.get(i));
					statement.setInt(2, loggedInUser);
					statement.setString(3, dateString);
					statement.setString(4, messageList.get(i));
			
					confirmResult = statement.executeUpdate();
				}
			}

			// Finally, remove any remaining pendingfriends
			while (!fromIDList.isEmpty()) {
				// Now remove the pending request from the DB
				query = "DELETE pendingfriends WHERE fromID = ? AND toID = ?";
				statement = connection.prepareStatement(query);
				statement.setInt(1, fromIDList.get(0));
				statement.setInt(2, loggedInUser);

				confirmResult = statement.executeUpdate();

				fromIDList.remove(0);
				messageList.remove(0);
			}
		}
		catch(Exception Ex) {
			System.out.println("Error confirming friends.  Machine Error: " + Ex.toString());
			return 0;
		}

		return 1;
	}

	/**
	* Runs appropriate logout procedures.
	* 
	* logoutProcedure is used mainly to update the last login timestamp 
	* that is kept for each user as part of their profile. Any future 
	* logout procedures can be added here. Also sets loggedInUser to 
	* zero.
	* 
	* @return int containing the exit status of the method
	*/
	private int logoutProcedure() {
		dateString = df.format(new java.util.Date(System.currentTimeMillis()));
		int logoutResult = 0;

		try {
			query = "UPDATE profile SET lastlogin = ? WHERE userID = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, dateString);
			statement.setInt(2, loggedInUser);
			
			logoutResult = statement.executeUpdate();

			System.out.println("You have logged out. Goodbye!\n");
		}
		catch(Exception Ex) {
			System.out.println("Something bad happened while updating the last login?!\n");
			return 0;
		}
		
		// Reset this variable
		loggedInUser = 0;

		return logoutResult;
	}

	/**
	* Login to an existing account.
	* 
	* login allows a person to log in to an existing account. If the 
	* account does not exist, then it alerts the person.
	* 
	* @return int containing the exit status of the method
	*/
	private int login() {
		String email, password;
		boolean foundUser = false; // Set to false by default
		System.out.print("\nEnter your email: ");
		email = input.nextLine();
		
		System.out.print("Enter your password: ");
		password = input.nextLine();
		
		// Check this email and password, if it is valid just print out the info about the user :)
		
		try {
			//statement = connection.createStatement();
			query = "SELECT userID, name, lastlogin FROM profile WHERE email = ? AND password = ?"; // Check email and password for existence
			statement = connection.prepareStatement(query);
			statement.setString(1, email);
			statement.setString(2, password);
			
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				loggedInUser = resultSet.getInt(1);
				last_login = resultSet.getDate(3);
				foundUser = true;
				System.out.println("\n+++++++++++++++++++++++++\nWelcome " + resultSet.getString(2) + ". You are now logged in!");
				System.out.println("Your userID is " + loggedInUser + " by the way :)\nLast Login: " + resultSet.getString(3) + "\n+++++++++++++++++++++++++\n");
			}
			
		}
		catch(Exception Ex) {
			if (Ex.toString().indexOf("NULL") != -1) {
				System.out.println("You left one of the required fields blank! Try again.");
			}
			else {
				System.out.println("Error logging in.  Machine Error: " + Ex.toString());
			}
		}
		
		if (foundUser) {
			return 1;
		}
		else {
			return 0;
		}
	}

	/**
	* Register a new account.
	* 
	* register allows a person to register a new account on the 
	* network. It is self-contained and specifies the required 
	* input as part of the method.
	* 
	* @return int containing the exit status of the method
	*/
	private int register() {
		String register_name, register_email, register_password, register_dob, register_purl, register_aboutme, register_lastLogin;
		String verify_email;
		int register_result = 0;
		boolean valid_input = false; // For checking inputs
		// Fields needed: userID, name, email, password, date of birth, picture URL, aboutme, lastlogin)


		System.out.print("\nEnter your name (REQUIRED): ");
		register_name = input.nextLine();

		do {
			System.out.print("Enter your email (REQUIRED): ");
			register_email = input.nextLine();
			if (register_email.length() > 12){
				verify_email = register_email.substring(register_email.indexOf('@'));
				if (verify_email.equals("@pitt.edu")) {
					valid_input = true;
				}
				else {
					System.out.println("You entered an invalid (non-Pitt) email!");
				}
			}
			else {
				System.out.println("The email address you entered was invalid or too short!");
			}
		}while(!valid_input);
		
		
		System.out.print("Enter your password (REQUIRED): ");
		register_password = input.nextLine();

		System.out.print("Enter your date of birth (DD-MMM-YYYY eg: 09-NOV-2012): ");
		valid_input = false;
		do {
			register_dob = input.nextLine();
			if (checkDate(register_dob) == 1) {
				valid_input = true;
			}
			else {
				System.out.println("You entered an invalid date of birth! Try again.");
			}
		} while(!valid_input);

		System.out.print("Enter your picture url: ");
		register_purl = input.nextLine();

		System.out.print("Enter your about me: "); // Only one line allowed at the moment
		register_aboutme = input.nextLine();

		register_lastLogin = "";

		try {
			query = "INSERT INTO profile VALUES(seq_userID.nextval, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement register_statement = connection.prepareStatement(query);
			register_statement.setString(1, register_name);
			register_statement.setString(2, register_email);
			register_statement.setString(3, register_password);
			register_statement.setString(4, register_dob);
			register_statement.setString(5, register_purl);
			register_statement.setString(6, register_aboutme);
			register_statement.setString(7, register_lastLogin);
			
			register_result = register_statement.executeUpdate();
		}
		catch(Exception Ex) {
			if ((Ex.toString()).indexOf("unique") != -1) {
				System.out.println("This email has already been registered! Try again.");
			}
			else if (Ex.toString().indexOf("NULL") != -1) {
				System.out.println("You left one of the required fields blank! Try again.");
			}
			else {
				System.out.println("Error registering.  Machine Error: " + Ex.toString());
			}
		}

		return register_result;
	}

	/**
	* Gets the current user's friends.
	* 
	* getMyGroups gets all of the current user's groups and then returns 
	* them in an ArrayList. There is also the option to print the groups 
	* out, which may be used by specifying the appropriate argument. This 
	* is a helper method used by sendMessageGroup and joinGroup.
	* 
	* @param printList - boolean containing whether or not to print the groups
	* @return ArrayList containing the groupIDs of the current user's groups
	*/
	private ArrayList<Integer> getMyGroups(boolean printList) {
		ArrayList<Integer> returnList = new ArrayList<Integer>();

		try {
			query = "SELECT gID, name, description FROM groups WHERE gID IN (SELECT gID FROM groupMembership WHERE userID = ?) ORDER BY gID ASC";
			statement = connection.prepareStatement(query);
			statement.setInt(1, loggedInUser);
			
			resultSet = statement.executeQuery();
			
			if (printList) {
				System.out.println("\n+++++++++++++++++++++++++\nHere are your groups:\ngroupID - Name - Description");
			}

			while(resultSet.next()) {
				if (printList) {
					System.out.println(resultSet.getInt(1) + " - " + resultSet.getString(2) + " - " + resultSet.getString(3));
				}

				returnList.add(resultSet.getInt(1)); // Add to the list of my groups
			}

			if (printList) {
				System.out.println("\n+++++++++++++++++++++++++\n"); // Extra space
			}

		}
		catch(Exception Ex) {
			System.out.println("Error running getMyGroups.  Machine Error: " + Ex.toString());
		}

		return returnList;
	}

	/**
	* Gets the current user's friends.
	* 
	* getMyFriends gets all of the current user's friends and then returns 
	* them in an ArrayList. There is also the option to print the friends 
	* out, which may be used by specifying the appropriate argument. This 
	* is a helper method used by sendMessage, displayFriends, and others.
	* getMy
	* @param printList - boolean containing whether or not to print the friends
	* @return ArrayList containing the userIDs of the logged in user's friends
	*/
	private ArrayList<Integer> getMyFriends(boolean printList) {
		ArrayList<Integer> returnList = new ArrayList<Integer>();

		try {
			query = "SELECT profile.userID, name FROM profile, (SELECT * FROM ((SELECT userid1 AS userID FROM friends WHERE userid2 = ?) UNION (SELECT userid2 AS userID FROM friends WHERE userid1 = ?))) users WHERE profile.userid = users.userid ORDER BY profile.userID ASC";
			statement = connection.prepareStatement(query);
			statement.setInt(1, loggedInUser);
			statement.setInt(2, loggedInUser);
			
			resultSet = statement.executeQuery();
			if (printList) {
				System.out.println("\nHere are your friends:\n+++++++++++++++++++++++++\nUserID - Name");
			}
			
			while(resultSet.next()) {
				if (printList) {
					System.out.println(resultSet.getInt(1) + " - " + resultSet.getString(2));
				}
				returnList.add(resultSet.getInt(1)); // Add to the list of my friends
			}

			if (printList) {
				System.out.println("+++++++++++++++++++++++++\n"); // Extra space
			}

		}
		catch(Exception Ex) {
			System.out.println("Error running getMyFriends.  Machine Error: " + Ex.toString());
		}

		return returnList;
	}

	/**
	* Gets the current user's friends of friends.
	* 
	* getMyFriends gets all of the current user's friends of friends and  
	* then returns them in an ArrayList. There is also the option to print 
	* out, which may be used by specifying the appropriate argument. This 
	* is a helper method used by sendMessage, displayFriends, and others.
	* getMy
	* @param printList - boolean containing whether or not to print the friends
	* @return ArrayList containing the userIDs of the logged in user's friends
	*/
	private ArrayList<Integer> getFriendsOfFriends(boolean printList) {
		ArrayList<Integer> returnList = new ArrayList<Integer>();

		try {
			query = "SELECT profile.userID, name FROM profile JOIN ((SELECT userID1 AS theID FROM friends WHERE userID2 IN ((SELECT userID1 AS userID FROM friends WHERE userID2 = ?) UNION (SELECT userID2 AS userID FROM friends WHERE userID1 = ?))) UNION (SELECT userID2 AS theID FROM friends WHERE userID1 IN ((SELECT userID1 AS userID FROM friends WHERE userID2 = ?) UNION (SELECT userID2 AS userID FROM friends WHERE userID1 = ?)))) users ON profile.userID = users.theID WHERE profile.userID <> ? ORDER BY profile.userID ASC";
			statement = connection.prepareStatement(query);
			statement.setInt(1, loggedInUser);
			statement.setInt(2, loggedInUser);
			statement.setInt(3, loggedInUser);
			statement.setInt(4, loggedInUser);
			statement.setInt(5, loggedInUser);
			
			resultSet = statement.executeQuery();

			if (printList) {
				System.out.println("\nHere are your friends of friends:\n+++++++++++++++++++++++++\nUserID - Name");
			}
			
			while(resultSet.next()) {
				if (printList) {
					System.out.println(resultSet.getInt(1) + " - " + resultSet.getString(2));
				}
				returnList.add(resultSet.getInt(1)); // Add to the list of my friends of friends
			}

			if (printList) {
				System.out.println("+++++++++++++++++++++++++\n"); // Extra space
			}

		}
		catch(Exception Ex) {
			System.out.println("Error running getFriendsOfFriends.  Machine Error: " + Ex.toString());
		}

		return returnList;
	}


	/**
	* Displays the profiles that return as a result of a query.
	* 
	* displayProfiles uses a statement that has previously been 
	* set up to execute and then print the profiles in a nice 
	* and organized fashion.
	* REQUIRES STATEMENT TO HAVE BEEN SET PREVIOUSLY.
	* 
	* @return int containing the exit status of the method
	*/
	private int displayProfiles() {
		try {
			resultSet = statement.executeQuery();

			System.out.println("\n+++++++++++++++++++++++++\nProfile(s): ");

			while(resultSet.next()) {
				System.out.println("Name: " + resultSet.getString(1) +
					"\nEmail: " + resultSet.getString(2) + "\nDate of Birth: " +
					resultSet.getString(3) + "\nAbout Me: " + resultSet.getString(5) + 
					"\nPicture URL: " + resultSet.getString(4) + "\nLast Login: " + resultSet.getString(6) + "\n\n");
			}

			System.out.println("\n+++++++++++++++++++++++++\n"); // Extra space
		}
		catch (Exception Ex) {
			System.out.println("\nError displaying the profile.\n");
			return 0;
		}

		return 1;
	}

	/**
	* Display the user's friends.
	* 
	* displayFriends is used to display the logged in user's 
	* friends. It then allows the user to display the full profile 
	* of any of the friends. Uses a helper method to get and 
	* display the friends, as well as display the profile(s).
	* 
	* @return int containing the exit status of the method
	*/
	private int displayFriends() {
		int display_choice = 0;;
		ArrayList<Integer> myFriends;
		ArrayList<Integer> friendsOfFriends;

		// Get list of friends. Set to true to print it as well.
		myFriends = getMyFriends(true);
		friendsOfFriends = getFriendsOfFriends(true);

		try {
			do {
				System.out.print("\nEnter the userID for full profile (or 0 to go home): ");
				do {
					if (input.hasNextInt()) {
						display_choice = input.nextInt();
						break;
					}
					else {
						System.out.println("You did not enter a number, try again.");
						input.nextLine();
					}
				}while(true);

				
				if (display_choice == 0) {
					return 0;
				}
				else if (myFriends.contains(display_choice) || friendsOfFriends.contains(display_choice)) {
					query = "SELECT name, email, date_of_birth, picture_URL, aboutme, lastlogin FROM profile WHERE userID = ?";
					statement = connection.prepareStatement(query);
					statement.setInt(1, display_choice);
					// Call the displayProfiles method to actually execute and display the profile(s)
					displayProfiles();
				}
				else {
					System.out.println("You entered an invalid userID!");
				}
			} while(true);
		}
		catch(Exception Ex) {
			System.out.println("Error running the queries.  Machine Error: " + Ex.toString());
		}
		return 0;
	}
	
	/**
	* Search for a user in the network.
	* 
	* findUser has the user input a/some search term(s) and 
	* then searches through all relevant fields in the database 
	* for the string(s). Then it calls a helper method to 
	* display the appropriate profiles.
	* 
	* @return int containing the exit status of the method
	*/
	private int findUser() {
		String[] searchTerms;
		String searchString;
		int numberOfTerms = 0;
		int counter;
		String toCat = "name || email || date_of_birth || picture_URL || aboutme LIKE ?";

		System.out.print("\nEnter some text to search for: \n");
		searchString = input.nextLine();
		searchString = input.nextLine();
		searchTerms = searchString.split(" ");

		numberOfTerms = searchTerms.length;

		if (numberOfTerms > 0) {
			try {
				query = "SELECT name, email, date_of_birth, picture_URL, aboutme, lastlogin FROM profile WHERE ";
				// This for loop sets up the desired number of LIKE comparisons
				for (counter = 1; counter <= numberOfTerms; counter++) {
					if (counter > 1) {
						query = query.concat(" OR ");
					}
					query = query.concat(toCat);
				}
				
				statement = connection.prepareStatement(query);

				// This for loop fills in what to compare to using the searchTerms array
				for (counter = 1; counter <= numberOfTerms; counter++) {
					statement.setString(counter, "%" + searchTerms[counter - 1] + "%");
				}

				// Call the displayProfiles method to actually execute and display the profile(s)
				displayProfiles();
			}
			catch(Exception Ex) {
			System.out.println("Error in findUser.  Machine Error: " + Ex.toString());
			}

			return 0;
		}
		else {
			System.out.println("You didn't enter anything to search for.\nReturning to the main menu.\n\n");
			return 1;
		}
	}

	/**
	* Gets the lists of all users in the network.
	* 
	* getListOfUsers takes two ArrayLists, one for IDs and one for names, 
	* and then adds to them all of the users in the network. It is a 
	* helper method. It does NOT include the logged in user.
	*
	* @param userIDList - List of userIDs of all users, modified by method
	* @param nameList - List of names of all users, modified by method
	* @return int containing the exit status of the method
	*/
	private int getListOfUsers(ArrayList<Integer> userIDList, ArrayList<String> nameList) {
		try {
			query = "SELECT userID, name FROM profile WHERE userID <> ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, loggedInUser);
			
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				userIDList.add(resultSet.getInt(1)); // Add to the list of userIDs
				nameList.add(resultSet.getString(2)); // Add to the list of names
			}
		}
		catch(Exception Ex) {
			System.out.println("Error running getListOfUsers.  Machine Error: " + Ex.toString());
			return 0;
		}

		return 1;
	}

	/**
	* Add a friend.
	*
	* addFriend shows all the users in the network, and then 
	* offers to let the logged in user add a new friend. If 
	* the person is already your friend, or you already have 
	* a pending request, it alerts the user.
	*
	* @return int containing the exit status of the method
	*/
	private int addFriend() {
		boolean runAgain, continueLoop;
		int helper_return, add_choice, addResult;
		do {
			ArrayList<Integer> myFriendsList;
			ArrayList<Integer> userIDList = new ArrayList<Integer>();
			ArrayList<String> nameList = new ArrayList<String>();
			helper_return = 0;
			add_choice = 0;
			addResult = 0;
			continueLoop = true;
			runAgain = false;

			myFriendsList = getMyFriends(false);
			helper_return = getListOfUsers(userIDList, nameList);

			if (helper_return == 0) {
				System.out.println("Something went wrong?!");
				return 0;
			}

			System.out.println("\nHere are all the users in the network:\nUserID - Name\n+++++++++++++++++++++++++");

			for (int i = 0; i < userIDList.size(); i++) {
				System.out.println(userIDList.get(i) + " - " + nameList.get(i));
			}

			System.out.print("+++++++++++++++++++++++++\n\nEnter the userID of the person you want to friend (or 0 to quit): ");
			do {
				if (input.hasNextInt()) {
					add_choice = input.nextInt();
					break;
				}
				else {
					System.out.println("You did not enter a number, try again.");
					input.nextLine();
				}
			}while(true);

			if (add_choice == 0) {
				System.out.println("Returning to the main menu.\n\n");
				return 1;
			}
			
			if (myFriendsList.contains(add_choice)) {
				System.out.println(nameList.get(userIDList.indexOf(add_choice)) + " is already your friend!\nReturning to the main menu.\n\n");
				return 1;
			}

			System.out.println("You are going to add " + nameList.get(userIDList.indexOf(add_choice)) + " as a friend.\n" + 
				"Enter the message you want to include:");

			StringBuilder buildMessage = new StringBuilder();
			String tempString;
			input.nextLine();

			continueLoop = true;
			do {
				tempString = input.nextLine();

				if (tempString.equals("")) {
					continueLoop = false;
				}
				else {
					buildMessage.append(tempString);
				}
			} while(continueLoop);


			System.out.println("Are you really sure you want to add this person?");
			System.out.print("Enter yes to continue: ");
			tempString = input.nextLine();

			if (tempString.toLowerCase().equals("yes")) {
				try {
				query = "INSERT INTO pendingfriends VALUES(?, ?, ?)";
				statement = connection.prepareStatement(query);
				statement.setInt(1, loggedInUser);
				statement.setInt(2, add_choice);
				statement.setString(3, buildMessage.toString());

				
				addResult = statement.executeUpdate();

				System.out.println("\nSuccess! You added a new friend!\n\n");
				}
				catch(Exception Ex) {
					if ((Ex.toString()).indexOf("unique") != -1) {
						System.out.println("You aready have a pending request to this person!\n");
					}
					else {
						System.out.println("Error adding a friend!  Machine Error: " + Ex.toString());
					}
					
					return 0;
				}
			}
			else {
				System.out.println("You chose not to add a friend.\n");
			}

			System.out.print("\n+++++++++++++++++++++++++\nWould you like to:\n1 - Add a Friend\nAny other number - Return to Main Menu\nEnter number value: ");
			
			do {
				if (input.hasNextInt()) {
					add_choice = input.nextInt();
					break;
				}
				else {
					System.out.println("You did not enter a number, try again.");
					input.nextLine();
				}
			}while(true);

			if (add_choice == 1) {
				runAgain = true;
			}
		}while(runAgain);

		System.out.println("Returning to the main menu.\n\n");

		return 1;
	}

	/**
	* Performs the three degrees of separation task.
	*
	* degreesSeparation is used to create a social graph of 
	* the network and then determine the smallest number of
	* hops from a user to other users. If it is 3 hops or less, 
	* it tells the user the path.
	*
	* @return int containing the exit status of the method
	*/
	private int degreesSeparation() {
		int the_maxID = 0;
		int degrees_choice = 0;
		Graph socialGraph;
		String columnValue;

		try {
			// Need to get the max userID to create the social graph
			query = "SELECT MAX(userID) FROM profile";
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();
			resultSet.next(); // Move to the first row
			the_maxID = resultSet.getInt(1); // Grab the max ID from the resultSet

			socialGraph = new Graph(the_maxID); // Create a new social graph

			query = "SELECT userID, name, email FROM profile ORDER BY userID ASC";
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();

			System.out.println("\nHere are all the users in the network:\nUserID - Name - Email\n+++++++++++++++++++++++++");

			while(resultSet.next()) {
				socialGraph.addVertex(resultSet.getString(2), resultSet.getInt(1)); // Add user to graph
				System.out.println(resultSet.getInt(1) + " - " + resultSet.getString(2) + " - " + resultSet.getString(3));
			}
			
			System.out.println("\n+++++++++++++++++++++++++\n"); // Extra space

			System.out.print("\nEnter the userID to check for 3 degrees: ");
			
			do {
					if (input.hasNextInt()) {
						degrees_choice = input.nextInt();
						break;
					}
					else {
						System.out.println("You did not enter a number, try again.");
						input.nextLine();
					}
				}while(true);


			if (degrees_choice == 0) {
				return 0;
			}
			else if (degrees_choice > the_maxID) {
				System.out.println("Invalid ID. Returning to main menu.\n\n");
				return 0;
			}
			else {
				// Get the friends to create the edges of the graph
				query = "SELECT userID1, userID2 FROM friends";
				statement = connection.prepareStatement(query);
				resultSet = statement.executeQuery();

				while(resultSet.next()) {
					socialGraph.addEdge(resultSet.getInt(1), resultSet.getInt(2), 1); // Add edge to graph, weight of 1 is default
				}

				socialGraph.djikstraCompute(loggedInUser);

				if (socialGraph.vertices[degrees_choice].minDistance <= 3) {
					System.out.println("Shortest distance to user " + socialGraph.vertices[degrees_choice].vertexName + ": " + socialGraph.vertices[degrees_choice].minDistance);
					List<Vertex> path = socialGraph.getShortestPathTo(degrees_choice);
					System.out.println("Path to get there: " + path + "\n");
				}
				else {
					System.out.println("That is more than 3 degrees of separation! :(\n");
				}
			}
		}
		catch(Exception Ex) {
			System.out.println("Error. You entered an invalid ID!\n");
			// Remove the following line later
			System.out.println("Error running the queries.  Machine Error: " + Ex.toString());
		}

		return 0;
	}


	/**
	* Sends a message.
	*
	* sendMessage is called to send a message. It then calls one 
	* of the helper methods after the user chooses who to send 
	* the message to.
	*
	* @return int containing the exit status of the method
	*/
	private int sendMessage() {
		int send_choice;
		int send_return = 0;

		System.out.println("\nWho would you like to send a message to?\n+++++++++++++++++++++++++");
		System.out.println("1 - Send to Friend(s)\n2 - Send to Group\n3 - Return to Main Menu\n+++++++++++++++++++++++++");
		System.out.print("Enter the numbered value: ");
		do {
			if (input.hasNextInt()) {
				send_choice = input.nextInt();
				break;
			}
			else {
				System.out.println("You did not enter a number, try again.");
				input.nextLine();
			}
		}while(true);

		switch (send_choice) {
			case 1: // Send to user(s)
				send_return = sendMessageUser();
				break;
			case 2: // Send to group
				send_return = sendMessageGroup();
				break;
			case 3:
				System.out.println("Returning to the main menu.\n");
				return 0;
			default:
				System.out.println("You entered an invalid choice!\n");
				break;
		}

		return send_return;
	}

	/**
	* Sends a message to a user or users.
	*
	* sendMessageUser is called to send a message to a specific
	* user or several users. It is called from sendMessage and 
	* is thus a helper method and is not directly called.
	*
	* @return int containing the exit status of the method
	*/
	private int sendMessageUser() {
		ArrayList<Integer> myFriends;
		int numUsers = 0;
		int i = 0;
		int tempID = 0;
		int sendResult = 0;
		boolean continueLoop = true;

		myFriends = getMyFriends(true);

		try {
			System.out.print("\nHow many users do you want to send this message to (or 0 to return to the menu)?: ");
			do {
				if (input.hasNextInt()) {
					numUsers = input.nextInt();
					break;
				}
				else {
					System.out.println("You did not enter a number, try again.");
					input.nextLine();
				}
			}while(true);
			
			if (numUsers == 0) {
				return 0;
			}

			int[] userArray = new int[numUsers];

			while (i < numUsers) {
				System.out.print("Enter the userID of person " + (i + 1) + ": ");
				do {
					if (input.hasNextInt()) {
						tempID = input.nextInt();
						break;
					}
					else {
						System.out.println("You did not enter a number, try again.");
						input.nextLine();
					}
				}while(true);


				if (myFriends.contains(tempID)) {
					userArray[i] = tempID;
					i++;
				}
				else {
					System.out.println("That person is not your friend!");
				}
			}

			System.out.println("Enter the message you want to send. Leave the line blank to stop:");

			StringBuilder buildMessage = new StringBuilder();
			String tempString;

			continueLoop = true;
			tempString = input.nextLine();
			do {
				tempString = input.nextLine();

				if (tempString.equals("")) {
					continueLoop = false;
				}
				else {
					buildMessage.append(tempString);
				}
			} while(continueLoop);

			dateString = df.format(new java.util.Date(System.currentTimeMillis()));

			for (i = 0; i < numUsers;i++) {
				query = "INSERT INTO messages VALUES(seq_msgID.nextval, ?, ?, ?, NULL, ?)";
				statement = connection.prepareStatement(query);
				statement.setInt(1, loggedInUser);
				statement.setString(2, buildMessage.toString());
				statement.setInt(3, userArray[i]);
				statement.setString(4, dateString);
				
				sendResult = statement.executeUpdate();
			}

			System.out.println("Success sending the message!\nReturning to the main menu.\n\n");
			return 1;
		}
		catch(Exception Ex) {
			System.out.println("Error sending a message.  Machine Error: " + Ex.toString());
		}

		return 0;
	}

	/**
	* Sends a message to a group.
	*
	* sendMessageGroup is called from sendMessage when the user
	* chooses to send a message to a whole group. Thus, it is not 
	* called directly and is a helper method.
	*
	* @return int containing the exit status of the method
	*/
	private int sendMessageGroup() {
		ArrayList<Integer> myGroups;
		int tempGroupID = 0;
		int sendResult = 0;
		boolean continueLoop = true;

		// Get and print groups
		myGroups = getMyGroups(true);

		try {
			do {
				System.out.print("Enter the group ID of the group you want to message (or 0 to go back to the menu): ");
				do {
					if (input.hasNextInt()) {
						tempGroupID = input.nextInt();
						break;
					}
					else {
						System.out.println("You did not enter a number, try again.");
						input.nextLine();
					}
				}while(true);


				if (myGroups.contains(tempGroupID)) {
					continueLoop = false;
				}
				else if (tempGroupID == 0) {
					return 0;
				}
				else {
					System.out.println("That is not a group you belong to!");
				}

			} while(continueLoop);

			System.out.println("Enter the message you want to send. Leave the line blank to stop:");

			StringBuilder buildMessage = new StringBuilder();
			String tempString;

			continueLoop = true;
			tempString = input.nextLine();
			do {
				tempString = input.nextLine();

				if (tempString.equals("")) {
					continueLoop = false;
				}
				else {
					buildMessage.append(tempString);
				}
			} while(continueLoop);

			dateString = df.format(new java.util.Date(System.currentTimeMillis()));

			query = "INSERT INTO messages VALUES(seq_msgID.nextval, ?, ?, NULL, ?, ?)";
			statement = connection.prepareStatement(query);
			statement.setInt(1, loggedInUser);
			statement.setString(2, buildMessage.toString());
			statement.setInt(3, tempGroupID);
			statement.setString(4, dateString);
			
			sendResult = statement.executeUpdate();

			return 1;
		}
		catch(Exception Ex) {
			System.out.println("Error running the queries.  Machine Error: " + Ex.toString());
		}

		return 0;
	}

	/**
	* Grabs and prints statistics on friends.
	*
	* myStats is used to print statistics regarding the specified
	* top number of users in the last x number of months. The user
	* specifies both number of users and number of months.
	*
	* @return int containing the exit status of the method
	*/
	private int myStats() {
		int numMonths = 0;
		int numTopUsers = 0;
		int typeOfStats = 0;

		System.out.print("\nHow many months do you want to check? (0 to quit): ");
		do {
			if (input.hasNextInt()) {
				numMonths = input.nextInt();
				break;
			}
			else {
				System.out.println("You did not enter a number, try again.");
				input.nextLine();
			}
		}while(true);

		if (numMonths == 0) {
			return 0;
		}

		System.out.print("\nHow many top users do you want to see? (0 to quit): ");
		do {
			if (input.hasNextInt()) {
				numTopUsers = input.nextInt();
				break;
			}
			else {
				System.out.println("You did not enter a number, try again.");
				input.nextLine();
			}
		}while(true);
		
		if (numTopUsers == 0) {
			return 0;
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 0 - numMonths);
		dateString = df.format(cal.getTime());

		System.out.println("There are two types of stats:\n1 - Number of messages sent and received by friends with just you\n2 - Number of messages sent and received by friends total.");
		System.out.print("Which kind of stats do you want (or 0 to quit): ");
		do {
			if (input.hasNextInt()) {
				typeOfStats = input.nextInt();
				break;
			}
			else {
				System.out.println("You did not enter a number, try again.");
				input.nextLine();
			}
		}while(true);

		if ((typeOfStats != 1) && (typeOfStats != 2)) {
			System.out.println("Returning to main menu.\n\n");
			return 0;
		}

		try {
			
			if (typeOfStats == 2) {
				// Yes I'm well aware that this is a disgusting query :)
				query = "SELECT theID, name, totalCount FROM \n" + 
					"(SELECT theID, name, NVL(sentCount,0) + NVL(receiveCount,0) + NVL(receiveGroupCount,0) totalCount FROM (((SELECT profile.userid theID, name FROM profile, (SELECT * FROM \n" + 
					"((SELECT userid1 AS userID FROM friends WHERE userid2 = ?) UNION (SELECT userid2 AS userID FROM friends WHERE userid1 = ?))) users WHERE profile.userid = users.userid) LEFT OUTER JOIN \n" + 
					"(SELECT fromID, COUNT(msgID) sentCount FROM messages WHERE dateSent >= ? GROUP BY fromID) ON theID = fromID) LEFT OUTER JOIN \n" + 
					"(SELECT toID, COUNT(msgID) receiveCount FROM messages WHERE dateSent >= ? GROUP BY toID) ON theID = toID) LEFT OUTER JOIN (SELECT userID g_userID, COUNT(msgID) receiveGroupCount FROM messageRecipients JOIN \n" + 
					"(SELECT msgID mrID FROM messages WHERE dateSent > ?) ON msgID = mrID GROUP BY userID) ON theID = g_userID ORDER BY totalCount DESC) WHERE rownum <= ?";

				statement = connection.prepareStatement(query);
				statement.setInt(1, loggedInUser);
				statement.setInt(2, loggedInUser);
				statement.setString(3, dateString);
				statement.setString(4, dateString);
				statement.setString(5, dateString);
				statement.setInt(6, numTopUsers);
			}

			if (typeOfStats == 1) {

				query = "SELECT theID, name, totalCount FROM \n" + 
					"(SELECT theID, name, NVL(sentCount,0) + NVL(receiveCount,0) + NVL(receiveGroupCount,0) totalCount FROM (((SELECT profile.userid theID, name FROM profile, (SELECT * FROM \n" + 
					"((SELECT userid1 AS userID FROM friends WHERE userid2 = ?) UNION (SELECT userid2 AS userID FROM friends WHERE userid1 = ?))) users WHERE profile.userid = users.userid) LEFT OUTER JOIN \n" + 
					"(SELECT fromID, COUNT(msgID) sentCount FROM messages WHERE dateSent >= ? AND (toID = ? OR msgID IN (SELECT msgID FROM messageRecipients WHERE userID = ?)) GROUP BY fromID) ON theID = fromID) LEFT OUTER JOIN \n" + 
					"(SELECT toID, COUNT(msgID) receiveCount FROM messages WHERE dateSent >= ? AND fromID = ? GROUP BY toID) ON theID = toID) LEFT OUTER JOIN (SELECT userID g_userID, COUNT(msgID) receiveGroupCount FROM messageRecipients JOIN \n" + 
					"(SELECT msgID mrID FROM messages WHERE dateSent > ? AND fromID = ?) ON msgID = mrID GROUP BY userID) ON theID = g_userID ORDER BY totalCount DESC) WHERE rownum <= ?";

				statement = connection.prepareStatement(query);
				statement.setInt(1, loggedInUser);
				statement.setInt(2, loggedInUser);
				statement.setString(3, dateString);
				statement.setInt(4, loggedInUser);
				statement.setInt(5, loggedInUser);
				statement.setString(6, dateString);
				statement.setInt(7, loggedInUser);
				statement.setString(8, dateString);
				statement.setInt(9, loggedInUser);
				statement.setInt(10, numTopUsers);
			}

			resultSet = statement.executeQuery();

			System.out.println("\n+++++++++++++++++++++++++\nHere are your requested statistics: ");
			while(resultSet.next()) {
				System.out.println("UserID: " + resultSet.getInt(1) + "  Name: " + resultSet.getString(2) +"  Number of Messages: " + resultSet.getInt(3));
			}

			System.out.println("+++++++++++++++++++++++++\nReturning to the main menu.\n\n");
		}
		catch(Exception Ex) {
			System.out.println("Error running myStats.  Machine Error: " + Ex.toString());
			return 0;
		}

		return 1;
	}

	/**
	* Drops the current user account.
	*
	* dropAccount is called when the user wants to delete their 
	* account and any related data. It uses a SQL trigger to 
	* delete the data outside of profile.
	*
	* @return int containing the exit status of the method
	*/
	private int dropAccount() {
		String drop_choice;
		PreparedStatement drop_statement;
		int drop_result = 0;

		System.out.print("\n\nARE YOU SURE YOU WANT TO DROP YOUR ACCOUNT?!\nType yes to continue: ");
		drop_choice = input.nextLine();
		drop_choice = input.nextLine();

		drop_choice = drop_choice.toLowerCase();

		if (!drop_choice.equals("yes")) {
			return 0;
		}

		System.out.print("\nARE YOU DEFINITELY SURE...?!\nType yes to continue: ");
		drop_choice = input.nextLine();

		drop_choice = drop_choice.toLowerCase();

		if (!drop_choice.equals("yes")) {
			return 0;
		}

		try {
			query = "DELETE profile WHERE userID = ?";
			drop_statement = connection.prepareStatement(query);
			drop_statement.setInt(1, loggedInUser);
			
			drop_result = drop_statement.executeUpdate();

		}
		catch(Exception Ex) {
			System.out.println("Error in dropAccount.  Machine Error: " + Ex.toString());
		}

		return 1;
	}

	/**
	* Displays the messages a user has received.
	* 
	* displayMessages is the main method to display the messages 
	* that a user has received. The user can choose to either 
	* display all messages or just those that have been received 
	* following or on their last login.
	* 
	* @return int containing the exit status of the method
	*/
	private int displayMessages() {
		int messageChoice = 0;
		int messageReturn = 0;

		System.out.println("What kind of messages do you want to display?\n1 - All Messages\n2 - New Messages\n0 - Return to menu");
		System.out.print("Enter a number: ");
		do {
			if (input.hasNextInt()) {
				messageChoice = input.nextInt();
				if (messageChoice == 1 || messageChoice == 2 || messageChoice == 0) {
					break;
				}
				else {
					System.out.println("Invalid choice, try again.");
				}
			}
			else {
				System.out.println("You did not enter a number, try again.");
				input.nextLine();
			}
		}while(true);

		if (messageChoice == 1) {
			messageReturn = displayAllMessages();
		}
		
		if (messageChoice == 2) {
			messageReturn = displayNewMessages();
		}

		System.out.println("Returning to main menu.\n\n");

		return messageReturn;
	}

	/**
	* Display all messages a user has received (i.e. their inbox).
	* 
	* displayAllMessages is a helper method for displayMessages that 
	* displays all messages that a user has ever received. This includes 
	* both old and new messages.
	* 
	* @return int containing the exit status of the method
	*/
	private int displayAllMessages() {
		try {
			query = "SELECT msgID, userID, name, dateSent, message FROM profile JOIN messages ON userID = fromID WHERE toID = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, loggedInUser);

			resultSet = statement.executeQuery();

			System.out.println("\n\nHere are your personal messages:");

			while(resultSet.next()) {
				printMessage(resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
			}

			query = "SELECT m.msgID, mr.userID, p.name, m.dateSent, m.message FROM (messages m JOIN messagerecipients mr ON m.msgID = mr.msgID) JOIN profile p ON fromID = p.userID WHERE mr.userID = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, loggedInUser);

			resultSet = statement.executeQuery();

			System.out.println("\n+++++++++++++++++++++++++\n\nHere are your group messages:");

			while(resultSet.next()) {
				printMessage(resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
			}
		}
		catch (Exception Ex) {
			System.out.println("Error displaying all messages.");
			return 0;
		}

		System.out.println("\n+++++++++++++++++++++++++\n\nDone displaying messages.");
		return 1;
	}

	/**
	* Displays only new messages.
	* 
	* displayNewMessages is a helper method to displayMessages 
	* that displays the messages a user has received since they 
	* last logged on. This includes messages sent on the same day 
	* as last login.
	* 
	* @return int containing the exit status of the method
	*/
	private int displayNewMessages() {
		try {
			query = "SELECT msgID, userID, name, dateSent, message FROM profile JOIN messages ON userID = fromID WHERE toID = ? AND dateSent >= ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, loggedInUser);
			statement.setDate(2, last_login);
			
			resultSet = statement.executeQuery();

			System.out.println("\n\nHere are your new personal messages:");

			while(resultSet.next()) {
				printMessage(resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
			}

			query = "SELECT m.msgID, mr.userID, p.name, m.dateSent, m.message FROM (messages m JOIN messagerecipients mr ON m.msgID = mr.msgID) JOIN profile p ON fromID = p.userID WHERE mr.userID = ? AND m.dateSent >= ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, loggedInUser);
			statement.setDate(2, last_login);
			
			resultSet = statement.executeQuery();

			System.out.println("\n+++++++++++++++++++++++++\n\nHere are your new group messages:");

			while(resultSet.next()) {
				printMessage(resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
			}
		}
		catch (Exception Ex) {
			System.out.println("Error displaying new messages.\nReturning to main menu.");
			return 0;
		}

		System.out.println("\n+++++++++++++++++++++++++\n\nDone displaying messages.");
		return 1;
	}

	/**
	* Prints the contents of a message.
	* 
	* printMessage is used to print the contents of a message. It is
	* a helper method for the displayMessages methods. 
	* Does not return anything.
	* 
	* @param theUserID - userID to display
	* @param theName - the name to display
	* @param theDateSent - the date to display
	* @param theMessage - the message to display
	*/
	private void printMessage(int theUserID, String theName, String theDateSent, String theMessage) {
		System.out.println("\n-------------------------\nFrom UserID: " + theUserID + 
							"\nName: " + theName +
							"\nDate sent: " + theDateSent +
							"\nMessage: " + theMessage +
							"\n-------------------------");
		return;
	}

	/**
	* Allows the user to join a group.
	* 
	* joinGroup is used to join a group. Makes sure you are not 
	* going to part of too many groups (>10) and that you are 
	* not already part of the group you are trying to join.
	* 
	* @return int containing the exit status of the method
	*/
	private int joinGroup() {
		ArrayList<Integer> myGroups;
		int joinChoice = 0;
		int joinResult = 0;
		boolean continueLoop = true;

		System.out.println("Here are the groups you are already part of:");
		myGroups = getMyGroups(true); // Set to true to print the groups

		try {
			System.out.println("Here are all of the groups in the network:" + 
				"\n+++++++++++++++++++++++++\nGroup ID - Name - Description");

			query = "SELECT gID, name, description FROM groups ORDER BY gID asc";
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				System.out.println(resultSet.getInt(1) + " - " + resultSet.getString(2) + " - " + resultSet.getString(3));
			}

			System.out.println("+++++++++++++++++++++++++");

			do {
				System.out.print("Enter the Group ID you want to join (or 0 to quit): ");
				do {
					if (input.hasNextInt()) {
						joinChoice = input.nextInt();
						break;
					}
					else {
						System.out.println("You did not enter a number, try again.");
						input.nextLine();
					}
				}while(true);

				if (joinChoice == 0) {
					continueLoop = false;
				}
				else if (myGroups.indexOf(joinChoice) != -1) {
					System.out.println("You are already a member of that group! Try again.");
				}
				else if (myGroups.size() >= 10) {
					System.out.println("You are already a member of 10 groups!");
					continueLoop = false;
				}
				else {
					query = "INSERT INTO groupMembership VALUES (?, ?)";
					statement = connection.prepareStatement(query);
					statement.setInt(1, joinChoice);
					statement.setInt(2, loggedInUser);

					joinResult = statement.executeUpdate();

					System.out.println("Success in joining group " + joinChoice + "!");
					
					myGroups = getMyGroups(false); // Grab an updated list, set to false not to print
				}
			} while(continueLoop);
		}
		catch (Exception Ex) {
			if ((Ex.toString()).indexOf("GROUPMEMBERSHIP_FK1") != -1) { // If the group doesn't exist
				System.out.println("The group you are trying to join does not exist.");
			}
			else if ((Ex.toString()).indexOf("GROUPMEMBERSHIP_FK2") != -1) { // If the group doesn't exist
				System.out.println("The user trying to join a group does not exist?!");
			}
			else if ((Ex.toString()).indexOf("unique") != -1) { // If you are already part of the group
				System.out.println("You are already part of that group!");
			}
			else if ((Ex.toString()).indexOf("MAXGROUPCHECK") != -1) { // If you are already part of 10 groups
				// Note: This only happens if two people on two machines try to join another group at the same time.
				System.out.println("You are already a memember of 10 groups! Concurrency!");
			}
			else {
				System.out.println("Error joining a group. Error: " + Ex.toString());
			}

			System.out.println("Returning to main menu.\n\n");
			return 0;
		}

		System.out.println("Returning to main menu.\n\n");
		return joinResult;
	}


	/**
	* Main method of the program.
	*
	* This is the main method of the program. Instanciates a new facesApp
	* object to run the program.
	*
	* @param  args - This is the command line argument array.
	*/
	public static void main(String args[]) {
		facesApp facesSession = new facesApp();
	}
}

// This is used for degrees of separation.
// It is a basic graph implementation in order to do Djikstra's shortest path.
class Graph {
	private int max_userID;
	Vertex[] vertices;
	
    public Graph (int maxID) {
		max_userID = maxID;
		vertices = new Vertex[max_userID + 1];
	}
	
	public void addVertex(String name, int id) {
		Vertex v = new Vertex(name, id);
		vertices[id] = v;
	}
	
	public void addEdge(int ID1, int ID2, int weight) {
		// You have to add the edge to both vertices, otherwise it doesn't work right
		// Also, weight isn't really used, but it's part of Djikstra's so I'm including it
		vertices[ID1].adjacencies.add(new Edge(vertices[ID2], weight));
		vertices[ID2].adjacencies.add(new Edge(vertices[ID1], weight));
	}
	
	// Compute vis Djiktra's the shortest path from the source
	public void djikstraCompute(int theSource)
    {
		Vertex source = vertices[theSource];
		int distanceViaVertexU;
		source.minDistance = 0;
        PriorityQueue<Vertex> vertexPQ = new PriorityQueue<Vertex>();
		vertexPQ.add(source);

		while (!vertexPQ.isEmpty()) {
			Vertex tempVertex = vertexPQ.poll();
			Edge[] tempAdj = tempVertex.adjacencies.toArray(new Edge[tempVertex.adjacencies.size()]);

			for (Edge e : tempAdj) {
				Vertex tempVertexB = e.target;
				int weight = e.weight;
				distanceViaVertexU = tempVertex.minDistance + weight;
				if (distanceViaVertexU < tempVertexB.minDistance) {
					vertexPQ.remove(tempVertexB);
					tempVertexB.minDistance = distanceViaVertexU ;
					tempVertexB.previous = tempVertex;
					vertexPQ.add(tempVertexB);
				}
			}
		}
    }

    // Returns a path that is the shortest path
	public List<Vertex> getShortestPathTo(int targetID) {
		Vertex target = vertices[targetID];
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous) {
			path.add(vertex);
		}
		
		Collections.reverse(path);
		return path;
	}
	
}

// Inner class Vertex, used by the Graph class.
class Vertex implements Comparable<Vertex> {
	public String vertexName;
	public int userID;
	public ArrayList<Edge> adjacencies;
	public int minDistance = Integer.MAX_VALUE;
	public Vertex previous;
	
	public Vertex(String theName, int theID) {
		vertexName = theName;
		userID = theID;
		adjacencies = new ArrayList<Edge>();
	}
	
	public String toString() {
		return vertexName;
	}
	
	public int compareTo(Vertex otherVertex) {
		return (minDistance - otherVertex.minDistance);
	}

}

// Inner class Edge, used by the Graph class.
class Edge {
	public Vertex target; // The target vertex
	public int weight; // Weight of the edge

	public Edge(Vertex theTarget, int theWeight) {
		target = theTarget;
		weight = theWeight;
	}
}