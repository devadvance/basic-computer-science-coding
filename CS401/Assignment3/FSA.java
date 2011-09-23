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

/** ********************************* FSA.java ************************************************
***	This is the Finite-State Machine class file, which allows the creation of FSM objects	***
***	in other programs. This class is designed to be used with a driver of some sort, and	***
***	is unable to run by itself. To test this program, either run the Driver file to allow	***
***	full interaction, or run the demoDriver file to demo its capabilities.					***
***********************************************************************************************/

import java.util.Scanner; // Required to read text from the data files
import java.io.*; // Required for input-output operations and exceptions

public class FSA
{
	private String machineName;			//name of machine
	private int numStates;				//number of states
	private String alphabet;			//a list of distinct symbols
	private int[][] transitions;		//numStates-by-length_of_alphabet 			NOTE THIS WAS CHANGED FROM INT TO STRING ARRAY!!!!
	private String tape;				//a string over the alphabet
	private int start;					//start-state
	private boolean[] acceptStates;		//set of accept-states
	private boolean firsttime = true;	//first time through
	
	
	//Use a Scanner to open the file whose name is fileName.
	//Input the FSA from this file
	public FSA(String fileName) throws FileNotFoundException
	{
		File infile = new File(fileName);
		Scanner inputFile = new Scanner(infile);
		
		machineName = inputFile.nextLine();
		numStates = Integer.parseInt(inputFile.nextLine());
		start = Integer.parseInt(inputFile.nextLine());
		alphabet = inputFile.nextLine();
		
		transitions = new int[numStates][alphabet.length()];
		
		acceptStates = new boolean[numStates];
		for(int i = 0; i <= (numStates - 1); i++)
			acceptStates[i] = false;
		
		
		int row;
		int column;
		int state;
		
		while(true)
		{
			row = inputFile.nextInt();
			if (row == -1)
				break;
			column = alphabet.indexOf(inputFile.next());
			transitions[row][column] = inputFile.nextInt();
			
		}
		
		while(true)
		{
			state = inputFile.nextInt();
			if (state == -1)
				break;
			acceptStates[state] = true;
		}
		
	}
	
	
	//Display the FSM as show on the next page
	public void displayMachine()
	{
		
		
		System.out.println("\nMachine Name: " + machineName);
		
		System.out.print("Set of states: {");
		for(int i=0;i<numStates - 1;i++)
			System.out.print(i + ", ");
		System.out.println((numStates - 1) + "}");
		
		System.out.println("Start state: " + start);
		
		System.out.print("Alphabet: {");
		for(int i=0;i<(alphabet.length() - 1);i++)
			System.out.print((alphabet.charAt(i)) + ", ");
		System.out.println((alphabet.charAt(alphabet.length() - 1)) + "}");
		
		System.out.print("Accept States: {");
		for(int i=0;i<=(acceptStates.length - 1);i++)
		{
			if (acceptStates[i] == true && (firsttime = true))
				System.out.print(i);
			if (acceptStates[i] == true && (firsttime = false))
				System.out.print(" ," + i);
		}
		System.out.println("}\n");
		
		for(int i=0;i<=(numStates - 1);i++)
		{
			for(int k=0;k<=(alphabet.length() - 1);k++)
			{
				System.out.println("(" + (i) + ", " + (alphabet.charAt(k)) + ")->" + (transitions[i][k]));
			}
		}
	}
	
	//Return the machineName to caller
	public String name()
	{
		return machineName;
	}
	
	//Return number of states to caller
	public int numberOfStates()
	{
	return numStates;
	}
	
	//Return alphabet back to caller
	public String alphabet()
	{
	return alphabet;
	}
	
	//Return start state back to caller
	public int start()
	{
	return start;
	}
	
	//Return next state if in current state and see symbol
	public int nextState(int current, char symbol)
	{
		return (transitions[current][(alphabet.indexOf(symbol))]);
	}
	
	//Return true or false if k is or is or is not a final state.
	public boolean finalState(int k)
	{
	return (acceptStates[k]);
	}
	
	//Process tape, display transitions and determine if accept or reject tape
	public void processTape(String tape)
	{
		System.out.print("\n " + tape.charAt(0));
		
		for(int i=1;i<tape.length();i++)
		{
			System.out.print("  " + tape.charAt(i));
		}
		
		int currentstate = start;
		System.out.print("\n" + start);
		try
		{
			for(int i=0;i<tape.length();i++)
			{
				currentstate = (transitions[currentstate][alphabet.indexOf(tape.charAt(i))]);
				System.out.print("->" + currentstate);
			}
		}
		catch (Exception e)
			{
				System.out.println("\nAn error has occurred. Possibly a tape with invalid symbols was entered.");
			}
		
		if (finalState(currentstate))
			System.out.println("\nACCEPT TAPE");
		else
			System.out.println("\nREJECT TAPE");		
		
	}
}
