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
CS1501
Assignment 3
*/

import java.util.*;
import java.io.*;

public class Encode
{
	public static int[] ascii = new int[256];
	
	public static void main (String[] args)
	{
		try
		{
			File inputfilename = new File(args[0]);
			FileReader reader = new FileReader(inputfilename);
			
			File outputfilename = new File(args[1]);
			FileWriter output = new FileWriter(outputfilename);
			
			int tempint;
			
			// Create a ASCII character array.
			for (int i = 0; i <256;i++)
			{
				ascii[i] = i;
			}
			
			// Read the next int into a temporary integer variable.
			tempint = reader.read();
			while(tempint != -1)
			{
				output.write(moveToFront(tempint) + " ");
				tempint = reader.read();
			}
			
			output.close();
			reader.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("You entered an invalid file name!");
		}
		catch (IOException e)
		{
			System.out.println("An IOException occured...uh oh.");
		}
	}
	
	public static int moveToFront(int charToMove)
	{
		int temporaryA;
		int temporaryB;
		int x = 0;
		
		temporaryA = ascii[0];
		// This loop only happens if the character is not already in the first position!
		while(temporaryA != charToMove)
		{
				temporaryB = ascii[x + 1];
				ascii[x + 1] = temporaryA;
				temporaryA = temporaryB;
				x++;
		}
		ascii[0] = charToMove;
		
		return x;
	}
}