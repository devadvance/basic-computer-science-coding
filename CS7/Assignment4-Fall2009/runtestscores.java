/**
Copyright (c) 2009, Matt Joseph
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
CS7
Assignment 4
*/

/** *******************************
runtestscores - Matt Joseph
This program uses the TestScores class. The TestScores class
is in the package called tscores, which is a directory under the
directory which runtestscores is in
******************************** */

import java.util.Scanner;
import tscores.TestScores;

public class runtestscores
{
    public static void main(String[] args)
    {
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Please enter the number of test scores: ");
		TestScores classroom = new TestScores(scan.nextInt());
		
		for (int counter = 1; counter <= classroom.getnumscores(); counter++)
		{
			System.out.print("Please enter the score on for test " + counter + ": ");
			classroom.addScore(counter, scan.nextDouble());
		}
		
		System.out.printf("\nThe average score was %.2f%%",classroom.getAverage());
	}
}