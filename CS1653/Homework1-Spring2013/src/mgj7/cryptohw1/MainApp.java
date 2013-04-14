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

import java.util.Scanner;

public class MainApp {
	public static void main (String[] args) {
		Scanner input = new Scanner(System.in);
		int menuChoice = 0;
		
		System.out.println("Welcome to the BouncyCastle test application (aka HW1)!" +
				"\nWhich test would you like to do? Enter the number and press ENTER." +
				"\n1 - AES Test" +
				"\n2 - RSA Test" +
				"\n3 - RSA Test without signature checking" +
				"\n4 - Timed comparison test" +
				"\n5 - Quit");
		
		menuChoice = input.nextInt();
		
		switch(menuChoice) {
		case 1:
			new AESTest(null);
			break;
		case 2:
			new RSATest(null, true);
			break;
		case 3:
			new RSATest(null, false);
			break;
		case 4:
			new CompareTest();
			break;
		case 5:
		default:
			System.out.println("Goodbye!");
			break;
		}
		
		input.close();
	}
}