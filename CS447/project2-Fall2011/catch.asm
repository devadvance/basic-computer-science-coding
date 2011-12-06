# Copyright (c) 2011, Matt Joseph
# All rights reserved.
# 
# Distributed under the New BSD License.
# Full contents of the license and its stipulations can be found in README.txt.
# By using this code, it is assumed that this license has been understood and agreed to.
# 
# University of Pittsburgh
# CS447
# Project 2
# Modified: 2011-11-11 @ 23:35
# 
# Note: Tested successfully with MARS 4.1 (Mips Assembler and Runtime Simulator)
# 

# Begin data section
# ##################################################
.data
	leftflies: .word -1,-1,-1,-1,-1,-1,-1,-1
	rightflies: .word -1,-1,-1,-1,-1,-1,-1,-1
	number_of_flies: .word 0
	time_start: .word 0
	endingMessage: .asciiz "\n\nGame finished. Your score was "
	forfeitMessage: .asciiz "\n\nGame forfeited. Your score was "
	periodAndEnter: .asciiz ".\n\n"
	
# Begin text section
# ##################################################
.text

	# Begin by jumping to the main procedure, located at the bottom
	j main
# Begin procedure section
# These procedures cannot be accessed unless explicitly called from somewhere else
# ##################################################

# #########################
# void _setLED(int x, int y, int color)
#   sets the LED at (x,y) to color
#   color: 0=off, 1=red, 2=orange, 3=green
#
# warning:   x, y and color are assumed to be legal values (0-127,0-7,0-3)
# arguments: $a0 is x, $a1 is y, $a2 is color
# trashes:   $t0-$t3
# returns:   none
#
# #########################
_setLED:
	# byte offset into display = y * 32 bytes + (x / 4)
	sll	$t0,$a1,5      # y * 32 bytes
	srl	$t1,$a0,2      # x / 4
	add	$t0,$t0,$t1    # byte offset into display
	li	$t2,0xffff0008	# base address of LED display
	add	$t0,$t2,$t0    # address of byte with the LED
	# now, compute led position in the byte and the mask for it
	andi	$t1,$a0,0x3    # remainder is led position in byte
	neg	$t1,$t1        # negate position for subtraction
	addi	$t1,$t1,3      # bit positions in reverse order
	sll	$t1,$t1,1      # led is 2 bits
	# compute two masks: one to clear field, one to set new color
	li	$t2,3		
	sllv	$t2,$t2,$t1
	not	$t2,$t2        # bit mask for clearing current color
	sllv	$t1,$a2,$t1    # bit mask for setting color
	# get current LED value, set the new field, store it back to LED
	lbu	$t3,0($t0)     # read current LED value	
	and	$t3,$t3,$t2    # clear the field for the color
	or	$t3,$t3,$t1    # set color field
	sb	$t3,0($t0)     # update display
	jr	$ra
# #########################
# End _setLED
# #########################
	
	
# #########################	
# int _getLED(int x, int y)
#   returns the value of the LED at position (x,y)
#
#  warning:   x and y are assumed to be legal values (0-127,0-7)
#  arguments: $a0 holds x, $a1 holds y
#  trashes:   $t0-$t2
#  returns:   $v0 holds the value of the LED (0 or 1)
#
# #########################
_getLED:
	# byte offset into display = y * 32 bytes + (x / 4)
	sll  $t0,$a1,5      # y * 32 bytes
	srl  $t1,$a0,2      # x / 4
	add  $t0,$t0,$t1    # byte offset into display
	la   $t2,0xffff0008
	add  $t0,$t2,$t0    # address of byte with the LED
	# now, compute bit position in the byte and the mask for it
	andi $t1,$a0,0x3    # remainder is bit position in byte
	neg  $t1,$t1        # negate position for subtraction
	addi $t1,$t1,3      # bit positions in reverse order
    	sll  $t1,$t1,1      # led is 2 bits
	# load LED value, get the desired bit in the loaded byte
	lbu  $t2,0($t0)
	srlv $t2,$t2,$t1    # shift LED value to lsb position
	andi $v0,$t2,0x3    # mask off any remaining upper bits
	jr   $ra
# #########################
# End _getLED
# #########################

# #########################
# makeRandomFly
# Creates a random fly somewhere on the board
# Does nothing if there are already 16 flies
# #########################
makeRandomFly:
	addi $sp, $sp, -4
        sw $ra, 0($sp)
	
	lw $t7, number_of_flies
	bge $t7, 16, end_makeRandomFly # DO NOTHING if there already 16 flies
	bne $t7, 15, randomFlyLoop # Jump down if there are less than 15 flies
	
Case15flies: # In the case that there are already 15 flies. Faster than doing random
	
	addi $a1, $0, -1
	la $t1, leftflies
	addi $t1, $t1, -4
