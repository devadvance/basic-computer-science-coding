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
#include <fcntl.h>
#include <ctype.h>
#include <sys/stat.h>

#define BUFSIZE 1024
#define FILENAMESIZE 100

int handle_connection(int sock);
int getFilePathFromRequest(char * request, char * out, int out_size);

int main(int argc, char * argv[]) {
    int server_port = -1;
    int rc          =  0;
    int sock        = -1;

	printf("%s\n", argv[0]);

    /* parse command line args */
    if (argc != 3) {
	fprintf(stderr, "usage: http_server2 k|u port\n");
	exit(-1);
    }

    server_port = atoi(argv[2]);

	// use the stack requested by the user
	char stack = tolower(argv[1][0]);
	if(stack == 'k') {
		minet_init(MINET_KERNEL);
	}
	else if(stack == 'u') {
		minet_init(MINET_USER);
	}
	else {
		fprintf(stderr, "usage: http_server2 k|u port\n");
		exit(-1);
	}

    if (server_port < 1500) {
	fprintf(stderr, "INVALID PORT NUMBER: %d; can't be < 1500\n", server_port);
	exit(-1);
    }

    /* initialize and make socket */
	if((sock = minet_socket(SOCK_STREAM)) < 0) {
		fprintf(stderr, "Error occured attempting to initialize socket.\n");
		exit(-1);
	}

    /* set server address*/
	sockaddr_in serv;
	memset(&serv, 0, sizeof(sockaddr_in));
	serv.sin_family = AF_INET;
	serv.sin_port = htons(server_port);
	serv.sin_addr.s_addr = INADDR_ANY;

    /* bind listening socket */
	if(minet_bind(sock, &serv) < 0) {
		fprintf(stderr, "Bind to socket failed.\n");
		exit(-1);
	}

    /* start listening */
	if(minet_listen(sock, SOMAXCONN) < 0) {
		fprintf(stderr, "Attempt to listen on socket failed.\n");
		exit(-1);
	}

    /* connection handling loop: wait to accept connection */

	/* create read list */
	fd_set master;
	fd_set connected_list;
	timeval timeout;

	timeout.tv_sec = 2;
	timeout.tv_usec = 0;

	FD_ZERO(&master);
	FD_ZERO(&connected_list);
	FD_SET(sock, &master);

	int maxsocks = sock;

    while (1) {
		connected_list = master;

		int select_result = minet_select(maxsocks+1, &connected_list, 0, 0, 0);

		/* do a select */
		if(select_result > 0) {
			/* process sockets that are ready */
			for(int i = 0; i < maxsocks+1; ++i) {
				
				if(FD_ISSET(i, &connected_list)) {
					/* for the accept socket, add accepted connection to connections */
					if(i == sock) {
						int client = minet_accept(sock, 0);
						
						if(client > 0) {
							if(client > maxsocks)
								maxsocks = client;
							FD_SET(client, &master);
						}
						else {
							// failed
							continue;
						}
					}
					else {
						/* for a connection socket, handle the connection */
						rc = handle_connection(i);
						FD_CLR(i, &master);
					}
				}
			}
		}
    }
}

