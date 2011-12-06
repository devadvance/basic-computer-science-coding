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
Assignment 1
*/

/** *********************** RandomWalker.java *************************************************
*** This program reads a starting point on the 2D plane from a file, as well as a number of	***
*** steps to take. Each step is in a random direction dependent on the random value given	***
*** the random number generator. Steps and final distance squared are written to a file.	***
*** NOTE: THE SAMPLE FILE FROM THE ASSIGNMENT IS CALLED "input.txt"!!! Enter this!			***
***********************************************************************************************/

import java.util.Random; // required to generate random direction
import java.util.Scanner; // required to get name of file
import java.io.*;

public class RandomWalker
{
	public static void main(String[] args) throws IOException
	{
		// Variable declaration
		String directionname = new String();
		int i,x,y,direction,steps;
		int walknumber = 0;

		// Scanner for getting the input file name
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the input file name: ");
		String filename = input.nextLine();
			
		Random generator = new Random(); // random generator
		
		// Opens the specified file for input
		File infile = new File(filename);  
		Scanner inputFile = new Scanner(infile);
		
		while (inputFile.hasNext())
		{
			walknumber++;
			
			x = inputFile.nextInt();
			y = inputFile.nextInt();
			steps = inputFile.nextInt();
			
			// Opens the specified file for output, with a name depending on the walknumber
			FileWriter file_p = new FileWriter("walk_" + walknumber + ".txt");
			PrintWriter walkfile_p = new PrintWriter(file_p);
			
			walkfile_p.println("Start at (" + x + "," + y + ") take " + steps + " steps");
			
			// Loop for each step
			for(i=1;i<=steps;i++)
			{
				
				// Directions represented by a random integer between 0 and 3 inclusive
				direction = generator.nextInt(4);
				if (direction == 0)
				{
					directionname = "North";
					y = (y + 1);
				}
				if (direction == 1)
				{
					directionname = "East";
					x = (x + 1);
				}
				if (direction == 2)
				{
					directionname = "South";
					y = (y - 1);
				}
				if (direction == 3)
				{
					directionname = "West";
					x = (x - 1);
				}
				
				walkfile_p.println("Step " + directionname + " to (" + x + "," + y + ")");
			}
			
			walkfile_p.println("distance squared from start is " + (x * x + y * y));
			walkfile_p.close(); // Closes file
		}
		inputFile.close(); // Closes file
	}
}