Case15FlyLoopLeft: # Just loop through and find the one empty spot, if on the left side.
	addi $a1, $a1, 1
	beq $a1, 8, end_Case15FlyLoopLeft
	
	addi $t1, $t1, 4
	lw $a0, 0($t1) # Load word from the location (x location)
	
	bne $a0, -1, Case15FlyLoopLeft
	move $a0, $a1 # 
	addi $t7, $0, 4 # x location for new fly
	
	j doneSide1
end_Case15FlyLoopLeft: #AKA didn't fine the empty spot

	addi $a1, $0, -1
	la $t1, rightflies
	addi $t1, $t1, -4
Case15FlyLoopRight: # Just loop through and find the one empty spot, if on the right side.
	addi $a1, $a1, 1
	beq $a1, 8, end_Case15FlyLoopRight
	
	addi $t1, $t1, 4
	lw $a0, 0($t1) # Load word from the location (x location)
	
	bne $a0, -1, Case15FlyLoopRight
	
	addi $t7, $0, 124 # x location for new fly
	move $a0, $a1 # 
	j doneSide1
end_Case15FlyLoopRight:

	j end_makeRandomFly
	
randomFlyLoop:
	# The following code gets a random number 0 through 15
	li $v0, 42 # Syscall 42 to get a random number in a range
	li $a0, 1 # Set a0 to the random number generator number (always 1 for this program)
	li $a1, 16 # Upper bounds of random number. 0-15 range. Non-inclusive 16
	syscall # Get random number 0-15. NOTE, a0 still has 1 from last syscall
	
	bge $a0, 8, rightSide1
leftSide1:
	la $t3,leftflies # Load the address of word_array into t3
	la $t2, ($a0) # Load the address of a0 to t2
	add $t2, $t2, $t2 # Double the index
	add $t2, $t2, $t2 # Double the index again (now 4x). This makes it into bytes
	add $t1, $t2, $t3 # Offset from the start of leftflies by the amount in t2. t3 contains the address of leftflies array
	addi $t7, $0, 4 # x location for new fly
	j doneSide1
rightSide1:
	addi $a0, $a0, -8
	la $t3,rightflies # Load the address of word_array into t3
	la $t2, ($a0) # Load the address of a0 to t2
	add $t2, $t2, $t2 # Double the index
	add $t2, $t2, $t2 # Double the index again (now 4x). This makes it into bytes
	add $t1, $t2, $t3 # Offset from the start of rightflies by the amount in t2. t3 contains the address of rightflies array
	addi $t7, $0, 124 # x location for new fly
doneSide1:
	move $a1, $a0 # Set a1 to a0 (y location)
	lw $a0, 0($t1) # Load word from the location (x location)
	
	bne $a0, -1, alreadyFly # If there's already a fly in this location
	move $a0, $t7
	sw $t7, ($t1)
	addi $a2, $0, 2 # Color of orange
	jal _setLED
	lw $t7, number_of_flies
	addi $t7, $t7, 1
	sw $t7, number_of_flies
	j end_makeRandomFly
alreadyFly:
	j randomFlyLoop # Try again
end_makeRandomFly:
	
	lw $ra, 0($sp)
	addi $sp, $sp, 4
	jr $ra
# #########################
# End makeRandomFly
# #########################


# #########################
# moveFlies procedure
# Moves the flies on the board. Does only one row at a time
# Moves one row every 50ms
# #########################
moveFlies:
	addi $sp, $sp, -4
	sw $ra, 0($sp)
	
	li $v0, 30
	syscall # Get the current system time
	
	sub $a0, $a0, $s7
	
	blt $a0, 50, end_moveFlies
	
moveLeftFlies:
	la $t4, leftflies
	sll $t5, $s6, 2
	add $t4, $t4, $t5
	
	lw $t5, ($t4)
	beq $t5, -1, end_moveLeftFlies
	
	# Set black LED
	move $a0, $t5
	move $a1, $s6
	addi $a2, $0, 0
	jal _setLED
	
	# Set next yellow LED
	addi $t5, $t5, 1
	move $a0, $t5
	move $a1, $s6
	addi $a2, $0, 2
	jal _setLED
	
	sw $t5, ($t4)
	bne $t5, 64, end_moveLeftFlies # See if its in the middle
	j endGame
	
end_moveLeftFlies:
	
moveRightFlies:
	la $t4, rightflies
	sll $t5, $s6, 2
	add $t4, $t4, $t5
	
	lw $t5, ($t4)
	beq $t5, -1, setNewTimes
	
	# Set black LED
	move $a0, $t5
	move $a1, $s6
	addi $a2, $0, 0
	jal _setLED
	
	# Set next yellow LED
	addi $t5, $t5, -1
	move $a0, $t5
	move $a1, $s6
	addi $a2, $0, 2
	jal _setLED
	
	sw $t5, ($t4)
	bne $t5, 64, setNewTimes # See if its in the middle
	j endGame
	
