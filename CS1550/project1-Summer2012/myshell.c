/*
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
CS1550
Project 1
Modified: 2012-06-03 @ 14:15

Note: Tested successfully by compiling with the following argument:
gcc -O2 -o myshell myshell.c lex.yy.c -lfl

No optimization: 20933 bytes
-O2 optimization: 18988 bytes

>>>>>This version does not open all pipes simultaneously. Thus it is NOT
*perfectly* optimized for multi-programming, but is the easier to
understand and will use the least number of resources at once.<<<<<

>>>>>Requires a lexer.<<<<<
>More information: http://en.wikipedia.org/wiki/Lex_(software)
WORD	[a-zA-Z0-9\/\.-]+
SPECIAL	[()><|&;*]
*/

#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <errno.h>
#include <stdlib.h>
#include <wait.h>

// Make sure TRUE and FALSE are defined as desired
#ifdef FALSE
#undef FALSE
#endif
#define FALSE 0

#ifdef TRUE
#undef TRUE
#endif
#define TRUE  1

extern char **getline();

main() {
	char **args;
	int argsPosition, argsLength, runningLength;
	int pipeCounter, numPipes;
	int status;
	pid_t pid;
	int firstCommand, lastCommand;
	int i, closePipesCounter;
	int lineCounter = 0;
	
	printf("\n"); // Blank line for clarity
	
	while(1) {
		status = 0; // Reset
		
		printf("(%d) myshell> ", lineCounter++); // Print a prefix to each line to make it clear when you should enter commands
		fflush(stdout); // Flush the stdout buffer
		
		args = getline(); // Get arguments
		
		if (args[0] == NULL)
			continue; // If nothing was entered, just go again
		else if (strcmp("exit", args[0]) == 0) { // If it was exit
			exit(EXIT_SUCCESS); // Clean exit
		}
		else if (strcmp("cd", args[0]) == 0) { // If it was cd
			if (args[1] == NULL) { // If there is nothing but cd, just go home
				chdir(getenv("HOME")); // http://linux.die.net/man/3/getenv < man page
			}
			else { // If there is a directory specified
				if (chdir(args[1]) == -1) { // chdir() returns -1 on error
					printf("-myshell: cd: %s: No such file or directory\n", args[1]); // If the path was invalid
				}
			}
		}
		else { // If it was some other command
			for(argsLength=0;args[argsLength] != NULL;argsLength++);
			int pipesFD[argsLength];
			// Reset all of the necessary data
			runningLength = 0;
			pipeCounter = 0;
			int firstCommand = TRUE;
			int lastCommand = FALSE;
			argsPosition = 0;
			argsLength = 0;
			numPipes = 0;
			
			for(argsPosition=0;args[argsPosition] != NULL;argsPosition++) { // Count number of items in args array
				
				runningLength++;
				
				if (firstCommand == FALSE) { //If this is not the first command, that means its piping, and now we check if it is the last command
					if (args[argsPosition + 1] == NULL)
						lastCommand = TRUE;
				}
				
				if (((strcmp("|", args[argsPosition])) == 0) || (lastCommand == TRUE)) { // If the current arg is a pipe OR this is the last command in a series of pipes
					
					char *runningCommands[runningLength + 1]; // Create the runningCommands array appropriately
					
					for(i = 0;i < runningLength;i++) { // Copy over the commands from the args array
						runningCommands[i] = args[argsPosition - runningLength + i + 1]; // Actual copying
					}
					
					runningCommands[runningLength] = NULL; // Verify it is a NULL terminated array
					if (lastCommand == FALSE) {
						runningCommands[runningLength - 1] = NULL; // Additional NULL value for the lastCommand
						if (pipe(pipesFD + pipeCounter) == -1) // Set up new pipe
							exit(1); // Pipe failed
						numPipes++; // Increase the counter numPipes
					}
					
					pid = fork(); // Fork new process
					
					if (pid == -1) { // fork() could not create a child process
						perror("fork() errored!"); // print the error to system error
						exit(1); // Exit with bad status
					}
					else if (pid == 0) { // Child process
						if ((lastCommand == FALSE) && (firstCommand == FALSE)) {
							dup2(pipesFD[pipeCounter - 2], 0);
							dup2(pipesFD[pipeCounter + 1], 1);
						}
						else if (firstCommand == TRUE) {
							dup2(pipesFD[1], 1); // Should always be pipesFD[1] for the first
						}
						else if (lastCommand == TRUE) {
							dup2(pipesFD[pipeCounter - 2], 0); // Zero if only 1 pipe
						}
						else {
							_exit(1); // Some sort of problem here
						}
						
						for (closePipesCounter = 0;closePipesCounter < numPipes;closePipesCounter++) { // Close all active pipes
							close(pipesFD[closePipesCounter * 2]);
							close(pipesFD[(closePipesCounter * 2) + 1]);
						}
						
						if ((execvp(runningCommands[0], runningCommands)) == -1)
							_exit(1);  // execvp() failed, return error so that a message can be printed
						
					}
					else {
						if (lastCommand == TRUE) {
							for (closePipesCounter = 0;closePipesCounter < numPipes;closePipesCounter++) { // Close all active pipes
								close(pipesFD[closePipesCounter * 2]);
								close(pipesFD[(closePipesCounter * 2) + 1]);
							}
						}
						else { // If not the last command
							close(pipesFD[pipeCounter - 2]); // Close the read end of the previous pipe
							close(pipesFD[pipeCounter + 1]); // Close the write end of the current pipe
						}
						
						waitpid(pid, &status, 0); // Wait for child
						firstCommand = FALSE; // Being here means that at least the first command has been instructed to run
						pipeCounter = pipeCounter + 2; // Increment pipeCounter
						
						if (WIFEXITED(status)) { // Check to see if got a status
							if (WEXITSTATUS(status) == 1) {// If the status was an exit error (i.e. _exit(1))
								printf("-myshell: %s: command not found\n",runningCommands[0]);
								break; // Some command was entered wrong and printed before
							}
						}
					}
					runningLength = 0; // Reset runningLength
				}
			}
			
			// In the case that there was no piping
			if (runningLength == argsPosition && WEXITSTATUS(status) != 1) {
				pid = fork();
				
				if (pid == -1) { // fork() could not create a child process
						perror("fork() errored!"); // Print the error to system error
						exit(1); // Exit with bad status
					}
				else if (pid == 0) { // Child process
					if ((argsPosition > 2) && (strcmp(">", args[argsPosition - 2])) == 0) { // Redirect if necessary
						if (freopen(args[argsPosition - 1], "a", stdout) == NULL) // Redirect to file specified with "a" for append
							printf("-myshell: %s: invalid file\n",args[argsPosition - 1]);
						args[argsPosition-1] = NULL; // Remove the filename
						args[argsPosition-2] = NULL; // Remove the > sign
					}
					
					if ((execvp(args[0], args)) == -1)
						printf("-myshell: %s: command not found\n",args[0]);
					_exit(1);  // execvp() failed
				}
				else { // Parent process 
					if (waitpid(pid, &status, 0) == -1) // Wait for child
						exit(1); // Error waiting
				}
			}
		}
	}
}
