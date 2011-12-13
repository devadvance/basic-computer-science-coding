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
Project 5
Modified: 2011-12-11 @ 23:30

Note: Tested successfully by compiling with the following argument:
gcc -O2 -pthread -o server server.c

Also, this program assumes that any file requested RELATIVE to the directory of the server!
For example, if this server is in /home/testuser/public/html/, then a request saying
GET http://localhost:80/example.html
is translated to /home/testuser/public/html/example.html
*/

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <sys/wait.h>
#include <errno.h>
#include <pthread.h>
#include <time.h>
#include <string.h>

// Max threads/(open/finished) connections is # defined here
#define MAX_PTHREADS 100

/* intlen
 * Takes an int and returns the length of the int.
 * Length means how many characters it takes to print on screen/to file. Aka how many digits.
 */
int intlen(float start) { 
	int length = 0; 
	while(start >= 1) { 
		start = start/10; 
		length++; 
	} 
	return length; 
} 

/* connection
 * Handles connections to the server. Returns file contents/file not found/bad command.
 */
void *connection(void *arg) {
	// Mutex
	pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
	
	// File size variables
	int read_file_size;
	int read_file_size_length; // Length if the string value of the file size
	
	// Time variables
	time_t current_time;
	struct tm* current_gmtime;
	
	// Stats file
	FILE *stats_file;
	int stats_string_length;
	
	// Read file variables
	char file_name[100];
	FILE *read_file;
	
	// Buffers
	char receive_buffer[1024];
	char *send_buffer;
	int receive_buffer_size = 1024;
	int send_buffer_size;
	char time_buffer[30];
	char *page_buffer;
	char *stats_buffer;
	int stats_buffer_size;
	
	// Receiving variables
	int receive_return;
	
	// Sending variables
	int send_return;
	int sent_so_far;
	
	// "Convert" arg to connfd for better code legibility
	int *connfd_value = (int *)arg;
	
	// Receive a packet
	recv(*connfd_value, receive_buffer, receive_buffer_size, 0);
	if (receive_return < 0) { // If receive returned an error
		printf("Error encountered in receive.\n");
		exit(0);
	}
	
	// Get the current date and time for the information header
	time(&current_time);
	current_gmtime = gmtime(&current_time);
	strftime(time_buffer, 30, "%a, %d %b %Y %X %Z", current_gmtime);
	
	if (!(strncmp(receive_buffer, "GET ", 4) == 0)) {
		printf("Unknown command encountered.\n");
		send_buffer = (char *)malloc(32);
		strcpy(send_buffer,"Unknown command encountered.\n\n");
		// Get the size of the send_buffer for later
		send_buffer_size = strlen(send_buffer);
		strcpy(file_name, "N/A");
	}
	else {
		// Get the file name requested
		int i;
		int j = 0; // In case a slash was skipped, a second variable is needed
		for (i = 0; i < 100; i++)
		{
			if ((receive_buffer[j+5] == ' ') || (receive_buffer[j+5] == '\0') || (receive_buffer[j+5] == '\n')) {
				break;
			}
			if ((i == 0) && (receive_buffer[i+4] == '/')) { // Make sure there's no beginning slash
				j--; // Decrement j if a slash is found. That means that the character transfer is offset by 1 now
			}
			else {
				file_name[j] = receive_buffer[i+4]; // Copy character
			}
			if (i == 98) {
				printf("Encountered a request for a file that had too long of a name.\n");
			}
			j++;
		}
		file_name[i] = '\0';
		
		// Try opening the file
		read_file = fopen(file_name,"r");
		if (read_file != NULL)
		{
			// Getting file size
			fseek(read_file,0,SEEK_END);
			read_file_size = ftell(read_file);
			fseek(read_file,0,SEEK_SET);
			
			// Set the length of that int
			read_file_size_length = intlen(read_file_size);
			
			// Calloc the size of the file. Calloc is better than malloc because it zeroes the space
			page_buffer = (char *)calloc(read_file_size + 1, sizeof(char));
			// Read the file
			fread(page_buffer,1,read_file_size,read_file);
			// Close the file
			fclose(read_file);
			
			// Malloc the send buffer
			send_buffer = (char*)malloc(read_file_size+read_file_size_length+120);
			// Add information and date beginning
			strcpy(send_buffer,"HTTP/1.1 200 OK\nDate: ");
			// Adds the date and time
			strcat(send_buffer,time_buffer);
			// Combines info and variables
			sprintf(send_buffer,"%s\nContent-Length: %d\nConnection: close\nContent-Type: text/html\n\n",send_buffer,read_file_size);
			// Concat the info with the page to send_buffer
			strcat(send_buffer, page_buffer);
			// Add new line at the end just in case
			strcat(send_buffer, "\n");
			// Get the size of the send_buffer for later
			send_buffer_size = strlen(send_buffer);
			// Free the page_buffer. No longer needed
			free(page_buffer);
		}
		else // If the file does not exist
		{
			send_buffer = (char *)malloc(64);
			strcpy(send_buffer,"HTTP/1.1 404 File Not Found\n\n");
			// Get the size of the send_buffer for later
			send_buffer_size = strlen(send_buffer);
		}
	}
	
	sent_so_far = 0; // How much of the data has been sent so far
	// Loop to send the page. Loop is due to the potential size being greater than that of a packet
	while (sent_so_far < send_buffer_size)
	{
		send_return = send(*connfd_value, send_buffer+sent_so_far, send_buffer_size-sent_so_far, 0);
		if (send_return < 0) // If an error occured sending the file
		{
			printf("Encountered an error while sending the file.\n");
			exit(0);
		}
		// Increase what has been sent so far
		sent_so_far += send_return;
	}
	
	// Close the connection
	close(*connfd_value);
	
	// Free the send buffer
	free(send_buffer);
	
	// Locks the current thread in for writing to stats.txt
	pthread_mutex_lock(&mutex);
	
	// Get the total size of the stats. Written this way for clarity
	stats_buffer_size = strlen(file_name);
	stats_buffer_size += strlen(time_buffer);
	stats_buffer_size += 32;
	// Malloc for stats_buffer
	stats_buffer = malloc(stats_buffer_size);
	// Create the string of stats to output
	sprintf(stats_buffer, "%s - File Requested: %s\n",time_buffer,file_name);
	stats_string_length = strlen(stats_buffer);
	
	// Try to open/create the stats file for appending
	stats_file = fopen("stats.txt","a");
	if (stats_file != NULL) // If the file is usable
	{
		// Write to the stats file
		fwrite(stats_buffer,stats_string_length,1,stats_file);
		// Close the stats file
		fclose(stats_file);
	}
	else {
		// Note: This does not cause the program to terminate.
		printf("Encountered an error opening/creating stats file for appending.\n");
	}
	// Free the stats_buffer
	free(stats_buffer);
	
	// Unlock the thread when done
	pthread_mutex_unlock(&mutex);
	
	return NULL;
}

