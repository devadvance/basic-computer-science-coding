# Copyright (c) 2011, encryptstream
# All rights reserved.
# 
# Distributed under the New BSD License.
# Full contents of the license and its stipulations can be found in README.txt.
# By using this code, it is assumed that this license has been understood and agreed to.
# 
# University of Pittsburgh
# CS447
# Project 1
# Modified: 2011-10-11 @ 21:15
# 
# Note: Tested successfully with MARS 4.1 (Mips Assembler and Runtime Simulator)
# 

# Begin data section
# ##################################################
.data
	welcome_msg: .asciiz "Welcome to Scramble!"
	i_am_thinking: .asciiz "I am thinking of a word. "
	the_word_is: .asciiz "The word is "
	score_is: .asciiz ". Score is "
	guess_letter: .asciiz ".\nGuess a letter?\n"
	already_found: .asciiz "This letter was already found!\n"
	yes: .asciiz "Yes! "
	no: .asciiz "No! "
	round_over: .asciiz "Round is over. Your final guess was:\n"
	correct_unscrambled: .asciiz "\nCorrect unscrambled word was:\n"
	play_again: .asciiz "\nDo you want to play again (y/n)?\n"
	final_score_phrase: .asciiz "\nYour final score is "
	goodbye: .asciiz ". Goodbye!\n"
	nl: .asciiz "\n"
	
	word_0: .asciiz "motorcycle"
	word_1: .asciiz "jelly"
	word_2: .asciiz "university"
	word_3: .asciiz "minerals"
	word_4: .asciiz "artwork"
	word_5: .asciiz "computer"
	word_6: .asciiz "mythology"
	word_7: .asciiz "calculator"
	word_8: .asciiz "apple"
	word_9: .asciiz "ridiculous"
	.align 2
	# array of pointers (addresses) to the words
	word_array: .word	word_0,word_1,word_2,word_3,word_4,word_5,word_6,word_7,word_8,word_9
	
	word_length: .word 0
	final_score: .word 0
	
	original_word_pointer: .word 0
	selected_word: .space 32
	output_word: .space 32
	
# Begin text section
# ##################################################
.text
main:
first_run: # To be used for the first run. Always uses computer as the first run
	# Print the welcome message
	la $a0, welcome_msg
	li $v0, 4
	syscall
	
	la $a0, word_5
	sw $a0, original_word_pointer
	j after_picked_word
	
# #########################
# random_word
# Picks a random word from the word_array and then uses that for the rest of the program.
# Uses t1, t2, t3, a0, v0
# Start
# #########################
random_word:
	li $v0, 30
	syscall # Get the current system time
	
	move $a1, $a0 # Move current time from a0 to a1
	li $a0, 1 # Set a0 to the random number generator number
	li $v0, 40 # Syscall 40 to seed random number generator
	syscall # Seed random generator 1
	
	li $v0, 42 # Syscall 42 to get a random number in a range
	li $a1, 10 # Upper bounds of random number. 0-9 range. Non-inclusive 10.
	syscall # Get random number 0-9. NOTE, a0 still has 1 from last syscall
	
	la $t3,word_array # Load the address of word_array into t3
	la $t2, ($a0) # Load the address of a0 to t2
	add $t2, $t2, $t2 # Double the index
	add $t2, $t2, $t2 # Double the index again (now 4x). This makes it into bytes
	add $t1, $t2, $t3 # Offset from the start of word_array by the amount in t2. t3 contains the address of word_array
	lw $a0, 0($t1) # Load word from the location pointed to by word_array plus the offset. It is a word containing a pointer
	sw $a0, original_word_pointer # Store the memory address (pointer) of the original word (THE ADDRESS! original_word_pointer is just a WORD, not an asciiz!)

after_picked_word:
	jal len # get length of string, NOT debug code!
	sw $v0, word_length # store word length

# #########################	
# The below section copies the random word to the "selected_word" memory location
# Uses t1, t2, t5, a0
# Start
# #########################
	lw $t1, original_word_pointer # set t1 to memory address of original word
	# la $t3, word_5
	la $t2, selected_word # set t2 to memory address of selected word
	
	add $a0, $0, $v0
	add $a0, $a0, $t2 # set a0 to one more than the last memory address of selected word
	
L1:
	bge $t2, $a0, exit_L1
	
	lb $t5, ($t1)
	
	sb $t5, ($t2)
	addi $t1, $t1, 1
	addi $t2, $t2, 1
	j L1

