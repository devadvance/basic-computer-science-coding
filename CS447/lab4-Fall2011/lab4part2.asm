# Copyright (c) 2011, Matt Joseph
# All rights reserved.
# 
# Distributed under the New BSD License.
# Full contents of the license and its stipulations can be found in README.txt.
# By using this code, it is assumed that this license has been understood and agreed to.
# 
# University of Pittsburgh
# CS447
# Lab 4 Part 2
# 
# Note: Tested successfully with MARS 4.1 (Mips Assembler and Runtime Simulator)
# 

#CS/COE 447 Lab 4 Part 2 Template

#This template includes testing code, but also has some support code to check
#for a common error.

.text:
        #This is the beginning of the testing code. You should not need to alter this.

        li $a0, 0xFFFF0008      #LED memory starts at this address
        li $a1, 0x95FAAF56      #The pattern to draw.
        li $a2, 7               #Draw the pattern 7 times vertically.
        jal drawRepeatedPattern #Jump and link to drawRepeatedPattern.

        la $a0, successfulQuitMessage
        li $v0, 4
        syscall

        li $v0, 10             #Exit syscall
        syscall

        #This is the end of the testing code.

#========================================
# * Place your drawPattern code here    *
#========================================
drawPattern:
sw $a1, 0($a0)
jr $ra



#========================================
# * DO NOT ALTER THIS NEXT LINE         *
j returnErrorHappened
#========================================




#========================================
# * Place drawRepeatedPattern code here *
#========================================
drawRepeatedPattern:
        move $t7,$ra
        addi $t1,$0,0
L1:
	bge  $t1,$a2,EXIT   # branch if ( $t1 >= $a2 )
	addi $t1,$t1,1      # $t1++
	jal drawPattern
	addi $a0,$a0,0x20
	j    L1               # jump back to L1 
EXIT:
	move $ra,$t7
	jr $ra


#========================================
# * DO NOT ALTER THIS NEXT LINE         *
j returnErrorHappened
#========================================


returnErrorHappened:
    #If this code is executed, your function did not properly return.
    la $a0, badReturnMessage
    li $v0, 4
    syscall
    li $v0, 10
    syscall

.data:
    badReturnMessage:       .asciiz "A function did not properly return!"
    successfulQuitMessage:  .asciiz "The program has finished."