/* main
 * Creates pthreads for each connection.
 * Maximum of 100 open/finished connections by default. Defined by macro at the top.
 * Takes a port via commandline argument.
 */
int main (int argc, char *argv[]) {
	// Connection variables
	int port;
	int sfd;
	int status;
	struct sockaddr_in addr;
	pid_t pid;
	int connfd_array[MAX_PTHREADS];
	
	// pthread variables
	void *exit_spointer;
	pthread_t thread_array[MAX_PTHREADS];
	int thread_int_array[MAX_PTHREADS];
	int thread_counter;
	
	if(argc < 2) {
		printf("Usage: server <port>\n");
		exit(0);
	}
	
	// Get port from command line
	port = atoi(argv[1]);
	
	// Creat the socket
	sfd = socket(PF_INET, SOCK_STREAM, 0);
	if(sfd == -1)
	{
		printf("Encountered an error while creating the socket.");
		return -1;
	}
	
	// Unbind on termination
	int so_reuseaddr = 1;
	setsockopt(sfd, SOL_SOCKET, SO_REUSEADDR, &so_reuseaddr, sizeof(so_reuseaddr));
	
	// Set up structure. Set port according to command line
	memset(addr.sin_zero, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(port);
	addr.sin_addr.s_addr = INADDR_ANY;

	// Bind to port
	int id = bind(sfd, (struct sockaddr *)&addr, sizeof(addr));
	if(id <0) // Check to make sure bind did not error
	{
		printf("Encountered an error in bind. ID: %d\n", id);
		return -1;
	}
	
	// Max number of connections (threads) is defined at the top. Default is 100
	thread_counter = 0;
	while(thread_counter < 100) {

		// Listen for a connection
		if(listen(sfd, 10) < 0) // Make sure listen did not error
		{
			printf("Encountered an error in listen.\n");
			return -1;
		}
		
		// Accept a connection
		connfd_array[thread_counter] = accept(sfd, NULL, NULL);
		if(connfd_array[thread_counter] < 0) // Make sure accepting did not error
		{
			printf("Encountered an error in accept.\n");
			return -1;
		}
		
		printf("Creating a connection...\n");
		// Create the thread/connection
		thread_int_array[thread_counter] = pthread_create(&(thread_array[thread_counter]), NULL, connection, &(connfd_array[thread_counter]));
		
		// Increment the thread counter
		thread_counter++;
	}
	
	// Wait for all pthreads (connections) to close
	int j;
	for (j = 0;j < thread_counter;j++) {
		pthread_join(thread_array[j], &exit_spointer);
	}
	
	// Close the socket
	close(sfd);
	// Return 0 if everything went okay
	return 0;
}
