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
Project 2
Modified: 2012-07-01 @ 19:00

Note: Tested successfully by compiling with the following argument:
gcc -m32 -O2 -o prodcons -I /u/OSLab/mgj7/linux-2.6.23.1/include/ prodcons.c

The information displayed in stdout/console is also printed to stderr.
This way buffering is avoided and it is easy to see if the semaphores are working.

*/

#include <sys/types.h>
#include <linux/unistd.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <stdio.h> // Print to stderr

// Semaphore structure. Identical to the one in sys.c
struct cs1550_sem {
	int value;
	struct Node *front; // Pointer to the front of the queue. Enables fast dequeue.
	struct Node *back; // Pointer to the back of the queue. Enables fast enqueue.
};

// Wrapper function
void up(struct cs1550_sem *sem) {
       syscall(__NR_cs1550_up, sem);
}

// Wrapper function
void down(struct cs1550_sem *sem) {
       syscall(__NR_cs1550_down, sem);
}

int main(int argc, char *argv[]) 
{
	int numof_prod, numof_con, buffer_size;
	int status; // For waiting
	
	if(argc != 4) { // If the number of arguments is not 4
		printf("Invalid number of arguments!\n");
		exit(0);
	}
	
	// Change the arguments to integers and place them into the variables
	numof_prod = atoi(argv[1]);
	numof_con = atoi(argv[2]);
	buffer_size = atoi(argv[3]);
	
	// Allocate the memory for the shared semaphores. Just allocate in one big chunk. 3x the size of one struct.
	void* sem_ptr = mmap(NULL, sizeof(struct cs1550_sem)*3, PROT_READ|PROT_WRITE, MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	
	struct cs1550_sem *empty;
	struct cs1550_sem *full;
	struct cs1550_sem *mutex;
	empty  = (struct cs1550_sem*)sem_ptr; // Sets the first chunk of shared memory to the empty semaphore
	full = (struct cs1550_sem*)sem_ptr + 1; // Sets the second chunk of shared memory to the full semaphore
	mutex = (struct cs1550_sem*)sem_ptr + 2; // Sets the third chunk of shared memory to the mutex

	// Allocate the memory for the shared buffer. Also includes the size of the buffer at the beginning.
	void* temp_ptr2 = mmap(NULL, sizeof(int)*(buffer_size + 1), PROT_READ|PROT_WRITE, MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	
	int *n_ptr, *in_ptr, *out_ptr, *buffer_ptr; // Pointers into the shared info/buffer memory
	
	n_ptr = (int*)temp_ptr2; // Holds the buffer size (aka the number of items, n)
	in_ptr = (int*)temp_ptr2 + 1; // Holds the position for the producer(s)
	out_ptr = (int*)temp_ptr2 + 2; // Holds the position for the consumer(s)
	buffer_ptr = (int*)temp_ptr2 + 3; // Points to the buffer. Used as an array
	
	*n_ptr = buffer_size; // Set n to the buffer size
	*in_ptr = 0; // Set the producer(s) position to zero
	*out_ptr = 0; // Set the consumer(s) position to zero
	
	// Initialize all of the data in the semaphores to their necessary values
	full->value = 0;
	full->front = NULL;
	full->back = NULL;
	mutex->value = 1;
	mutex->front = NULL;
	mutex->back = NULL;
	empty->value = buffer_size;
	empty->front = NULL;
	empty->back = NULL;
	
	int i; // C99 standards apparently need it declared out here???
	
	for(i = 0; i < buffer_size; i++) { // Fill the buffer with zeros for potential debug
		buffer_ptr[i] = 0;
	}
	
	// Print some info for the user
	printf("Number of Producers: %d\n", numof_prod);
	printf("Number of Consumers: %d\n", numof_con);
	printf("Buffer Sizer: %d\n", buffer_size);
	printf("Press enter to continue...\n");
	getchar(); // Wait for the user to press enter before continuing
	
	for(i = 0; i < numof_prod; i++) { // Loop to create all of the desired producers
		if(fork() == 0) { // Fork and check if it is the child (producer) process
			int pitem;
			while(1) 
			{
				down(empty);
				down(mutex); // Lock the mutex
				
				// Right now, pitem just follows the value of in, but this offers the ability to change it in the future
				pitem = *in_ptr;
				buffer_ptr[*in_ptr] = pitem;
				printf("Chef %c Produced: Pancake%d\n", i+65, pitem); // Turn i into a letter by adding 65
				fprintf(stderr,"Chef %c Produced: Pancake%d\n", i+65, pitem); // Also print to avoid buffering issues
				*in_ptr = (*in_ptr+1) % *n_ptr;
				
				up(mutex); // Unlock the mutex
				up(full);
			}
		}
	}
	
	for(i = 0; i < numof_con; i++) { // Loop to create all of the desired consumers
		if(fork() == 0) { // Fork and check if it is the child (consumer) process
			int citem;
			
			while(1) 
			{
				down(full);
				down(mutex); // Lock the mutex
				
				citem = buffer_ptr[*out_ptr]; // Just pulling out number for now, could be changed in the future
				printf("Customer %c Consumed: Pancake%d\n", i+65, citem); // Turn i into a letter by adding 65
				fprintf(stderr,"Customer %c Consumed: Pancake%d\n", i+65, citem); // Also print to avoid buffering issues
				*out_ptr = (*out_ptr+1) % *n_ptr;
				
				up(mutex); // Unlock the mutex
				up(empty);
			}
		}
	}
	
	wait(&status); // Wait until all processes complete
	return 0; // Finished successfully
}