exit_L1:
	li $t5, '\0'
	sb $t5, ($t2)
	
# #########################
# The below section permutes the selected_word
# Uses t2-t7, a0-a2
# Start
# #########################
	addi $t7, $0, 2 # Set t7 to 2
	lw $t6, word_length # Set t6 to the length of the word
	li $a0, 1 # Set a0 to 1 in order to specify which random number generator
	la $a2, selected_word

L2:	
	blt $t6, $t7, exit_L2 # if t6 is less than 2 ($7) then branch
	li $v0, 42 # Syscall 42 - random number in range
	la $a1, -1($t6) # Upper bound of random number
	syscall # a0 now contans random int (r)
	
	add $t5, $a2, $t6 # go to index t6 (i)
	addi $t5, $t5, -1 # go -1 back (total of i-1)
	lb $t4, ($t5) # Load selected_word[i-1]
	add $t3, $a2, $a0 # t5 is r
	lb $t2, ($t3) # Load selected_word[r]
	sb $t2, ($t5) # Store byte into location of t5 selected_word[i-1]
	sb $t4, ($t3) # Store byte into location of t3 selected_word[r]
	
	addi $t6, $t6, -1 # Decrease t6 by 1
	
	j L2
exit_L2:

# #########################
# The below section makes output_word into the properly formatted item (right now just blanks)
# Uses a0, t2, t5
# Start
# #########################
	lw $a0, word_length # Set a0 to the length of the word
	la $t2, output_word # set t6 to memory address of selected word
	
	add $a0, $a0, $t2 # set a0 to last memory address of selected word
	
L3:
	bge $t2, $a0, exit_L3
	li $t5, '_'
	sb $t5, ($t2)
	addi $t2, $t2, 1
	j L3

exit_L3:
	li $t5, '\0'
	sb $t5, ($t2)
	

	# print new line
	la $a0, nl
	li $v0, 4
	syscall 

# #########################	
# The below section is the loop for the actual game...
# Uses
# Start
# #########################
	lw $t8, word_length # Set the initial score to the length of the word
	add $t6, $0, $t8 # Also set t6 to length for comparison later
	addi $t9, $0, 0 # Set the inital number of characters filled in to zero
	# The score and number of characters filled in will determine whether or not to terminate the loop
	
	# ###Print stuff### #
	
	# Print a new line
	la $a0, nl
	li $v0, 4
	syscall
	
	la $a0, i_am_thinking
	li $v0, 4
	syscall
L_the_word:	
	la $a0, the_word_is
	li $v0, 4
	syscall
	
	jal print_output_word # Print the output_word in the properly formatted manner.
	
	# Print the score phrase.
	la $a0, score_is
	li $v0, 4
	syscall
	
	# Print the actual score.
	move $a0, $t8
	li $v0, 1
	syscall
	
	# Print the guess_letter question.
	la $a0, guess_letter
	li $v0, 4
	syscall
	
	# Get character from the user. USER DOES NOT HAVE TO PRESS ENTER.
	li $v0, 12
	syscall
	
	move $t0, $v0 # Temporarily put the character in t0.
	
	# Print a new line
	la $a0, nl
	li $v0, 4
	syscall
	
	move $a0, $t0 # Move the character back to a0.
	
	li $t0, '.' # Load the period value for comparison.
	beq $a0, $t0, forfeit # End the game loop if the user inputs a period to forfeit.
	
	jal check_for_letter # Check the word for the existence of the character
	
	addi $t0, $0, -1 # Check to see if the letter was already guessed.
	bne $t0, $v0, continue_to_match_check
	
	# If the letter was already guessed
	la $a0, already_found
	li $v0, 4
	syscall
	
	j after_match
	
continue_to_match_check:
	bne $v0, $0, match_found # Branch if the character was not found in the word.
	
match_not_found: # If no match is found in the word.
	addi $t8, $t8, -1 # Subtract a point for a bad guess.
	la $a0, no
	li $v0, 4
	syscall
	j after_match
	
match_found: # If match is found.
	add $t9, $t9, $v0 # Add number of characters filled to total.
	la $a0, yes
	li $v0, 4
	syscall
	
after_match: # Do this after the match stuff
	bge $t9, $t6, end_game_loop
	ble $t8, $0, end_game_loop
	j L_the_word
	
	
forfeit:
	addi $t8, $0, 0	# Set the round score to zero if the user forfeited the round
	
