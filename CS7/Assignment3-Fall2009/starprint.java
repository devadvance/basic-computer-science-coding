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
Assignment 3
*/

/** *******************************
starprint - Matt Joseph
This program prints star patterns according to the user's choice.
It uses a single printf statement to accomplish any pattern but E,
which uses two. Each pattern relies on for loops with several conditions,
and the overall program relies on a do-while loop. There are a total of
eight (8) print statements within the entire program.
******************************** */
import java.util.Scanner;

public class starprint
{	
	public static void main (String[] args)
	{
		int l,c,z;
		String space,s,n,choice,inputS;
		boolean repeat = true;
		Scanner scan = new Scanner(System.in);
		
		do
		{
			System.out.print("Enter pattern type A-E (enter Q to quit): "); // print statement 1
			choice = scan.nextLine();
			
			if (choice.equalsIgnoreCase("A")) // This is for the A pattern
			{
				for (l=0; l <=7; l++)
				{
					for (c=(l + 1); c > 0; c--)
					{
						if (c > 1)
							n = "";
						else
							n = "\n";
						System.out.printf("*%s",n); // print statement 2
					}
				}
			}
			
			if (choice.equalsIgnoreCase("B")) // This is for the B pattern
			{
				for (l=8; l >=1; l--)
				{
					for (c=l; c > 0; c--)
					{
						if (c > 1)
							n = "";
						else
							n = "\n";
						System.out.printf("*%s",n); // print statement 3
					}
				}
			}
			
			if (choice.equalsIgnoreCase("C")) // This is for the C pattern
			{
				s = "";
				for (l=8; l >=1; l--)
				{
					for (c=l; c > 0; c--)
					{
						if (c==l && l < 8)
							space = s;
						else
							space = "";
						if (c > 1)
							n = "";
						else
							n = "\n";
						System.out.printf("%s*%s",space,n); // print statement 4
					}
					s += " ";
				}
			
			}
			
			if (choice.equalsIgnoreCase("D")) // This is for the D pattern
			{
				s = "       ";
				for (l=0; l <=7; l++)
				{
					for (c=(l + 1); c > 0; c--)
					{
						if (c > 1)
							n = "";
						else
							n = "\n";
						if (c == l + 1)
							space = s;
						else
							space = "";
						System.out.printf("%s*%s",space,n); // print statement 5
					}
					s = s.replaceFirst("\\s","");
				}
			}
			
			if (choice.equalsIgnoreCase("E")) // This is for the E pattern
			{
				int input = 0;
				char isnotInt = 'Y';
				char isnotOdd = 'Y';
				while (isnotInt == 'Y' || isnotOdd == 'Y')
				{
					System.out.print("Enter desired number of stars (odd number): "); // print statement 6
					inputS = scan.nextLine();
					try
					{
						input = Integer.parseInt(inputS);
						isnotInt = 'N'; //Did not throw
						if ((input % 2) == 1)
							isnotOdd = 'N';
						else
							isnotOdd = 'Y';
					}
					catch (Exception e)
					{
						isnotInt = 'Y'; //Threw
					}
				}
				
				// reference number of spaces
				z = (input / 2);
				s = "";
				
				// sets the actual starting amount of spaces
				while (s.length() < z)
				{
					s += " ";
				}
				
				// Loop 1 (first half)
				for (l=1; l <= z; l++)
				{
					// System.out.println("l is " + l);
					for (c=1; c <= l * 2 - 1; c++)
					{
						if (c == 1)
							space = s;
						else
							space = "";
						if (c == l * 2 - 1)
							n = "\n";
						else
							n = "";
						System.out.printf("%s*%s",space,n); // print statement 7
					}
					s = s.replaceFirst("\\s","");
				}
				
				// Loop 2 (second half)
				s = " ";
				for (l=z + 1; l <= input; l++)
				{
					for (c = 1; c <= (input - l) * 2 + 1; c++)
					{
						if (c == 1 && l > (z + 1))
							space = s;
						else
							space = "";
						if (c == (input - l) * 2 + 1)
							n = "\n";
						else
							n = "";
						System.out.printf("%s*%s",space,n); // print statement 8
					}
					if (l > z + 1)
						s = s.replaceFirst("\\s","  ");
				}
			}
			
			if (choice.equalsIgnoreCase("Q"))
				repeat = false;
			
		} while (repeat);
	}
}