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
Assignment 1
*/

/** *********************** a1problem1.java ***************************************************
*** This program computes the first solution to the equation a^5 + b^5 + c^5 + d^5 = e^5	***
*** and then prints the solution to a file. Since the variables should not form duplicate	***
*** permutations, break states are used selectively within loops. The powers are computed	***
*** using the power5 method created for this program. Prints to solution.txt				***
*** NOTE: TO EXECUTE, SIMPLY RUN THE PROGRAM												***
***********************************************************************************************/

import java.io.*;           // Used by PrintWriter
public class a1problem1
{
	public static void main(String[] args) throws IOException
	{
		long a,b,c,d,e;
		FileWriter file = new FileWriter("solution.txt");
		PrintWriter solutionfile = new PrintWriter(file);
		
		
		for(e=1;e < Long.MAX_VALUE;e++) // outer loop, other loops are limited by this root loop
		{
			
			for(a=1;a<Long.MAX_VALUE;a++)
			{
				if (a==e) // prevents duplicate permutations
					break;
				for(b=1;b<Long.MAX_VALUE; b++)
				{
					if (b > a) // prevents duplicate permutations
						break;
					for(c=1;c<Long.MAX_VALUE;c++)
					{	
						if (c > b) // prevents duplicate permutations
							break;
						for(d=1;d<Long.MAX_VALUE;d++)
						{
							
							if (d > c) // prevents duplicate permutations
								break;
							// if solution is found
							if ((power5(a) + power5(b) + power5(c) + power5(d)) == (power5(e)))
							{
								System.out.println("e = " + e + " a = " + a + " b = " + b + " c = " + c + " d = " + d);
								solutionfile.println("e = " + e + " a = " + a + " b = " + b + " c = " + c + " d = " + d);
								solutionfile.close();
								System.exit(0); // terminates the program
							}
						}
					}
				}
				
			}		
		}
	}
	
	public static long power5(long number) // method to raise a long to the 5th power
	{
		return (number * number * number * number * number);
	}

	
}