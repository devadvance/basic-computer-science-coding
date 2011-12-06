.text
    j main

    sumOfFactorials:
        #TODO: FILL IN THIS FUNCTION
        #Your sumOfFactorials should call _fact (e.g., "jal _fact").
        #Your sumOfFactorials must follow calling conventions.
		
		# prologue to procedure
		addi    $sp,$sp,-8        # push space for activation frame
		sw    $s0,0($sp)        # save $s0, which we use
		sw    $ra,4($sp)        # save return address
		
		# Check to see if its the base case (a0 is 1)
		li $v0, 1
		beq $v0, $a0, sumOfFactorials_exit
	
		# Central procedure
		move $s0, $a0
		jal _fact
		
		addi $a0, $s0, -1
		
		move $s0, $v0
		jal sumOfFactorials
		
		add $v0, $s0, $v0
		
		
	sumOfFactorials_exit:
		# epilogue to exit procedure
		lw    $ra,4($sp)        # restore $ra
		lw    $s0,0($sp)        # restore $s0
		addi    $sp,$sp,8        # pop activation frame
        jr $ra


    #============================================== 
    #Do NOT edit the rest of the code in this file.
    #============================================== 

    main: #
        jal setRegisterStates

        li $v0, 5           #Ask the user for a number, n.
        syscall

        move $a0, $v0       #Copy user's typed in integer into $a0.

        jal sumOfFactorials #Compute sumOfFactorials(n)

        move $a0, $v0       #Print return value.
        li $v0, 1
        syscall

        jal checkRegisterStates

        li $v0, 10          #Exit
        syscall

#This function copied from:
# http://www.cs.pitt.edu/~childers/CS0447/examples/factorial.asm

_fact:
    # prologue to procedure
    addi    $sp,$sp,-8        # push space for activation frame
    sw    $s0,0($sp)        # save $s0, which we use
    sw    $ra,4($sp)        # save return address
    # start of actual procedure work
    move    $s0,$a0            # get argument ($a0)
    li    $v0,0x1            # 1
    beq    $s0,$v0,_fact_exit    # end of recursion (f==1?)
    addi    $a0,$s0,-1        # f /= 1, so continue. set up arg(f-1)
    jal    _fact            # recursive call 
    mult    $v0,$s0            # multiply
    mflo    $v0            # return mul result
_fact_exit:
    # epilogue to exit procedure
    lw    $ra,4($sp)        # restore $ra
    lw    $s0,0($sp)        # restore $s0
    addi    $sp,$sp,8        # pop activation frame
    jr    $ra            # return


setRegisterStates:
    li $s0, -1
    li $s1, -1
    li $s2, -1
    li $s3, -1
    li $s4, -1
    li $s5, -1
    li $s6, -1
    li $s7, -1
    sw $sp, old_sp_value
    sw $s0, ($sp)       #Write something at the top of the stack
    jr $ra

checkRegisterStates:

    bne $s0, -1, checkRegisterStates_failedCheck
    bne $s1, -1, checkRegisterStates_failedCheck
    bne $s2, -1, checkRegisterStates_failedCheck
    bne $s3, -1, checkRegisterStates_failedCheck
    bne $s4, -1, checkRegisterStates_failedCheck
    bne $s5, -1, checkRegisterStates_failedCheck
    bne $s6, -1, checkRegisterStates_failedCheck
    bne $s7, -1, checkRegisterStates_failedCheck

    lw $t0, old_sp_value
    bne $sp, $t0, checkRegisterStates_failedCheck

    lw $t0, ($sp)
    bne $t0, -1, checkRegisterStates_failedCheck

    jr $ra                      #Return: all registers passed the check.
    
    checkRegisterStates_failedCheck:
        la $a0, failed_check    #Print out the failed register state message.
        li $v0, 4
        syscall

        li $v0, 10              #Exit prematurely.
        syscall

.data:
    old_sp_value:   .word 0
    failed_check:   .asciiz "One or more registers was corrupted by your code.\n"