setNewTimes:
	li $v0, 30
	syscall # Get the current system time
	move $s7, $a0 #s7 is designated as the start time register
	
end_moveRightFlies:
	
	bne $s6, 7, end_moveRightFlies2
	addi $s6, $0, -1 # Reset back to -1 so that it incremements to row 0
end_moveRightFlies2:
	addi $s6, $s6, 1 # Increment row counter
	
end_moveFlies: # End of procedure
	lw $ra, 0($sp)
	addi $sp, $sp, 4
	jr $ra
# #########################
# end moveFlies procedure
# #########################

# #########################
# endGame
# End the game if a fly reaches the middle.
# #########################
endGame:

	la $a0, endingMessage
	li $v0, 4
	syscall
	
	move $a0, $s4
	li $v0, 1
	syscall
	
	la $a0, periodAndEnter
	li $v0, 4
	syscall
	
	j exit
# #########################
# End endGame
# #########################

# #########################
# forfeitGame
# Procedure if the 'b' button is pressed to quit the game
# #########################
forfeitGame:

	la $a0, forfeitMessage
	li $v0, 4
	syscall
	
	move $a0, $s4
	li $v0, 1
	syscall
	
	la $a0, periodAndEnter
	li $v0, 4
	syscall
	
	j exit
# #########################
# End forfeitGame
# #########################

# #########################
# keyPressed
# Polling procedure to see if any key was pressed.
# If so, handle accordingly.
# #########################
keyPressed:
	addi $sp, $sp, -12
	sw $ra, 0($sp)
	sw $s0, 4($sp)
	sw $s1, 8($sp)
	
	li $t0, 0xFFFF0000
	lw $s0, ($t0)
	
	bne $s0, 1, end_keyPressed # Branch if a key was NOT pressed
	
	# If a key was pressed, continue
	li $t0, 0xFFFF0004 # Get the key value
	lw $s1, ($t0)
	
ifUp: # If the key pressed was up
	bne $s1, 0xE0, elseifDown
	
	addi $a0, $0, 64
	move $a1, $s5
	addi $a2, $0, 0
	jal _setLED
	
	addi $s5, $s5, -1
	
	bne $s5, -1, newUpPosition # If the frog position is already 0, meaning the top of the screen
	addi $s5, $0, 7
	
newUpPosition:
	addi $t5, $t5, 1
	addi $a0, $0, 64
	move $a1, $s5
	addi $a2, $0, 3
	jal _setLED
	
	j doneStatements
elseifDown: # Else if the key pressed was down
	bne $s1, 0xE1, elseifLeft
	
	addi $a0, $0, 64
	move $a1, $s5
	addi $a2, $0, 0
	jal _setLED
	
	addi $s5, $s5, 1
	
	bne $s5, 8, newDownPosition # If the frog position is already 7, meaning the bottom of the screen
	addi $s5, $0, 0
	
newDownPosition:
	addi $t5, $t5, 1
	addi $a0, $0, 64
	move $a1, $s5
	addi $a2, $0, 3
	jal _setLED
	
	j doneStatements
elseifLeft: # Else if the key pressed was left
	bne $s1, 0xE2, elseifRight
	jal tongueLeft
	
	j doneStatements
elseifRight: # Else if the key pressed was right
	bne $s1, 0xE3, elseifB
	jal tongueRight
	
	j doneStatements
elseifB: # Else if the key pressed was 'b'
	bne $s1, 0x42, doneStatements
	
	jal forfeitGame
	
doneStatements: # Done checking keys
	
end_keyPressed:
	lw $ra, 0($sp)
	lw $s0, 4($sp)
	lw $s1, 8($sp)
	addi $sp, $sp, 12
	jr $ra
	
# #########################
# End keyPressed
# #########################
	
# #########################
# tongueLeft
# Draws the tongue line in LEDs if the left button is pressed
# #########################
tongueLeft:
	addi $sp, $sp, -12
	sw $ra, 0($sp)
	sw $s0, 4($sp)
	sw $s1, 8($sp)
	
	la $t6, leftflies
	# Frog row
	sll $t5, $s5, 2 # array offset
	add $t6, $t6, $t5
	lw $t5, ($t6) # t5 contains the x position of fly, if any
	
	addi $s0, $0, 63
	
tongueLeftLoop1: # Put out tongue
	beq $s0, 23, end_tongueLeftLoop1_noFly
	
	# Set a red LED
	move $a0, $s0
	move $a1, $s5
	addi $a2, $0, 1
	jal _setLED
	
	beq $s0, $t5, end_tongueLeftLoop1_Fly # If a fly was hit, end the loop prematurely
	
	addi $s0, $s0, -1
	j tongueLeftLoop1
	
