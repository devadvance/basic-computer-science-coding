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
Assignment 3
*/

import java.io.*;

public class demoDriver
{
	public static void main(String[] args) throws FileNotFoundException
	{
		FSA machine = new FSA("divisibleby3.dat");
		
		System.out.printf("name: %-20s", machine.name());					//display machine's name
		System.out.printf("alpha: %-20s%n", machine.alphabet());			//display alphabet
		System.out.printf("nodes:%-20d", machine.numberOfStates());			//number of states
		System.out.printf("starting node: %-20d%n%n", machine.start());		//start node
		
		int[] currentnodes = {1,0,1,2,0,2}; //(node, symbol) pairs
		char[] symbols = {'2','3','0','1','2','3'};
		int node;
		char symbol;
		
		System.out.println("from   sym   to");
		System.out.println("node	  node");
		System.out.println("----------------");
		for(int k=0; k<symbols.length; k++){    //make sure transitions array is correct
			node = currentnodes[k];
			symbol = symbols[k];
			System.out.printf("%4d   %3c   %2d%n", node, symbol, 
			machine.nextState(node,symbol));
		}
		System.out.println("\n------------------------------------------------");
		
		machine.displayMachine();				//display FSM
		machine.processTape("112");				//process tape 1
		machine.processTape("1130121");			//process tape 2
   }
}
	