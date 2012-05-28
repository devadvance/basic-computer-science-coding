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
perfectnum - Matt Joseph
Uses method perfect to check if a number is a perfect number.
******************************** */

public class perfectnum
{
    public static void main(String[] args)
    {
		System.out.print("The perfect numbers between 1 and 1000 are: ");
		
		for (int number = 1; number <= 1000; number++)
		{
			if (perfect(number))
				System.out.print(number + " ");
		}
		
	}

	public static boolean perfect(int num)
	{
		boolean isperfect = false;
		int sum = 1;
		
		// Find factors and add to sum
		for (int divisor = 2; divisor < num; divisor++)
			{
				if ((num % divisor) == 0)
					sum += divisor;
			}
		
		if (num == sum && num != 1)
			isperfect = true;
		
		return isperfect;

	}
}