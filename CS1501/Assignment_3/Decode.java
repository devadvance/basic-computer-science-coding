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
CS1501
Assignment 3
*/

import java.util.*;
import java.io.*;

public class Decode
{
	public static int[] ascii = new int[256];
	
	public static void main (String[] args)
	{
		try
		{
			File filename = new File(args[0]);
			Scanner reader = new Scanner(filename);
			
			int tempint;
			String output = "";
			
			// Create a ASCII character array.
			for (int i = 0; i <256;i++)
			{
				ascii[i] = i;
			}
			
			// Read the next int into a temporary integer variable.
			while(reader.hasNext())
			{
				tempint = reader.nextInt();
				output += (char)moveToFront(tempint);
			}
			
			System.out.println(output);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("You entered an invalid file name!");
		}
	}
	
	public static int moveToFront(int positionOfChar)
	{
		int temporaryA = 0;
		int temporaryB = ascii[positionOfChar];
		
		for (int i = positionOfChar;i > 0;i--)
		{
			temporaryA = ascii[i];
			ascii[i] = ascii[i - 1];
		}
		
		ascii[0] = temporaryB;
		return temporaryB;
	}
}