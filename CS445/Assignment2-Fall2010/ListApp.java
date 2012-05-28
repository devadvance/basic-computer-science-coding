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
Assignment 2
*/

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class ListApp
{
	public static void main (String[] args) throws FileNotFoundException
	{
		String fileName;
		String tempstring;
		ArrayList<String> dictionary = new ArrayList<String>();
		int indict = 0;
		int testfilecounter = 0;
		
		System.out.println("Experiment: Dictionary is stored in a list.\n");
		
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the name of the dictionary file: ");
		fileName = input.nextLine();
		
		
		File dictfile = new File(fileName);
		Scanner dictinput = new Scanner(dictfile);
		
		while(dictinput.hasNext())
		{
				dictionary.add(dictinput.next());
				
		}
		
		
		System.out.println("Size of the dictionary: " + dictionary.size());
		
		System.out.print("Enter the name of the test file: ");
		fileName = input.nextLine();
		File testfile = new File(fileName);
		Scanner testinput = new Scanner(testfile);
		
		StopWatch clock = new StopWatch();
		
		System.out.println("\nSearching for words in the file: " + fileName + "\n");
		
        clock.start();
		
		while(testinput.hasNext())
		{
			tempstring = testinput.next();
			if (dictionary.contains(tempstring.toLowerCase()))
				indict++;
			testfilecounter++;
			
			
		}
		
		
		clock.stop();
		
		System.out.println("Number of tokens: " + testfilecounter);
		System.out.println("Number of tokens in the dictionary: " + indict);
		
        System.out.println("(Clock: " + clock.getTimeSec() + "sec)");
		
		
	}
}