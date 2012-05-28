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
Assignment 3
*/

import java.math.BigInteger;
import java.util.Scanner;

public class Egypt
{
	public static void main (String[] args)
	{
		OrderedLinkedList<BigInteger> fractlist = new OrderedLinkedList();
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome to the Egyptian Fractions program! Please follow the instructions.");
		
		System.out.print("Enter the numerator of the fraction: ");
		BigInteger numerator = BigInteger.valueOf(input.nextInt());
		
		System.out.print("Enter the denominator of the fraction: ");
		BigInteger denominator = BigInteger.valueOf(input.nextInt());
		
		for(int i = 1;BigInteger.valueOf(i).compareTo(numerator) <= 0;i++)
		{
			fractlist.add(denominator);
		}
		
		int duplicate = fractlist.firstDuplicate();
		BigInteger tempBigA;
		BigInteger tempBigB;
		
		while (duplicate != -1)
		{
			tempBigA = fractlist.getData(duplicate);
			fractlist.remove(duplicate);
			fractlist.add(tempBigA.add(BigInteger.ONE));
			tempBigB = tempBigA.add(BigInteger.ONE);
			fractlist.add(tempBigB.multiply(tempBigA));
			
			duplicate = fractlist.firstDuplicate();
			
		}
		System.out.println("This is the Ordered Linked List:");
		
		fractlist.display();
		
		//Begin the pretty printing part!
		
		System.out.println();
		
		BigInteger tempbig;
		BigInteger tempquot;
		int tempquotB;
		
		String currentNumer = "";
		String dashes = "";
		String currentDenom = denominator.toString();
		
		String finalNumer = "";
		String finalDashes = "";
		String finalDenom = "";
		
		//start initial
		
		tempquotB = currentDenom.length() / 2;
		
		//Pre-spacing
			for (int x = 0; x < tempquotB; x++)
			{
				currentNumer += " ";
			}
			
			//Numerator
				currentNumer += "3";
			
			//Post-spacing
			for (int y = tempquotB + 1; y < currentDenom.length(); y++)
			{
				currentNumer += " ";
			}
			
			//Dashes
			for (int z = 0; z < currentDenom.length(); z++)
			{
				dashes += "-";
			}
		
		currentNumer += "   ";
		dashes += " = ";
		currentDenom += "   ";
		
		finalNumer += currentNumer;
		finalDashes += dashes;
		finalDenom += currentDenom;
		
		//end initial
		
		for (int j = 1; j <= fractlist.getLength();j++)
		{
			currentNumer = "";
			dashes = "";
			currentDenom = "";
			
			//Add spacing to the beginning if it is not the first fraction
			if (j != 1)
			{
				currentNumer += "   ";
				dashes += " + ";
			}
			
			tempbig = fractlist.getData(j);
			currentDenom = tempbig.toString();
			
			tempquotB = currentDenom.length() / 2;
			
			//Pre-spacing
			for (int x = 0; x < tempquotB; x++)
			{
				currentNumer += " ";
			}
			
			//Numerator
				currentNumer += "1";
			
			//Post-spacing
			for (int y = tempquotB + 1; y < currentDenom.length(); y++)
			{
				currentNumer += " ";
			}
			
			//Dashes
			for (int z = 0; z < currentDenom.length(); z++)
			{
				dashes += "-";
			}
			
			currentDenom += "   ";
			
			finalNumer += currentNumer;
			finalDashes += dashes;
			finalDenom += currentDenom;
		}
		
		System.out.println(finalNumer);
		System.out.println(finalDashes);
		System.out.println(finalDenom);
	}
}