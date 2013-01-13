/*
Copyright (c) 2012, Raymond Wang, Matt Joseph
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
CS1652
Project 1
*/

#include "minet_socket.h"
#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>
#include <string>
#include <iostream>

#define BUFSIZE 1024

using namespace std;

int main(int argc, char * argv[]) {

	char * server_name = NULL;
	int server_port	= -1;
	char * server_path = NULL;
	char * req		 = NULL;
	bool ok			= false;
	
	char buf[BUFSIZE];
	struct hostent *hp;
	struct sockaddr_in saddr;
	fd_set fds;
	string response = "";
	string header = "";
	string status;
	string::size_type position;
	int result; // Used for return values
	int socket_fd; // Socket file descriptor

	/*parse args */
	if (argc != 5) {
	fprintf(stderr, "usage: http_client k|u server port path\n");
	exit(-1);
	}

	server_name = argv[2];
	server_port = atoi(argv[3]);
	server_path = argv[4];

	req = (char *)malloc(strlen("GET  HTTP/1.0\r\n\r\n") + strlen(server_path) + 1);  

	/* initialize */
	if (toupper(*(argv[1])) == 'K') { 
		minet_init(MINET_KERNEL);
	} else if (toupper(*(argv[1])) == 'U') { 
		minet_init(MINET_USER);
	} else {
		fprintf(stderr, "First argument must be k or u\n");
		free(req);
		exit(-1);
	}

	/* make socket */
	socket_fd = minet_socket(SOCK_STREAM);
	if (socket_fd == -1) {
		fprintf(stderr, "Encountered an error while creating the socket.\n");
		free(req);
		return -1;
	}
	
	/* get host IP address  */
	/* Hint: use gethostbyname() */
	hp = gethostbyname(server_name);
	if (hp == 0) {
		fprintf(stderr, "Encountered an error while getting host IP address.\n");
		free(req);
		return -1;
	}
	/* set address */
	saddr.sin_family = AF_INET;
	memcpy(&saddr.sin_addr.s_addr, hp->h_addr, hp->h_length);
	
	saddr.sin_port = htons(server_port);
	
	/* connect to the server socket */
	if (minet_connect(socket_fd,&saddr) < 0) {
		fprintf(stderr, "Encountered an error connecting to server.\n");
		free(req);
		return -1;
	}

	/* send request message */
	sprintf(req, "GET %s HTTP/1.0\r\n\r\n", server_path);
	if (minet_write(socket_fd, req, strlen(req)) <= 0) { // Might need to only be less than...
		fprintf(stderr, "Encountered an error sending message to server.\n");
		free(req);
		return -1;
	}
	
	/* wait till socket can be read. */
	/* Hint: use select(), and ignore timeout for now. */
	
	FD_ZERO(&fds); // Clear the file descriptor set
	FD_SET(socket_fd, &fds); // Add the socket file descriptor to the set
	
	if (minet_select(socket_fd+1, &fds, NULL, NULL, NULL) < 0) { // No timeout. Which makes sense here
        fprintf(stderr, "Encountered an error selecting socket.\n");
		free(req);
        minet_close(socket_fd); // Close socket before exiting
        minet_deinit();
		return -1;
    }
	
	/* first read loop -- read headers */
	result = minet_read(socket_fd, buf, BUFSIZE-1);
	
	while (result > 0) {
		buf[result] = '\0'; // Set to empty string
		response += string(buf);
		position = response.find("\r\n\r\n",0);
		
		if (position != string::npos) { // If you found the header
			header = response.substr(0, position); // Split off the header
			response = response.substr(position+4); // Split of the response text
			break;
		}
		
		result = minet_read(socket_fd, buf, BUFSIZE-1);
	}
	
	/* examine return code */   
	status = header.substr(header.find(" ") + 1); // Get status from header
	status = status.substr(0, status.find(" "));
	
	//Skip "HTTP/1.0"
	//remove the '\0'

	// Normal reply has return code 200
	/* print first part of response: header, error code, etc. */
	if(status == "200") {
		ok = true; // It all went okay
		cout << response; // Have to use cout because of strings
	}
	else {
		ok = false;
		cerr << header + "\r\n\r\n" + response; // Have to use cerr because of strings
	}

	/* second read loop -- print out the rest of the response: real web content */
	while ((result = minet_read(socket_fd, buf, BUFSIZE-1)) > 0) {
		buf[result] = '\0';
		if(ok) {
			printf("%s", buf);
		}
		else {
			fprintf(stderr, buf);
		}
	}
	/*close socket and deinitialize */
	minet_close(socket_fd);
	minet_deinit();
	free(req);
	
	if (ok) {
	return 0;
	} else {
	return -1;
	}
}
