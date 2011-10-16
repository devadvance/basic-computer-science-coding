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
Project 1b
Modified: 2011-09-23 @ 00:55

Note: Tested successfully by compiling with the following argument:
gcc -O2 -o id3tagEd_simplified id3tagEd_simplified.c

This version of the program uses no functions and no pointers to structs. This reduces the code to a more basic program, though with more repeated code.
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// Main function.
int main (int argc, char *argv[]) {
	/*  ID3v1.1 standard information:
		Offset	length	description
		0		3		"TAG" identifier string
		3		30		Song title string
		33		30		Artist string
		63		30		Album string
		93		4		Year string
		97		28		Comment string
		125		1		A one-byte separator (always 0)
		126		1		Track number byte
		127		1		Genre identifier byte
	*/
	
	struct id3tag {
		char identifier[3];
		char title[30];
		char artist[30];
		char album[30];
		char year[4];
		char comment[28];
		char separator;
		unsigned char tracknumber;
		unsigned char genre;
	};
	
	// Variable declaration.
	char *file_location;
	struct id3tag the_tag;
	int counter;
	FILE *musicfile;
	FILE *write_file;
	
	if (argc == 1) { // If the user does not enter any arguments. WILL CHANGE PROGRAMNAME LATER.
		printf("You entered no filename!\nCorrect usage:\nPROGRAMNAME FILE_NAME.EXT\n\tDisplay current ID3v1.1 data in a file\nPROGRAMNAME FILE_NAME.EXT -SWITCH NEW_DATA ...\n\tEither changes the existing ID3v1.1 data, or creates a new tag with the specified data.\n");
		printf("\nAvailable switches:");
		printf("\n-TITLE \"TITLE_DATA\"\t-\tSets the title to the specified data");
		printf("\n-ARTIST \"ARTIST_DATA\"\t-\tSets the artist to the specified data");
		printf("\n-ALBUM \"ALBUM_DATA\"\t-\tSets the album to the specified data");
		printf("\n-YEAR \"YEAR_DATA\"\t-\tSets the year to the specified data");
		printf("\n-COMMENT \"COMMENT_DATA\"\t-\tSets the comment to the specified data");
		printf("\n-TRACK \"TRACK_NUMBER\"\t-\tSets the track to the specified number\n\n");
	}
	else if (argc == 2) { // If the user only enters a filename, just read out the tag.
	
		// Prints the filename entered by the user.
		printf("Filename: %s\n",argv[1]);
		
		// Open the file to read.
		musicfile = fopen(argv[1],"rb");
		
		// Return 1 if the file was empty or somehow otherwise bad.
		if (musicfile == NULL)
			printf("You entered an invalid filename! :(\n");
		else { // If the file is generally fine and not invalid/empty, continue.
			// Go to 128 bytes from the end of the file, where the tag data should be if it exists.
			fseek(musicfile,-128L,SEEK_END);

			// Read the last 128 bytes of the file into the tag struct. Note that the tag struct is simply treated as a location in memory.
			fread(&the_tag,1,128,musicfile);
			fclose(musicfile);
			
			if (strncmp(the_tag.identifier,"TAG",3)==0) // If the tag contains the identifier, then it has a tag, and it can be printed.
			{
				printf("\nIdentifier: %-3.3s\n",the_tag.identifier);
				printf("Title: %-30.30s\n",the_tag.title);
				printf("Artist: %-30.30s\n",the_tag.artist);
				printf("Album: %-30.30s\n",the_tag.album);
				printf("Year: %-4.4s\n",the_tag.year);
				printf("Comment: %-28.28s\n",the_tag.comment);
				printf("Track number: %d\n",the_tag.tracknumber);
				printf("Genre: %d\n\n",the_tag.genre);		
			}
			else { // Tag did not have the identifier. That is to say, the location in memory did not containt TAG
				printf("There was no ID3v1.1 tag in the specified file :(\n");
			}
		}
		
	}
	else if ((argc % 2) == 1) // If the user enters an invalid number of arguments. 2 arguments for program name and filename, and then 2 more each for switches. If it is an odd number, they're missing something.
		printf("Invalid number of arguments!\n");
	else if ((argc % 2) == 0) { // If the user enteres a correct number of arguments beyond just the program name and filename, then they want to write some tag data.
		// Prints the filename entered by the user.
		printf("Filename: %s\n",argv[1]);
		
		// Print the current ID3v1.1 tag, if there is one.
		printf("Current ID3v1.1 data:\n");
		
		// Open the file to read.
		musicfile = fopen(argv[1],"rb");
		
		// Return 1 if the file was empty or somehow otherwise bad.
		if (musicfile == NULL)
			printf("You entered an invalid filename! :(\n");
		else { // If the file is generally fine and not invalid/empty, continue.
			// Go to 128 bytes from the end of the file, where the tag data should be if it exists.
			fseek(musicfile,-128L,SEEK_END);

			// Read the last 128 bytes of the file into the tag struct. Note that the tag struct is simply treated as a location in memory.
			fread(&the_tag,1,128,musicfile);
			fclose(musicfile);
			
			if (strncmp(the_tag.identifier,"TAG",3)==0) { // If the tag contains the identifier, then it has a tag, and it can be printed.
				// Loop to check to see if there are tags to be set. If the user enters an incorrect switch, such as -FOOBAR, that switch and the data that follows it is simply ignored.
				for (counter = 2;counter < argc;counter++) {
					if ((strcmp(argv[counter],"-TITLE")) == 0)
						strncpy(the_tag.title,argv[++counter],30);
					else if ((strcmp(argv[counter],"-ARTIST")) == 0)
						strncpy(the_tag.artist,argv[++counter],30);
					else if ((strcmp(argv[counter],"-ALBUM")) == 0)
						strncpy(the_tag.album,argv[++counter],30);
					else if ((strcmp(argv[counter],"-YEAR")) == 0)
						strncpy(the_tag.year,argv[++counter],4);
					else if ((strcmp(argv[counter],"-COMMENT")) == 0)
						strncpy(the_tag.comment,argv[++counter],28);
					else if ((strcmp(argv[counter],"-TRACK")) == 0)
						the_tag.tracknumber = atoi(argv[++counter]);
				}
				
				// Open file for reading and writing.
				write_file = fopen(argv[1],"r+b");

				// Verify it is not an empty file. Perhaps redundant, but not harmful.
				if (write_file == NULL)
					return(1);
				
				// Seek to the position of the existing tag in the file.
				fseek(write_file,-128L,SEEK_END);
				
				// Write the tag to the file in one go. Writes as a byte stream. Then closes the file.
				fwrite(&the_tag,1,128,write_file);
				fclose(write_file);
						
			}
			else { // Tag did not have the identifier. That is to say, the location in memory did not containt TAG
				printf("Current ID3v1.1 tag:\n");
				printf("The file did not have an existing ID3v1.1 tag.\n");
				
				// Set tag struct to all null bytes initially. That way if some tag is not set, then it is just full of null.
				// There is probably a better way to do this with memset or something like that. We haven't learned that yet...
				strncpy(the_tag.identifier,"TAG",3);
				strncpy(the_tag.title,"",30);
				strncpy(the_tag.artist,"",30);
				strncpy(the_tag.album,"",30);
				strncpy(the_tag.year,"",4);
				strncpy(the_tag.comment,"",28);
				the_tag.separator = '\0';
				the_tag.tracknumber = atoi("");
				the_tag.genre = atoi("");
				
				// Loop to check to see if there are tags to be set. If the user enters an incorrect switch, such as -FOOBAR, that switch and the data that follows it is simply ignored.
				for (counter = 2;counter < argc;counter++) {
					if ((strcmp(argv[counter],"-TITLE")) == 0)
						strncpy(the_tag.title,argv[++counter],30);
					else if ((strcmp(argv[counter],"-ARTIST")) == 0)
						strncpy(the_tag.artist,argv[++counter],30);
					else if ((strcmp(argv[counter],"-ALBUM")) == 0)
						strncpy(the_tag.album,argv[++counter],30);
					else if ((strcmp(argv[counter],"-YEAR")) == 0)
						strncpy(the_tag.year,argv[++counter],4);
					else if ((strcmp(argv[counter],"-COMMENT")) == 0)
						strncpy(the_tag.comment,argv[++counter],28);
					else if ((strcmp(argv[counter],"-TRACK")) == 0)
						the_tag.tracknumber = atoi(argv[++counter]);
				}
				
				// Open file for apending ONLY.
				write_file = fopen(argv[1],"ab");

				// Verify it is not an empty file. Perhaps redundant, but not harmful.
				if (write_file == NULL)
					return(1);
				
				// Write the tag to the end of the file in one go. Writes as a byte stream. Then closes the file.
				fwrite(&the_tag,1,128,write_file);
				fclose(write_file);
			}
			
			// Print new ID3v1.1 data, regardless of whether it was a brand new tag or just edited.
			printf("\nNew ID3v1.1 data:\n");
			printf("\nIdentifier: %-3.3s\n",the_tag.identifier);
			printf("Title: %-30.30s\n",the_tag.title);
			printf("Artist: %-30.30s\n",the_tag.artist);
			printf("Album: %-30.30s\n",the_tag.album);
			printf("Year: %-4.4s\n",the_tag.year);
			printf("Comment: %-28.28s\n",the_tag.comment);
			printf("Track number: %d\n",the_tag.tracknumber);
			printf("Genre: %d\n\n",the_tag.genre);
		}
	}
	else {
		printf("\nSomething bad happened? :O \n");
		return 1;
	}
	
	// Returns 0 if nothing went wrong with program execution.
	return 0;
}
