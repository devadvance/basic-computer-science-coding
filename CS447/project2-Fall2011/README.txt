# ########################################################################### #
# ########################################################################### #

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
CS447
Project 2
Modified: 2011-11-11 @ 23:15

Note: Tested successfully with MARS 4.1 (Mips Assembler and Runtime Simulator)
The above is the New BSD License, under which this file, and the code located catch.asm, are released.

# ########################################################################### #
# ########################################################################### #

NOTE: Second submission because originally flies were placed at location 127 on the right, and location 0 on the left.
Rereading the guidelines, it should be 4 on the left, and 124 on the right. Fixed for the second submission, still before deadline.

>>>>> Logic behind the program <<<<<

The program is set up with the following structure:

- Main procedure loops and does the following:
	> Calls keyPressed to check if a key has been pressed.
	> Calls moveFlies to move some number of flies.
- keyPressed checks to see if a key is pressed
	> If up or down is pressed, it simply moves the green dot up or down accordingly (using _setLED)
	> If 'b' is pressed, it calls the end game procedure forfeitGame which exits the game appropriately.
	> If left or right is pressed, then either tongueLeft or tongueRight is called.
- tongueLeft and tongueRight draw the frog's tongue in the appropriate direction, until either
	a) It catches a fly
	b) It goes a length of 40 LEDs (max length)
	> If a fly is caught, 2 more are spawed using makeRandomFly, and the score is updated
	> The tongue pauses for 100ms while it is out, to make it visible to the player
- Whether or not a key is pressed, main then called moveFlies
	> Every 50ms, a row is moved inwards by one LED, if there is no fly on one or both sides, then the row is skipped, and the timer reset.
	> If the flies are moved, and one or more is now on the middle line where the frog is, the game is over, and endGame is called.


>>>>> Important procedures <<<<<

These are copied from the catch.asm file for better readability here.

# #########################
# makeRandomFly
# Creates a random fly somewhere on the board
# Does nothing if there are already 16 flies
# #########################

# #########################
# moveFlies procedure
# Moves the flies on the board. Does only one row at a time
# Moves one row every 50ms
# #########################

# #########################
# endGame
# End the game if a fly reaches the middle.
# #########################

# #########################
# forfeitGame
# Procedure if the 'b' button is pressed to quit the game
# #########################

# #########################
# keyPressed
# Polling procedure to see if any key was pressed.
# If so, handle accordingly.
# #########################

# #########################
# tongueLeft
# Draws the tongue line in LEDs if the left button is pressed
# #########################

# #########################
# tongueRight
# Draws tongue right if the right button is pressed.
# #########################

# #########################
# main
# Beginning of the actual main code
# Loops while the game is still going on
# The end game conditions are handled elsewhere
# #########################


	
>>>>> Bugs <<<<<

There are no known bugs. There are a couple of situations, such as "overlapping" colors on the LEDs, that may be see due to the loops involved, but none hinder functionality or prevent the program from executing properly.

>>>>> Notes <<<<<

For the most part, this logic is the exact same as suggested in the project2.pdf file. The timings are the same.
The only thing not implemented was moving the flies while the frog's tongue was out. Based upon how usable it is right now, it didn't seem necessary.

There may be a couple instances where code is somewhat unoptimized or redundant, such as saving 's' registers. Better safe than sorry.


Game finished. Your score was 16


-- program is finished running --


Game forfeited. Your score was 36.


-- program is finished running --