end_game_loop:
	# Print the round over phrase.
	la $a0, round_over
	li $v0, 4
	syscall
	
	jal print_output_word
	
	la $a0, correct_unscrambled
	li $v0, 4
	syscall
	
	lw $a0, original_word_pointer
	li $v0, 4
	syscall
	
	la $a0, play_again
	li $v0, 4
	syscall
	
	lw $a0, final_score
	add $a0, $a0, $t8
	sw $a0, final_score
	
	# Get character from user
	li $v0, 12
	syscall
	
	move $t0, $v0 # Temporarily put the character in t0.
	li $t1, 'y'
	
	bne $t0, $t1, finale
not_finale:
	# Print a new line
	la $a0, nl
	li $v0, 4
	syscall
	
	j random_word
	
finale:	
	# Print final score phrase.
	la $a0, final_score_phrase
	li $v0, 4
	syscall
	
	# Print the final score.
	lw $a0, final_score
	li $v0, 1
	syscall
	
	la $a0, goodbye
	li $v0, 4
	syscall
	
	j exit
# ###########################################################################
# End of game
# ###########################################################################



# ###########################################################################
# Start of procedure section
# Cannot access this section unless called from above
# ###########################################################################

# #########################
# len (taken from lecture example and implemented here)
# Counts length of string pointetd to by a0.
# Uses t0, t1, v0, a0
# Returns length in v0.
# Start (assumes memory address to string is in a0)
# #########################
len:
	move $t0,$a0
len_loop:
	lbu $t1,0($t0)
	beq $t1,$0,len_exit
	addi $t0,$t0,1
	j len_loop
len_exit:
	sub $v0,$t0,$a0
	jr $ra

# #########################
# End len
# #########################

# #########################
# print_output_word
# Prints the output_word in the format intended to be used for the game. Includes spaces between letters/underscores.
# Uses t1, t2, a0
# Start
# #########################
print_output_word:
	lw $t1, word_length # Set t1 to the length of the word
	la $t2, output_word # set t1 to memory address of output_word
	
	add $t1, $t1, $t2 # set t1 to last memory address of output_word
	
	lb $a0, ($t2)
	li $v0, 11
	syscall
	addi $t2, $t2, 1
	
L_print_output:
	bge $t2, $t1, exit_L_print_output
	
	li $a0, ' '
	li $v0, 11
	syscall
	
	lb $a0, ($t2)
	li $v0, 11
	syscall
	
	addi $t2, $t2, 1
	
	j L_print_output

exit_L_print_output:
	jr $ra
# #########################
# End print_output_word
# #########################

# #########################
# check_for_letter
# Checks to see if there is an occurance of the letter in the word.
# Uses a0, t1, t2, t3, t4, t7
# Start (returns v0)
# #########################
	#NOTE: a0 contains letter checking for
check_for_letter:
	addi $t7, $0, 0 # Set t7 to zero. If it is zero in the end, no match was found. If it is >= 1, then a match was found.
	
	lw $t1, word_length # Set t1 to the length of the word
	la $t2, output_word # set t2 to memory address of output_word
	la $t3, selected_word # set t3 to memory address of selected_word
	
	add $t1, $t1, $t2 # set t1 to last memory address of selected word
	
	# lb $a0, ($t2)
	
L_check_for_letter:
	bge $t2, $t1, exit_L_check_for_letter
	
	lb $t4, ($t3) # Get current letter from selected_word
	
	bne $t4, $a0, check_for_letter_notmatch
	
check_for_letter_match:	
	
	
check_if_previously_found:
	lb $a3, ($t2) # Load current output_word byte to a3
	bne $a3, $a0, not_previously_found # If it has not yet been found, continue below, otherwise it has been found already
	
	# If this character has been found/guessed already, return a -1
	addi $t7, $0, -1
	j exit_L_check_for_letter
	
not_previously_found:	
	addi $t7, $t7, 1 # Add one to t7 to indicate a match was found
	
	sb $a0, ($t2)
	# NOTE: Falls through if a match is found :)	
check_for_letter_notmatch:
	
	addi $t2, $t2, 1 # Increment output_word position by 1
	addi $t3, $t3, 1 # Increment selected_word position by 1
	
	j L_check_for_letter

exit_L_check_for_letter:
	move $v0, $t7
	jr $ra
	
# #########################
# End check_for_letter
# #########################

# ###########################################################################
# End of program
# ###########################################################################

# Exit the program with syscall 10
exit:
	li $v0, 10
	syscall