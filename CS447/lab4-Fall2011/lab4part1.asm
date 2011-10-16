# Copyright (c) 2011, encryptstream
# All rights reserved.
# 
# Distributed under the New BSD License.
# Full contents of the license and its stipulations can be found in README.txt.
# By using this code, it is assumed that this license has been understood and agreed to.
# 
# University of Pittsburgh
# CS447
# Lab 4 Part 1
# 
# Note: Tested successfully with MARS 4.1 (Mips Assembler and Runtime Simulator)
# 

#CS/COE 447 Lab 4 Part 1 Template

#This template includes testing code, but also has some support code to check
#for a common error.

.text:
        #This is the beginning of the testing code. You should not need to alter this.

        li $a0, 0xFFFF0008      #LED memory starts at this address
        li $a1, 0x95FAAF56      #Pattern to draw. It will then be disrupted.
        jal drawPattern         #Jump and link to drawPattern, to draw an
                                #initial pattern on the display.

        #Jump and link to disruptPattern. This call should alter the display by
        #disrupting the pattern that was drawn via drawPattern. This will occur
        #so fast that you will not see the original pattern that was drawn.
        li $a0, 0xFFFF0008      #LED memory starts at this address
        jal disruptPattern

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
# * Place your getPattern code here     *
#========================================
getPattern:
lw $v0, 0($a0)
jr $ra



#========================================
# * DO NOT ALTER THIS NEXT LINE         *
j returnErrorHappened
#========================================




#========================================
# * Place your disruptPattern code here *
#========================================
    disruptPattern:
        #TODO: Fill this function in.
        #This function should make use of your drawPattern and your
        #getPattern function.
        move $t7,$ra
        jal getPattern
        li $t1, 0x12345678
        xor $a1, $v0, $t1
        jal drawPattern
        move $ra, $t7
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