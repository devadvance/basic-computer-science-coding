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
CS447
Project 1
Modified: 2011-10-09 @ 20:00

Note: Tested successfully with MARS 4.1 (Mips Assembler and Runtime Simulator)
The above is the New BSD License, under which this file, and the code located scramble.asm, are released.

# ########################################################################### #
# ########################################################################### #

>>> Logic behind the program <<<

The program is set up with the following structure:

1) Data section
2) First run
	-Chooses the word "computer" as the first word every time, and skips the random word picker for the first run.
3) Random word picking
	-This only occurs during any run that is NOT the first run. Uses a random number generator to pick a random word out of the 10 available ones
4) original_word_pointer is set to point to the un-permuted, untouched original word in the word_array. Essentially, it's a char pointer
5) Goes to len procedure to compute the length of the word. This is then stored in memory as word_length
6) Copies the original word to the selected_word location in memory, byte by byte. This leaves the original word untouched. selected_word is null-terminated to indicated that it is a string.
7) Permutes the selected_word using the algorithm given for the project. Uses the random number generator. 
	-Uses http://en.wikipedia.org/wiki/Fisher-Yates_shuffle as suggested for the project.
8) Makes output_word into a string of blanks. Originally, it is just a space in memory. This space is filled with the appropriate number of blanks, and then null-terminated to indicated that it is a string.
9) The actual game loop begins here.
	A) All of the messages are already stored in memory. Start by printing some of them out.
	B) Set the original score.
	C) Shows output_word by going to the print_output_word procedure. This procedure prints output_word with spaces in between all characters.
	C) Gets character input from the user.
		-If the user enters a period, the round is forfeited, and the score for the round is zero.
	D) Uses check_letter prodecure to check for a match or matches. If none are found, it returns zero. If some are found, it returns that number to save it as the number of characters found so far.
		-It also can return -1 if the letter has already been guessed. This avoids some logic issues and gives the user some leeway.
	E) If the score hits zero or the user guesses all the letters, the round ends.
	F) Add the round score to the final score.
	G) Asks the user if they want to play again.
		-If yes, redo the whole game by going to the top, picking a random letter, and starting everything over.
		-If no, print the final score (sum of all the rounds) and then quit.

>>> Bugs <<<

There are no known bugs. There are a couple of situations (upper case characters, invalid input, etc) that are no accounted for, but these were not required by the assignment, and therefore the "exceptions" were not handled.

>>> Notes <<<

The program assembles and runs properly by all accounts. Things to note:
1) When the program asks the user if they want to play again, any character other than 'y' will quit the game. This is done rather than code in a multiline loop in order to check user input.
2) It is assumed that the user will only guess lower case letters and the period, as stated in the project instructions.
3) There may be a couple instances where code is somewhat unoptimized or redundant.

>>> Sample Run <<<


Welcome to Scramble!

I am thinking of a word. The word is _ _ _ _ _ _ _ _. Score is 8.
Guess a letter?
c
Yes! The word is _ _ _ _ _ _ c _. Score is 8.
Guess a letter?
o
Yes! The word is _ _ _ o _ _ c _. Score is 8.
Guess a letter?
m
Yes! The word is _ _ _ o _ m c _. Score is 8.
Guess a letter?
p
Yes! The word is _ _ _ o _ m c p. Score is 8.
Guess a letter?
u
Yes! The word is _ u _ o _ m c p. Score is 8.
Guess a letter?
t
Yes! The word is t u _ o _ m c p. Score is 8.
Guess a letter?
e
Yes! The word is t u _ o e m c p. Score is 8.
Guess a letter?
m
This letter was already found!
The word is t u _ o e m c p. Score is 8.
Guess a letter?
t
This letter was already found!
The word is t u _ o e m c p. Score is 8.
Guess a letter?
z
No! The word is t u _ o e m c p. Score is 7.
Guess a letter?
z
No! The word is t u _ o e m c p. Score is 6.
Guess a letter?
x
No! The word is t u _ o e m c p. Score is 5.
Guess a letter?
r
Yes! Round is over. Your final guess was:
t u r o e m c p
Correct unscrambled word was:
computer
Do you want to play again (y/n)?
y


I am thinking of a word. The word is _ _ _ _ _ _ _ _ _ _. Score is 10.
Guess a letter?
r
Yes! The word is _ _ _ _ _ _ r _ _ _. Score is 10.
Guess a letter?
i
No! The word is _ _ _ _ _ _ r _ _ _. Score is 9.
Guess a letter?
t
Yes! The word is _ _ _ _ t _ r _ _ _. Score is 9.
Guess a letter?
w
No! The word is _ _ _ _ t _ r _ _ _. Score is 8.
Guess a letter?
y
Yes! The word is _ _ _ _ t y r _ _ _. Score is 8.
Guess a letter?
u
No! The word is _ _ _ _ t y r _ _ _. Score is 7.
Guess a letter?
n
No! The word is _ _ _ _ t y r _ _ _. Score is 6.
Guess a letter?
m
Yes! The word is _ _ _ _ t y r _ m _. Score is 6.
Guess a letter?
o
Yes! The word is o _ _ _ t y r o m _. Score is 6.
Guess a letter?
l
Yes! The word is o _ _ _ t y r o m l. Score is 6.
Guess a letter?
h
No! The word is o _ _ _ t y r o m l. Score is 5.
Guess a letter?
a
No! The word is o _ _ _ t y r o m l. Score is 4.
Guess a letter?
j
No! The word is o _ _ _ t y r o m l. Score is 3.
Guess a letter?
e
Yes! The word is o _ e _ t y r o m l. Score is 3.
Guess a letter?
h
No! The word is o _ e _ t y r o m l. Score is 2.
Guess a letter?
c
Yes! Round is over. Your final guess was:
o c e c t y r o m l
Correct unscrambled word was:
motorcycle
Do you want to play again (y/n)?
n
Your final score is 7
-- program is finished running (dropped off bottom) --