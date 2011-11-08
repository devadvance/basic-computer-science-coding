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
Project 3
Modified: 2011-11-07 @ 21:45

Note: Tested successfully by compiling with the following argument:
gcc -m32 -o malloctest malloctest.c

*/

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

//include your code

#include "mymalloc.h"

//replace malloc here with the appropriate version of mymalloc
#define MALLOC my_bestfit_malloc
//replace free here with the appropriate version of myfree
#define FREE my_free

/* This program performs several tests to check the reliability of the
 * custom malloc/free functions defined in the header mymalloc.h.
 * The tests check to make sure that the heap size is dealt with
 * appropriately, that the "best fit" idea is followed for adjacent
 * free space, and that nothing goes wrong in general.
 */
int main() {
	
	char *test;
	char *test2;
	char *test3;
	char *test4;
	char *test5;
	struct node *testing;
	
	printf("\nBegin testing. sbrk at the beginning %p:\n\n\n", sbrk(0));
	
	printf("==================================================\n");
	printf("First test part A.\nAllocates 3 times, store strings to the first and last.\nThen free the first and last.\n");
	printf("==================================================\n");
	test = (char*)MALLOC(50);
	test2 = (char*)MALLOC(40);
	test3 = (char*)MALLOC(1000);
	
	strcpy(test, "Hello!");
	printf("test: %p\n", test);
	printf("output: %s\n",test);
	
	printf("test2: %p\n", test2);
	printf("output: %s\n",test2);
	
	strcpy(test3, "This is a really long sentence that simply serves the purposes to \ntest the memory location designated by test3 \nand has no other purpose other than to print and \nthen be done with it!");
	printf("test3: %p\n", test3);
	printf("output: %s\n",test3);
	
	printf("Linked list of nodes:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test\n");
	FREE(test);
	
	printf("Freeing test3\n");
	FREE(test3);
	
	printf("Linked list of nodes after freeing:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	printf("==================================================\n");
	printf("End first test part A.\n");
	printf("sbrk(0): %p\n", sbrk(0));
	printf("==================================================\n\n\n");
	
	
	
	printf("==================================================\n");
	printf("First test part B.\nAllocate another space, smaller than the free node in the heap. \nShould not change the heap size.\nAlso tests to see if merging free nodes works properly.\n");
	printf("==================================================\n");
	
	test = (char*)MALLOC(10);
	
	strcpy(test, "Goodbye!");
	printf("test: %p\n", test);
	printf("output: %s\n",test);
	
	printf("Linked list of nodes:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test\n");
	FREE(test);
	
	printf("Linked list of nodes after freeing:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	printf("==================================================\n");
	printf("End first test part B.\n");
	printf("sbrk(0): %p\n", sbrk(0));
	printf("==================================================\n\n\n");
	
	
	
	printf("==================================================\n");
	printf("First test part C.\nAllocate another space, larger than the free node in the heap. \nShould change the heap size.\nThen allocated a smaller node; uses the first empty.\nThen free the third node (not last).\n");
	printf("==================================================\n");
	
	test4 = (char*)MALLOC(500);
	test5 = (char*)MALLOC(10);
	
	printf("Should have changed the heap. sbrk(0): %p\n", sbrk(0));
	
	strcpy(test4, "This is a big sentence that should be located at the end of the heap.\nThe malloced size for this is 500.\nCool right?");
	printf("\ntest4: %p\n", test4);
	printf("output: %s\n",test4);
	
	strcpy(test5, "Hmmmm?!");
	printf("test5: %p\n", test5);
	printf("output: %s\n",test5);
	
	printf("Linked list of nodes:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test2\n");
	FREE(test2);
	
	printf("Linked list of nodes after freeing:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("sbrk(0): %p\n", sbrk(0));
	
	printf("Freeing test5\n");
	FREE(test5);
	printf("Freeing test4\n");
	FREE(test4);
	
	printf("==================================================\n");
	printf("End first test part C.\n");
	printf("sbrk(0): %p\n", sbrk(0));
	printf("==================================================\n\n\n");
	
	
	
	printf("==================================================\n");
	printf("Second test.\nTests attempts to allocate zero and negatives values.\n");
	printf("==================================================\n");
	
	printf("Trying to allocate -100\n");
	test = MALLOC(-100);
	if (test == (void*)NULL) {
		printf("Returned NULL, this is good.\n");
	}
	else {
		printf("SOMETHING WRONG HERE!\n");
	}
	
	printf("Trying to allocate 0\n");
	test = MALLOC(0);
	if (test == NULL) {
		printf("Returned NULL, this is good.\n");
	}
	else {
		printf("SOMETHING WRONG HERE!\n");
	}
	printf("==================================================\n");
	printf("End second test.\n");
	printf("sbrk(0): %p\n", sbrk(0));
	printf("==================================================\n\n\n");
	
	printf("==================================================\n");
	printf("Third test.\n");
	printf("==================================================\n");
	/*
	test = MALLOC(INT_MAX);
	test2 = MALLOC(INT_MAX);
	test3 = MALLOC(INT_MAX);
	printf("here\n");
	if (test == (void*)NULL) {
		printf("test RETURNED NULL\n");
	}
	else {
		printf("test was good\n");
		FREE(test);
	}
	
	if (test2 == (void*)NULL) {
		printf("test2 RETURNED NULL\n");
	}
	else {
		printf("test2 was good\n");
		FREE(test2);
	}
	
	if (test3 == (void*)NULL) {
		printf("test3 RETURNED NULL\n");
	}
	else {
		printf("test3 was good\n");
		FREE(test3);
	}
	
	printf("Linked list of nodes after freeing:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	*/
	
	printf("==================================================\n");
	printf("End third test.\n");
	printf("sbrk(0): %p\n", sbrk(0));
	printf("==================================================\n\n\n");
	
	
	
	printf("==================================================\n");
	printf("Fourth test.\nAllocate 5 spaces.\nFree the 1st and 3rd, then the 2nd.\nChecks to see if merging works with 3 free spaces.\n");
	printf("==================================================\n");
	
	test = (char*)MALLOC(50);
	test2 = (char*)MALLOC(40);
	test3 = (char*)MALLOC(1000);
	test4 = (char*)MALLOC(500);
	test5 = (char*)MALLOC(100);
	printf("sbrk(0): %p\n", sbrk(0));
	
	strcpy(test, "Hello from test!");
	printf("test: %p\n", test);
	printf("output: %s\n",test);
	
	strcpy(test2, "Hello from test2!");
	printf("test2: %p\n", test2);
	printf("output: %s\n",test2);
	
	strcpy(test3, "This is a really long sentence that simply serves the purposes to \ntest the memory location designated by test3 \nand has no other purpose other than to print and \nthen be done with it!");
	printf("test3: %p\n", test3);
	printf("output: %s\n",test3);
	
	strcpy(test4, "This is a big sentence that should be located at the end of the heap.\nThe malloced size for this is 500.\nCool right?");
	printf("\ntest4: %p\n", test4);
	printf("output: %s\n",test4);
	
	strcpy(test5, "Hmmmm?! Test5 here");
	printf("test5: %p\n", test5);
	printf("output: %s\n",test5);
	
	printf("Linked list of nodes:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test\n");
	FREE(test);
	printf("Freeing test3\n");
	FREE(test3);
	printf("sbrk(0): %p\n", sbrk(0));
	
	printf("Linked list of nodes after freeing:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test2\n");
	FREE(test2);
	printf("sbrk(0): %p\n", sbrk(0));
	
	printf("Linked list of nodes after freeing:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test4\n");
	FREE(test4);
	printf("Freeing test5\n");
	FREE(test5);
	printf("sbrk(0): %p\n", sbrk(0));
	
	printf("==================================================\n");
	printf("End fourth test.\n");
	printf("sbrk(0): %p\n", sbrk(0));
	printf("==================================================\n\n\n");
	
	
	printf("==================================================\n");
	printf("Fifth test.\nAllocate 4 spaces.\nFree the 1st. Allocated something small to take part of the first.\nThen free the 1st again.\nAllocate something the exact size of the free block.\nFree it, then free all the rest of the blocks.\n");
	printf("==================================================\n");
	
	test = (char*)MALLOC(100);
	test2 = (char*)MALLOC(100);
	test3 = (char*)MALLOC(1000);
	test4 = (char*)MALLOC(500);
	//test5 = (char*)MALLOC(100);
	printf("sbrk(0): %p\n", sbrk(0));
	
	strcpy(test, "Hello from test!");
	printf("test: %p\n", test);
	printf("output: %s\n",test);
	
	strcpy(test2, "Hello from test2!");
	printf("test2: %p\n", test2);
	printf("output: %s\n",test2);
	
	strcpy(test3, "Hello from test3!");
	printf("test3: %p\n", test3);
	printf("output: %s\n",test3);
	
	strcpy(test4, "Hello from test4!");
	printf("test4: %p\n", test4);
	printf("output: %s\n",test4);
	
	printf("Linked list of nodes:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test\n");
	FREE(test);
	
	test5 = (char*)MALLOC(50);
	
	strcpy(test5, "Hmmmm?! Test5 here");
	printf("test5: %p\n", test5);
	printf("output: %s\n",test5);
	
	printf("Linked list of nodes:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test5\n");
	FREE(test5);
	
	printf("Same thing again, but this time should be a perfect fit for the empty node.\n");
	
	test5 = (char*)MALLOC(100);
	
	strcpy(test5, "Hmmmm?! Test5 here");
	printf("test5: %p\n", test5);
	printf("output: %s\n",test5);
	
	printf("Linked list of nodes:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test5\n");
	FREE(test5);
	
	printf("Linked list of nodes:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test2\n");
	FREE(test2);
	
	printf("Freeing test3\n");
	FREE(test3);
	
	printf("Linked list of nodes:\n");
	testing = first_node;
	while(testing != NULL) {
		printf("Current Node: %p Size: %d Used: %d Previous: %p Next: %p\n", testing,testing->size,testing->used,testing->previous_node,testing->next_node);
		testing = testing->next_node;
	}
	
	printf("Freeing test4\n");
	FREE(test4);
	
	printf("==================================================\n");
	printf("End fifth test.\n");
	printf("sbrk(0): %p\n", sbrk(0));
	printf("==================================================\n\n\n");
	
	
	printf("\nDone testing. sbrk at the end %p:\n", sbrk(0));
	
	return 0;
}
