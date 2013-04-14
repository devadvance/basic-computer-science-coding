/*
Copyright (c) 2013, Matt Joseph
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
CS1653
Homework 1
Spring Semester 2013
*/

package mgj7.cryptohw1;

import java.util.Random;
import java.util.Scanner;

public class CompareTest {

	public CompareTest() {
		Scanner input = new Scanner(System.in);
		int lengthChoice = 0;
		int numberChoice = 0;
		int newKeyChoice = 0;
		boolean newKey = false;
		byte[][] randomStrings = null;
		
		System.out.println("" +
				"\nThis will compare the time it takes to encrypt a number" +
				"\n of randomly generated strings in both AES and RSA." +
				"\nYou will need to enter the length of the strings as " +
				"\nwell as how many you want to generate.");
		
		System.out.print("Enter the length of the random strings: ");
		lengthChoice = input.nextInt();
		
		System.out.print("Enter the number of random strings: ");
		numberChoice = input.nextInt();
		
		System.out.println("Generating the random strings...");
		randomStrings = generateArray(numberChoice, lengthChoice);
		System.out.print("Done generating the random strings." +
				"\nEnter 1 if you want to create a new key each round, or 0 if you do not: ");
		
		newKeyChoice = input.nextInt();
		
		if (newKeyChoice == 1) {
			newKey = true;
		}
		
		StopWatch sw = new StopWatch("Encryption Test StopWatch");
		
		System.out.println("Starting the AES Timed Test");
		sw.start("AES Timed Test");
		new AESTest(newKey, randomStrings);
		sw.stop();
		
		System.out.println("Starting the RSA Timed Test");
		sw.start("RSA Timed Test");
		new RSATest(newKey, randomStrings);
		sw.stop();
		
		System.out.println(sw.prettyPrint() +
				"\n\nEnd of comparison test.");
		
		input.close();
	}
	
	private byte[][] generateArray(final int number, final int length) {
		byte[][] returnArray = new byte[number][];
		
		// Loop through and generate each array of specified length
		for (int i = 0; i < number; i++) {
			returnArray[i] = generateByteArray(length);
		}
		
		return returnArray;
	}
	
	private byte[] generateByteArray(final int length) {
	    Random rand = new Random();
	    StringBuilder builder = new StringBuilder();
	    for(int i = 0; i < length; i++) {
	    	// Limited to 127 just to avoid the chars beyond ASCII
	        char c = (char)(rand.nextInt((int)(127)));
	        builder.append(c);
	    }
	    return builder.toString().getBytes();
	}
}
