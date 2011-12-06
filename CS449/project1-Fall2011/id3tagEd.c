/*
Copyright (c) 2011, Matt Joseph (mgj7@pitt.edu)
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
Modified: 2011-09-24 @ 19:30

Note: Tested successfully by compiling with the following argument:
gcc -O2 -o id3tagEd id3tagEd.c
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// id3tag struct for use throughout this program.
struct id3tag {
		char identifier[3];
		char title[30];
		char artist[30];
		char album[30];
		char year[4];
		char comment[28];
		char separator;
		unsigned char track;
		unsigned char genre;
};

// Function prototypes
void print_tag(struct id3tag *tag_p);
int read_tag(char *path,struct id3tag *tag_p);
int write_tag(int argument_count, char **arguments,struct id3tag *tag_p);

// Simply prints, in a formatted manner, the contents of an id3tag struct.
void print_tag(struct id3tag *tag_p) {
	printf("Title: %-30.30s\n",tag_p->title);
	printf("Artist: %-30.30s\n",tag_p->artist);
	printf("Album: %-30.30s\n",tag_p->album);
	printf("Year: %-4.4s\n",tag_p->year);
	printf("Comment: %-28.28s\n",tag_p->comment);
	printf("Track number: %d\n",tag_p->track);
	printf("Genre: %d\n\n",tag_p->genre);
}

// This function simply reads the tags in a file. If there are none, it says so. If the file is incorrect, it returns 1, else returns 0.
int read_tag(char *path,struct id3tag *tag_p) {
	FILE *musicfile;
	
	// Prints the filename entered by the user.
	printf("\nFilename: %s\n",path);
	
	// Open the file to read.
	musicfile = fopen(path,"rb");
	
	// Return 1 if the file was empty or somehow otherwise bad.
	if (musicfile == NULL)
		return(1);
	
	// Go to 128 bytes from the end of the file, where the tag data should be if it exists.
	fseek(musicfile,-128L,SEEK_END);

	// Read the last 128 bytes of the file into the tag struct. Note that the tag struct is simply treated as a location in memory.
	fread(tag_p,1,128,musicfile);
	fclose(musicfile);
	
    if (strncmp(tag_p->identifier,"TAG",3)==0) // If the tag contains the identifier, then it has a tag, and it can be printed.
		print_tag(tag_p);
	else { // Tag did not have the identifier. That is to say, the location in memory did not containt TAG
		printf("There was no ID3v1.1 tag in the specified file.\n");
		return 2; // Return 2 means that it had no ID3v1.1 tag.
	}
	
	// Return 0 if everything went fine.
	return 0;
}

int write_tag(int argument_count, char **arguments,struct id3tag *tag_p) {
	int read_return; // Stores what is returned from the read_tag function.
	int counter;
	FILE *write_file; // FILE for writing. Same as the file for reading, but reopened specifically to append.
	
	// Print the original ID3v1.1 tag, if there is one.
	printf("\nOriginal ID3v1.1 tag:\n");
	read_return = read_tag(arguments[1],tag_p);
	
	// Return 1 if the file was empty or somehow otherwise bad.
	if (read_return == 1)
		return 1;
	else if (read_return == 0) { // If the file read correctly, and it had an existing ID3v1.1 tag.
		// Loop to check to see if there are tags to be set. If the user enters an incorrect switch, such as -FOOBAR, that switch and the data that follows it is simply ignored.
		for (counter = 2;counter < argument_count;counter++) {
			if ((strcmp(arguments[counter],"-TITLE")) == 0)
				strncpy(tag_p->title,arguments[++counter],30);
			else if ((strcmp(arguments[counter],"-ARTIST")) == 0)
				strncpy(tag_p->artist,arguments[++counter],30);
			else if ((strcmp(arguments[counter],"-ALBUM")) == 0)
				strncpy(tag_p->album,arguments[++counter],30);
			else if ((strcmp(arguments[counter],"-YEAR")) == 0)
				strncpy(tag_p->year,arguments[++counter],4);
			else if ((strcmp(arguments[counter],"-COMMENT")) == 0)
				strncpy(tag_p->comment,arguments[++counter],28);
			else if ((strcmp(arguments[counter],"-TRACK")) == 0)
				tag_p->track = atoi(arguments[++counter]);
		}
		
		// Open file for reading and writing.
		write_file = fopen(arguments[1],"r+b");

		// Verify it is not an empty file. Perhaps redundant, but not harmful.
		if (write_file == NULL)
			return(1);
		
		// Seek to the position of the existing tag in the file.
		fseek(write_file,-128L,SEEK_END);
		
		// Write the tag to the file in one go. Writes as a byte stream. Then closes the file.
		fwrite(tag_p,1,128,write_file);
		fclose(write_file);
	}
	else if (read_return == 2) { // If the file read correctly, but it did not have an existing ID3v1.1 tag.		
		// Set tag struct to all null bytes initially. That way if some tag is not set, then it is just full of null.
		// There is probably a better way to do this with memset or something like that. We haven't learned that yet...
		strncpy(tag_p->identifier,"TAG",3);
		strncpy(tag_p->title,"",30);
		strncpy(tag_p->artist,"",30);
		strncpy(tag_p->album,"",30);
		strncpy(tag_p->year,"",4);
		strncpy(tag_p->comment,"",28);
		tag_p->separator = '\0';
		tag_p->track = atoi("");
		tag_p->genre = atoi("0");
		
		// Loop to check to see if there are tags to be set. If the user enters an incorrect switch, such as -FOOBAR, that switch and the data that follows it is simply ignored.
		for (counter = 2;counter < argument_count;counter++) {
			if ((strcmp(arguments[counter],"-TITLE")) == 0) // If the switch is for the title, grab the next data, and set it to that.
				strncpy(tag_p->title,arguments[++counter],30);
			else if ((strcmp(arguments[counter],"-ARTIST")) == 0) // If the switch is for the artist, grab the next data, and set it to that.
				strncpy(tag_p->artist,arguments[++counter],30);
			else if ((strcmp(arguments[counter],"-ALBUM")) == 0) // If the switch is for the album, grab the next data, and set it to that.
				strncpy(tag_p->album,arguments[++counter],30);
			else if ((strcmp(arguments[counter],"-YEAR")) == 0) // If the switch is for the year, grab the next data, and set it to that.
				strncpy(tag_p->year,arguments[++counter],4);
			else if ((strcmp(arguments[counter],"-COMMENT")) == 0) // If the switch is for the comment, grab the next data, and set it to that.
				strncpy(tag_p->comment,arguments[++counter],28);
			else if ((strcmp(arguments[counter],"-TRACK")) == 0) // If the switch is for the track number, grab the next data, and set it to that.
				tag_p->track = atoi(arguments[++counter]);
		}
		
		// Open file for apending ONLY.
		write_file = fopen(arguments[1],"ab");

		// Verify it is not an empty file. Perhaps redundant, but not harmful.
		if (write_file == NULL)
			return(1);
		
		// Write the tag to the end of the file in one go. Writes as a byte stream. Then closes the file.
		fwrite(tag_p,1,128,write_file);
		fclose(write_file);
	}
	
	// Print out the new ID3v1.1 data for the user to see.
	printf("New ID3v1.1 tag:\n");
	print_tag(tag_p);
	
	// Return 0 if everything went fine.
	return 0;
}

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
	
	// Variable declaration.
	struct id3tag the_tag;
	int temp_return;
	
	// Initially set temp_return equal to 0.
	temp_return = 0;
	
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
	else if (argc == 2) // If the user only enters a filename, just read out the tag.
		temp_return = read_tag(argv[1],&the_tag);
	else if ((argc % 2) == 1) // If the user enters an invalid number of arguments. 2 arguments for program name and filename, and then 2 more each for switches. If it is an odd number, they're missing something.
		printf("Invalid number of arguments!\n");
	else if ((argc % 2) == 0) // If the user enteres a correct number of arguments beyond just the program name and filename, then they want to write some tag data.
		temp_return = write_tag(argc,argv,&the_tag);
	
	if (temp_return == 1) // If the user entered an invalid filename.
		printf("You entered an invalid filename! :(\n");
	
	// Returns 0 if nothing went wrong with program execution.
	return 0;
}
