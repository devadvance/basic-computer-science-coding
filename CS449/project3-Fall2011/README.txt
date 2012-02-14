
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
Project 3
Modified: 2011-11-06 @ 02:30

Note: Goes with mymalloc.h
##################################################
##################################################
README for mymalloc.h

I wrote this README at the urging of Professor Misurda, who noted that point may be
subtracted since the method of grading MAY NOT take into account how I have
implemented malloc.

I refer to the custom malloc function as custom_malloc, and the custom free
function as custom_free. These are just general names, not specific to my implementation.
##################################################


##################################################
>>>>> How this implementation of malloc works <<<<<

It uses a linked-list data structure to keep track of allocated memory in the heap.
Rather than a separate link list structure, it stores the nodes as header information
for each allocated space.
##################################################


##################################################
>>>>> Inherent problem(s) with this implementation <<<<<

Let us assume for the moment that this will be used on:
1) A 32bit system
2) A system that allocates memory at the OS level in chunks/pages or anything
you want to call it.
3) These chunks/pages/allocation sizes/etc are a size that is a power of 2.
This is the most logical way to do it, and is done so at an OS level.

Let us also assume that our malloc function should:
1) Not leak memory.
2) Not lose track of memory it has allocated.

To explain the inherent problem, let us take a linked-list that uses headers
as nodes.

In C, this struct defines the header:
struct node {
	unsigned int size; // Including header size
	unsigned int used; // Is the node used or not
	struct node *previous_node; // Pointer to the previous node
	struct node *next_node; // Pointer to the next node
};

This header is 16 bytes. That means that when the custom malloc function allocates
space using sbrk, it includes an extra 16 bytes for the header. So a call to malloc
for 10 bytes:

custom_malloc(10);

actually allocates 26 bytes instead of 10. 16 byte header + 10 bytes for the user = 26 bytes.

Let us take the scenario where custom_malloc is called twice.

test1 = custom_malloc(10);
test2 = custom_malloc(10);

Then test1 is freed:

custom_free(test1);

What this does is simply mark that "node" as free. The "node" in this case refers to the
allocated space, INCLUDING the header. "node" means ALL 26 bytes. To mark it as free, 
the custom_free function simply changes used to equal 0 (as in false).

Okay, so the program then calls 

test3 = custom_malloc(8);

If custom_malloc is a written poorly using best fit, then it will give the first
24 bytes of the free "node" to the user. This is 16 bytes for the header, plus
8 actual usable bytes, as requested.

BUT THAT ONLY LEAVES 2 BYTES LEFT!

So now there is a choice:
A) Try to create a new header in the leftover space, which OVERWRITES AN EXISTING HEADER
AND CORRUPTS THE LINKED-LIST
B) Realize that the space left is less than 16 bytes, and simply exclude it. AKA LEAK MEMORY.

Okay, so the easiest way to solve this is to make it so there is a MINIMUM INTERNAL ALLOCATION SIZE.

This means that custom_malloc should ONLY try to to create new "nodes" if there is 17 or more
bytes available. 16 bytes for the header + 1 usable for whatever called custom_malloc.

So in the scenario above, either:
A) It would LEAK MEMORY.
B) It would notice that there are ONLY 2 bytes left, and decide NOT to use the first 24, since
it doesn't leave AT LEAST 17 remaining.

The logical choice is to use option B. Why would you want custom_malloc to essentially ignore and
lose track of memory space? There would also be the problem of how to determines lost space, etc..
##################################################


##################################################
>>>>> So the problem is solved, right? <<<<<

Yes....but not correctly.

This is how it was designed to be solved for Misurda's CS0449 class.
This is how it was designed to be solved to make sure the grading driver works.

But it does not work in the most efficient and logical manner!
##################################################


##################################################
>>>>> So what did I do??? <<<<<

Instead of a minimum "node" size of 17 bytes, or leaking memory, or anything like that, I chose
to have custom_malloc use an ALLOCATION SIZE aka CHUNK SIZE or any other name it might be called.

My chunk size is 32 bytes. 16 bytes for the header, 16 bytes usable space.

If the user does this:

test1 = custom_malloc(10);

custom_malloc ACTUALLY increases heap size by 32 bytes.

Let us, once again, take the scenario where custom_malloc is called twice.

test1 = custom_malloc(10);
test2 = custom_malloc(10);

This means there are two "nodes" with size 32 bytes in the heap.

custom_free(test1);

would then free the first one.

test3 = custom_malloc(8);

would then use the first free "node" and NOT SPLIT IT!

INTERNALLY, CUSTOM_MALLOC KNOWS THAT 32 BYTES WAS ALLOCATED IN MEMORY. The user just cares that
AT LEAST 8 bytes is allocated.
##################################################


##################################################
>>>>> Why do it this way? <<<<<

Reasons for doing it this way:
1) It does not leak memory.
2) It does not lose track of memory.
3) custom_malloc ALIGNS memory with the pages/allocations/etc of the UNDERLYING OS

The third reason is most important!

That means that this custom_malloc function will be:
>>> faster
>>> more efficient
>>> less wasteful
than any other implementation!

32 bytes is a power of 2. It will align correctly. 17 bytes will not!

##################################################
