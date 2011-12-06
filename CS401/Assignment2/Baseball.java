/**
Copyright (c) 2011, Matt Joseph
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
Assignment 2
*/

/** ************************* Baseball.java ***************************************************
*** This program uses the BallPlayer class in combination with an input file to sort the	***
*** data in a proper table format. It uses the built in methods in the BallPlayer class as	***
*** well as the initial constructor. It also finds the best player based on slugging.		***
*** NOTE: USES "list.txt" AS THE DEMO LIST!!!												***
***********************************************************************************************/

import java.io.*;           // Used by PrintWriter
import java.util.Scanner; // required to get name of file
import java.util.ArrayList; // required to create ArrayLists

public class Baseball
{
	public static void main(String[] args) throws FileNotFoundException
	{
		int myyear,maxplayernum;
		String myfirstname,mylastname;
		ArrayList<BallPlayer> players = new ArrayList<BallPlayer>();  // ArrayList to store all BallPlayer objects
		
		// Scanner for getting the input file name (uses list.txt for demo!!!)
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the data file name: ");
		String filename = input.nextLine();
		
		// Setup to read from file
		File infile = new File(filename);
		Scanner inputFile = new Scanner(infile);
		
		// Add some space for nice formatting
		System.out.print("\n\n");
		
		// Loops to go through all of the players on the input list
		while (inputFile.hasNext())
		{
			myfirstname = inputFile.next();
			mylastname = inputFile.next();
			myyear = Integer.parseInt(inputFile.next());
			players.add(new BallPlayer(myfirstname,mylastname,myyear));
			
			BallPlayer currentplayer = players.get(players.size() - 1); // Create a reference to the BallPlayer that has just been added to the ArrayList for simpler code
			
			currentplayer.setHits(Integer.parseInt(inputFile.next())); // Setting hits
			currentplayer.setDoubles(Integer.parseInt(inputFile.next())); // Setting doubles
			currentplayer.setTriples(Integer.parseInt(inputFile.next())); // Setting triples
			currentplayer.setHomeRuns(Integer.parseInt(inputFile.next())); // Setting homeRuns
			currentplayer.setSingles((currentplayer.hits() - (currentplayer.doubles() + currentplayer.triples() + currentplayer.homeRuns()))); // Setting singles based on hits, doubles, triples, and homeruns
			currentplayer.setAtBats(Integer.parseInt(inputFile.next())); // Setting atBats (not required but simplifies program)
			
		}
		inputFile.close(); // Closes file
		
		(players.get(0)).heading();
		for (int i = 1;i <= players.size();i++)
		{
			(players.get(i - 1)).display();
		}
		
		// Find best player
		maxplayernum = 0;
		
		for (int i = 1;i <= players.size();i++)
		{
			if ((players.get(maxplayernum)).compareTo((players.get(i - 1))) == -1) // Using the Comparable method in the BallPlayer object
				maxplayernum = (i - 1);
			
		}
		// Print the best player
		System.out.printf("\nMax slugger is %s in %4d with slugging percentage %4.3f\n\n",(players.get(maxplayernum)).printableName(),(players.get(maxplayernum)).year(),(players.get(maxplayernum)).sluggingPercentage());
		
		
	}
}