int handle_connection(int sock) {
    bool ok = false;

    char * ok_response_f = "HTTP/1.0 200 OK\r\n"	\
	"Content-type: text/html\r\n"			\
	"Content-length: %d \r\n\r\n";
 
    char * notok_response = "HTTP/1.0 404 FILE NOT FOUND\r\n"	\
	"Content-type: text/html\r\n\r\n"			\
	"<html><body bgColor=black text=white>\n"		\
	"<h2>404 FILE NOT FOUND</h2>\n"
	"</body></html>\n";

	// accept connection from client
	int serv = sock;//minet_accept(sock, 0);;

	// error occured
	if(serv < 1) {
		return -1;
	}
    
    /* first read loop -- get request and headers*/
	char recvbuf[BUFSIZE];
	char totalrecv[BUFSIZE*1024];

	// check for error in trying to receive request
	/*if(read(serv, recvbuf, BUFSIZE) < 0) {
		return -1;
	}*/
	int received = read(serv, recvbuf, BUFSIZE);
	int recvpos = 0;
	do {
		if(received > 0) {
			memcpy(totalrecv+recvpos, recvbuf, received);
			recvpos += received;
			// last block already received
			if(received < BUFSIZE)
				break;
		}
		received = read(serv, recvbuf, BUFSIZE);
	} while(received > 0 && (recvpos < (BUFSIZE*1024)));

	// display what was read to the screen
	printf("%s\n", totalrecv);

    /* parse request to get file name */
    /* Assumption: this is a GET request and filename contains no spaces*/
	char filename[FILENAMESIZE];
	if(getFilePathFromRequest(totalrecv, filename, FILENAMESIZE) < 0) {
		fprintf(stderr, "Malformed request received from client.\n");
		return -1;
	}

    /* try opening the file */
	int content_length = 0;
	char * databuf = 0;
	FILE * reqfile = 0;
	int filenamelen = strlen(filename);

	struct stat s;
	
	// need to find index.htm, html, php, since user requested /
	if(!strcmp(filename, "")) {
		reqfile = fopen("index.htm", "rb");
		if(!reqfile)
			reqfile = fopen("index.html", "rb");
		if(!reqfile)
			reqfile = fopen("index.php", "rb");
	}
	// directory
	else if(filename[filenamelen-1] == '/' || ((stat(filename,&s) == 0) && (s.st_mode & S_IFDIR))) {
		char tempbuf[FILENAMESIZE];
		strcpy(tempbuf, filename);
		if(filename[filenamelen-1] != '/') {
			strcat(tempbuf, "/");
		}

		sprintf(filename, "%sindex.htm", tempbuf);
		reqfile = fopen(filename, "rb");
		if(!reqfile) {
			sprintf(filename, "%sindex.html", tempbuf);
			reqfile = fopen(filename, "rb");
		}
		if(!reqfile) {
			sprintf(filename, "%sindex.php", tempbuf);
			reqfile = fopen(filename, "rb");
		}
	}
	// everything else
	else {
		reqfile = fopen(filename, "rb");
	}

	if(!reqfile) {
		fprintf(stderr, "Attempt to open file (%s) failed.\n", filename);
		ok = 0;
	}
	else {
		fseek(reqfile, 0, SEEK_END);
		content_length = ftell(reqfile);
		fseek(reqfile, 0, SEEK_SET);

		databuf = new char[content_length];
		memset(databuf, 0, content_length);

		if(!fread(databuf, 1, content_length, reqfile)) {
			fprintf(stderr, "Attempt to read file (%s) failed.\n", filename);
			ok = 0;
			delete [] databuf;
		}
		else {
			ok = 1;
		}

		fclose(reqfile);
	}

    /* send response */
    if (ok) {
	/* send headers */
		unsigned int buf_size = strlen(ok_response_f)+4096;
		char * sendbuf = new char[buf_size];
		memset(sendbuf, 0, buf_size);
		sprintf(sendbuf, ok_response_f, content_length);
		// send the header
		if (minet_write(serv, sendbuf, strlen(sendbuf)) < 0) {
			fprintf(stderr, "Attempt to send header failed.\n");
			ok = 0;
		}
		if (minet_write(serv, databuf, content_length) < 0) {
			fprintf(stderr, "Attempt to send data failed.\n");
			ok = 0;
		}
		delete [] sendbuf;
		delete [] databuf;
    } else {
	// send error response
		minet_write(serv, notok_response, strlen(notok_response)+1);
    }
    
    /* close socket and free space */
	close(serv);
  
    if (ok) {
	return 0;
    } else {
	return -1;
    }
}

int getFilePathFromRequest(char * request, char * out, int out_size) {
	int req_len = strlen(request);
	char * temp = new char[req_len+1];
	char * temp2 = new char[req_len+1];
	strcpy(temp, request);
	char * pos = strstr(temp, " HTTP/1");
	// malformed request
	if(!pos || (strncmp(temp, "GET ", 4) != 0)) {
		delete [] temp;
		delete [] temp2;
		return -1;
	}

	// kill it
	pos[0] = 0;

	// get requested file path
	if(temp[4] == '/') {
		strcpy(temp2, temp+5);
	}
	else {
		strcpy(temp2, temp+4);
	}

	// buffer too small
	if((int)strlen(temp2)+1 > out_size) {
		delete [] temp;
		delete [] temp2;
		return -1;
	}

	strcpy(out, temp2);
	delete [] temp;
	delete [] temp2;
	return 0;
}