/*
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
CS449
Project 4
Modified: 2011-11-27 @ 00:45

Note: Tested successfully by compiling with the following argument:
gcc -m32 -o pi_digits pi_digits.c

*/

#include <stdio.h>
#include <stdlib.h>

const char *USAGE_INFO[] = {
	"Usage Info:",
	"pi_digits\n\tDisplays this usage info",
	"pi_digits START END\n\tDisplays the digits of pi from START position to END position",
	"\t\tEX: pi_digits 0 3 produces 3141"
};

const int USAGE_INFO_SIZE = sizeof(USAGE_INFO)/sizeof(char *);

int usage() {
	int i;
	for (i = 0; i < USAGE_INFO_SIZE; i++)
		printf("%s\n", USAGE_INFO[i]);
	return -1;
}

/* Reads the arguments from the shell/command line.
 * Tells you if you entered less than 2 arguments, or more than 2.
 */
int main (int argc, char *argv[]) {
	int temp_return;
	int start;
	int end;
	int length;
	FILE *piData;
	
	// Initially set temp_return equal to 0.
	temp_return = 0;
	
	if ((argc <= 2) || (argc > 3)) {
		temp_return = usage();
	}
	else {
		start = atoi(argv[1]); // Set start to first argument
		end = atoi(argv[2]); // Set end to second argument
		
		if (start < 0) { // Make sure start is greater than 0
			printf("You entered an incorrect start value! The start value must NOT be negative!\n");
			return 1;
		}
		else if (end < 0) { // Make sure end is greater than 0
			printf("You entered an incorrect end value! The end value must NOT be negative!\n");
			return 1;
		}
		else if (start > end) { // Make sure start is less than or equal to end
			printf("You entered an incorrect start or end value. End must be GREATER than start!\n");
			return 1;
		}
		length = (end - start) + 1; // Set length accordingly
		char pi_value[length + 1]; // Allocate a character array
		
		piData = fopen("/dev/pi", "r"); // Open the pi char device
		
		// Return 1 if the pi devices was empty or somehow bad
		if (piData == NULL) {
			return 1;
		}
		
		// Seek to the correct position
		// Note, this is just ONE WAY of changing *ppos
		fseek(piData,start,SEEK_SET);
		fread(pi_value,1,length,piData); // Read the data. Uses the pi_read function
		fclose(piData); // Close the file
		
		pi_value[length] = '\0'; // Add terminating null character
		
		printf("%s\n", pi_value); // Print it on the screen
		
		temp_return = 0; // Success
	}
	
	return temp_return;
}
