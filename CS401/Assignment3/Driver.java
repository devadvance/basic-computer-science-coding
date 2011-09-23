/**
Copyright (c) 2011, encryptstream
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
CS401
Assignment 3
*/

/** *************************** Driver.java - *************************************************
***	This is the Driver to run the FSA class. This allows the user to pick a specific FSM	***
***	to run. Then the user can choose whether or not to display the machine, including the	***
***	transitions. A tape can be entered, and all choices and inputs are checked to make sure	***
***	that they are correct. The user can also re-rerun the program following a tape.			***
*** INCLUDED MACHINES: "divisibleby3.dat", "exactlytwoas.dat", "dragon.dat"					***
***********************************************************************************************/

import java.util.Scanner; // Required to get name of file and to interact with user
import java.io.*; // Required for input-output operations and exceptions

public class Driver
{
	public static void main(String[] args) throws FileNotFoundException
	{
		String thefilename,choice,thetape;
		Scanner input = new Scanner(System.in);
		boolean badinput,repeat = false;
		FSA machine;
		
		System.out.println("-----------------------------------------------------------------------------\n");
		System.out.println("|| INCLUDED MACHINES: \"divisibleby3.dat\", \"exactlytwoas.dat\", \"dragon.dat\" ||");
		System.out.println("-----------------------------------------------------------------------------\n");
		
		badinput = true;	// Initialize the boolean for the next loop
		do
		{
			System.out.print("\nDo you want to run a finite-state machine? (y or n)--> ");
			choice = input.nextLine();
			if (choice.equalsIgnoreCase("y"))	// Run the finite-state machine
			{
				repeat = true;
				badinput = false;
			}
			else if (choice.equalsIgnoreCase("n"))	// Do not run machine
			{
				repeat = false;
				badinput = false;
			}
			else	// Invalid input
			{
				System.out.println("You entered an invalid choice. Please try again.");
				badinput = true;
			}
		} while (badinput);
		
		while (repeat)
		{
			badinput = true;	// Set boolean for first loop
			do
			{
				System.out.print("Enter file where finite-state machine is located--> ");
				thefilename = input.nextLine();
				
				try	// To prevent exiting on typo
				{
				machine = new FSA(thefilename);
				badinput = false;
				System.out.println(machine.name() + " has been initialized.");
				}
				catch (Exception e)
				{
				System.out.println("You entered an invalid file name. Please try again.");
				badinput = true;
				}
			} while(badinput);
			machine = new FSA(thefilename);	// To make sure the machine is initialized
			
			badinput = true;	// Re-initialize the boolean for the next loop
			do
			{
				System.out.print("Do you want to display this machine? (y/n): ");
				choice = input.nextLine();
				if (choice.equalsIgnoreCase("y"))	// Display machine
				{
					badinput = false;
					machine.displayMachine();
				}
				else if (choice.equalsIgnoreCase("n"))	// Do not display machine
					badinput = false;
				else	// Invalid input
				{
					System.out.println("You entered an invalid choice. Please try again.");
					badinput = true;
				}
			} while (badinput);
			
			
			badinput = true;	// Re-initialize the booleans for the next loop
			repeat = true;
			do
			{
				System.out.print("\nDo you want to process a tape? (y/n): ");
				choice = input.nextLine();
				if (choice.equalsIgnoreCase("y"))	// Process the tape
				{
					badinput = false;
					System.out.print("Enter the tape from the keyboard--> ");
					thetape = input.nextLine();
					System.out.println();
					machine.processTape(thetape);
					repeat = true;
				}
				else if (choice.equalsIgnoreCase("n"))	// No processing
				{
					badinput = false;
					repeat = false;
				}
				else	// Invalid input
				{
					System.out.println("You entered an invalid choice. Please try again.");
					badinput = true;
					repeat = true;
				}
			} while (badinput || repeat);
			
			badinput = true;	// Initialize the boolean for the next loop
			do
			{
				System.out.print("Do you want to run a finite-state machine? (y or n)--> ");
				choice = input.nextLine();
				if (choice.equalsIgnoreCase("y"))	// Run the finite-state machine
				{
					repeat = true;
					badinput = false;
				}
				else if (choice.equalsIgnoreCase("n"))	// Do not run machine
				{
					repeat = false;
					badinput = false;
				}
				else	// Invalid input
				{
					System.out.println("You entered an invalid choice. Please try again.");
					badinput = true;
				}
			} while (badinput);
		}
		
	}
}