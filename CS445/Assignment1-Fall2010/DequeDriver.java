/**
Copyright (c) 2012, Matt Joseph
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
CS445
Assignment 1
*/

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Scanner;
import java.io.IOException;
import java.io.EOFException;

public class DequeDriver
{
	public static void main (String[] args)
	{
		Scanner input = new Scanner(System.in);
		Deque<Character> deck = new Deque<Character>();
		String filename;
		char tempchar;
		DataInputStream thestream;
		
		System.out.println("This runs the Deque class to test it.\n\n");
		
		System.out.print("Enter the name of the data file: ");
		filename = input.nextLine();
		try
		{
			FileInputStream file = new FileInputStream(filename);
			thestream = new DataInputStream(file);

			System.out.print("Deleted characters: ");
		
			while(true)
			{
				tempchar = thestream.readChar();
				if (tempchar == '\b')
					System.out.print(deck.removeBack());
				else
					deck.addToBack(tempchar);
			}
			
		}
		catch (EOFException e)
		{
			System.out.println("\nReached the end of the input file.\n");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		System.out.println("The message contained in the data file is as follows:\n");
		
		while (deck.getFront() != null)
		{
			tempchar = deck.removeFront();
			if (tempchar == '$' || tempchar == '\u0003')
				System.out.println("");
			else
				System.out.print(tempchar);
		}
	}
}
