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

/** *********************** a1problem2.java ***************************************************
*** This program computes a experimental value for pi using trials. It uses the value of	***
*** the number of times the dart lands in the circle divided by the total number of times	***
*** to give an estimate. It is then displayed using the printf method to 10 decimal places	***
*** NOTE: TO EXECUTE, USE "java a1problem2 n", where n is an integer number in command line	***
***********************************************************************************************/

import java.util.Random; // required to generate random numbers

public class a1problem2
{
	public static void main(String[] args)
	{
		int n = Integer.parseInt(args[0]); // turns the command-line input into an integer
		double x,y,distance,incircle=0;
		Random generator = new Random(); // random generator

		for (int i=1;i<=n;i++) // loop based on command-line input
		{
			
			x = (-1.0d + (generator.nextDouble() * 2.0d)); // x value
			y = (-1.0d + (generator.nextDouble() * 2.0d)); // y value
			distance = Math.sqrt(x * x + y * y);
			
			if (distance <= 1.0d) // determine whether or not the point is inside the circle
				incircle = incircle + 1.0d;
			
		}
		double pi = ((incircle / n) * 4.0d); // calculate pi
		System.out.println("N = " + n);
		System.out.printf("pi = %#.10f",pi);
	}
}