end_tongueLeftLoop1_Fly:
	addi $t7, $0, -1
	sw $t7, ($t6) # update fly info
	
	addi $s4, $s4, 1 # Add to score
	
	# Subtract from the number of existing flies
	lw $t7, number_of_flies
	addi $t7, $t7, -1
	sw $t7, number_of_flies
	
	jal makeRandomFly
	jal makeRandomFly
	
end_tongueLeftLoop1_noFly:
	
	li $v0, 30
	syscall # Get the current system time
	move $t7, $a0
	
pauseLoopLeft:
	
	li $v0, 30
	syscall # Get the current system time
	
	sub $a0, $a0, $t7
	bgt $a0, 100, end_pauseLoopLeft
	
	j pauseLoopLeft
	
end_pauseLoopLeft:
	
tongueLeftLoop2: # Retract tongue
	beq $s0, 64, end_tongueLeftLoop2
	
	move $a0, $s0
	move $a1, $s5
	addi $a2, $0, 0
	jal _setLED
	
	addi $s0, $s0, 1
	j tongueLeftLoop2
	
end_tongueLeftLoop2:
	
	lw $ra, 0($sp)
	lw $s0, 4($sp)
	lw $s1, 8($sp)
	addi $sp, $sp, 12
	jr $ra
# #########################
# End tongueLeft
# #########################	
	
	
	
# #########################
# tongueRight
# Draws tongue right if the right button is pressed.
# #########################
tongueRight:
	addi $sp, $sp, -12
	sw $ra, 0($sp)
	sw $s0, 4($sp)
	sw $s1, 8($sp)
	
	la $t6, rightflies
	# Frog row
	sll $t5, $s5, 2 # array offset
	add $t6, $t6, $t5
	lw $t5, ($t6) # t5 contains the x position of fly, if any
	
	addi $s0, $0, 65
	
tongueRightLoop1: # Put out tongue
	beq $s0, 105, end_tongueRightLoop1_noFly
	
	# Set a red LED
	move $a0, $s0
	move $a1, $s5
	addi $a2, $0, 1
	jal _setLED
	
	beq $s0, $t5, end_tongueRightLoop1_Fly # If a fly was hit, end the loop prematurely
	
	addi $s0, $s0, 1
	j tongueRightLoop1
	
end_tongueRightLoop1_Fly: # If a fly was hit
	addi $t7, $0, -1
	sw $t7, ($t6) # update fly info
	
	addi $s4, $s4, 1 # Add to score
	
	# Subtract from the number of existing flies
	lw $t7, number_of_flies
	addi $t7, $t7, -1
	sw $t7, number_of_flies
	
	jal makeRandomFly
	jal makeRandomFly
	
end_tongueRightLoop1_noFly:
	
	li $v0, 30
	syscall # Get the current system time
	move $t7, $a0
	
pauseLoopRight:
	
	li $v0, 30
	syscall # Get the current system time
	
	sub $a0, $a0, $t7
	bgt $a0, 100, end_pauseLoopRight
	
	j pauseLoopRight
	
end_pauseLoopRight:
	
tongueRightLoop2: # Retract tongue
	beq $s0, 64, end_tongueRightLoop2
	
	move $a0, $s0
	move $a1, $s5
	addi $a2, $0, 0
	jal _setLED
	
	addi $s0, $s0, -1
	j tongueRightLoop2
	
	
end_tongueRightLoop2:
	
	lw $ra, 0($sp)
	lw $s0, 4($sp)
	lw $s1, 8($sp)
	addi $sp, $sp, 12
	jr $ra
# #########################
# End tongueRight
# #########################


# Start of the main procedure
# ##################################################
	
# #########################
# main
# Beginning of the actual main code
# Loops while the game is still going on
# The end game conditions are handled elsewhere
# #########################
main:
	# Start by setting up the random number generator
	li $v0, 30
	syscall # Get the current system time
	
	# Set up the random number generator
	move $a1, $a0 # Move current time from a0 to a1
	li $a0, 1 # Set a0 to the random number generator number
	li $v0, 40 # Syscall 40 to seed random number generator
	syscall # Seed random generator 1
	
	li $v0, 30
	syscall # Get the current system time
	move $s7, $a0 #s7 is designated as the start time register :)
	
	addi $a0, $0, 64
	addi $a1, $0, 4
	addi $a2, $0, 3
	jal _setLED
	
	addi $s5, $0, 4 # Frog position register
	addi $s4, $0, 0 # Score register
	
	jal makeRandomFly
gameLoop:
	jal keyPressed
	jal moveFlies
	
	j gameLoop

# #########################
# End main
# #########################

# Exit
# ##################################################

# #########################
# Exit the program
# #########################
exit:
	li $v0, 10
	syscall
