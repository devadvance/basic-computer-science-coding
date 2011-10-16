/*
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
CS449
Project 2a
Modified: 2011-10-16 @ 02:15

Note: Tested successfully by compiling with the following argument:
gcc -O2 -o mystrings mystrings.c
*/

#include <stdio.h>
#include <string.h>

static char *usageInfo[] = {
	"Usage Info:",
	"mystrings\n\tDisplays this usage info",
	"mystrings fileName\n\tDisplays all of the strings in the specified file with length of at least 4 characters",
	"mystrings -n min-len fileName\n\tDisplays all of the strings in the specified file with length of at least min-len",
	"\tNote: The default minlen is 4 if unspecified."
};

static int sizeUsageInfo = sizeof(usageInfo)/sizeof(char *);

static int usage() {
	int i;
	for (i = 0; i < sizeUsageInfo; i++)
		printf("%s\n", usageInfo[i]);
	return -1;
}

/* Function to actually find the strings in the file.
 * This function returns 0 if there were no problems, or 1 if there was an issue reading the file
 * Logic behind this function:
 * Rather than have a character/string buffer, a counter is used to see how many characters in a row were found in the file.
 * If the counter, called char_counter, reaches the requisite number of characters, the function seeks back in the file and
 * prints that number of characters, before continuing to check the next one. This way, no buffer is needed. The performance
 * penalty is the same for doing it this way as a buffer. Both would require 2 total passes for at least some of the characters.
 */
int findStrings(char *path, int minlen) {
	FILE *inputfile;
	int char_counter; // This will count up to the correct number of requisite characters before printing out
	char current_char; // This is be the current character from the file.
	char prev_char; // Used for storing any previous characters.
	int seek_value;
	
	// seek_value is the negative of the minlen minus 1
	seek_value = 0 - minlen - 1;
	
	// Prints the filename entered by the user.
	printf("\nFilename: %s\n\n",path);
	
	// Open the file to read.
	inputfile = fopen(path,"rb");
	
	// Return 1 if the file was empty or somehow otherwise bad.
	if (inputfile == NULL) {
		return(1);
	}
	
	// Loops while it is not the end of the input file
	while(!feof(inputfile)) {
		current_char = fgetc(inputfile); // Get the character from the input file (aka a byte of data)
		
		if (char_counter > minlen) {
			if ((current_char >= 32) && (current_char <= 126)) {
				char_counter++;
				printf("%c", current_char);
			}
			else {
				char_counter = 0; // Reset char_counter
				printf("\n"); // End of current string
			}
		}
		else if (char_counter == minlen) {
			fseek(inputfile,seek_value,SEEK_CUR); // Go minlen characters back in order to print the previous minlen number of characters
			for (char_counter = 0; char_counter < minlen; char_counter++) { // Print out the previous minlen number of characters
				prev_char = fgetc(inputfile);
				printf("%c", prev_char);
			}
			
			if ((current_char >= 32) && (current_char <= 126)) { // Now check the current character
				char_counter++;
			}
			else {
				char_counter = 0; // Reset char_counter
				printf("\n"); // End of current string
			}
			
		}
		else {
			if ((current_char >= 32) && (current_char <= 126)) { // Check the current character only
			char_counter++;
			}
			else {
				char_counter = 0; // Reset char_counter
			}
			
		}
		
	}
	// Close the inputfile
	fclose(inputfile);
	
	// Return 0 if everything went fine.
	return 0;
}

/* Main function */
int main (int argc, char *argv[]) {
	int temp_return;
	
	// Initially set temp_return equal to 0.
	temp_return = 0;
	
	if (argc == 1) { // If the user does not enter any arguments.
		temp_return = usage();
	}
	else if (argc == 2) { // If the user only enters a filename.
		temp_return = findStrings(argv[1], 4);
	}
	else if ((argc % 2) == 1) { // If the user enters an invalid number of arguments. 2 arguments for program name and filename, and then 2 more if a switch is used.
		printf("Invalid number of arguments!\n");
	}
	else if (((strcmp(argv[1],"-n")) == 0) && ( argc == 4)) { // If the minlen switch was used to specify a minlen.
		temp_return = findStrings(argv[3], atoi(argv[2]));
	}
	
	// If a bad filename was provided by the user.
	if (temp_return == 1) {
		printf("Bad file!\n");
	}
	
	return temp_return;
}
