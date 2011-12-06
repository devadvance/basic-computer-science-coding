.text
    j main

    registerToString:
        #TODO: FILL IN THIS FUNCTION
        #$a0 = an integer
        #$a1 = address of a character buffer
		
		addi $t0, $0, 32 # Counter
		
	L1:
		beq $t0, $0, L1_exit
		
		move $t1, $a0
		li $t2, 0x80000000
		and $t1, $t1, $t2
		
		beq $t1, $0, if_zero
		
	if_one:
		li $t1, 49
		sb $t1, ($a1)
		j continue
		
	if_zero:	
		li $t1, 48
		sb $t1, ($a1)
		
	continue:	
		sll $a0, $a0, 1
		addi $t0, $t0, -1
		addi $a1, $a1, 1
	
		j L1
		
		
	L1_exit:
		li $t1, 0
		sb $t1, ($a1)

        jr $ra


    #============================================== 
    #Do NOT edit the rest of the code in this file.
    #============================================== 

    main: #
        jal setRegisterStates

        la $a0, prompt
        li $v0, 4
        syscall

        li $v0, 5           #Ask the user for a number, n.
        syscall

        move $a0, $v0       #Copy user's typed in integer into $a0.
        la $a1, str

        jal registerToString #Compute registerToString(n, str)

        la $a0, result_str
        li $v0, 4
        syscall

        la $a0, str
        li $v0, 4
        syscall

        jal checkRegisterStates

        li $v0, 10          #Exit
        syscall

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
    #Normally we would declare str as, str: .space 33, as that would allocate 33
    #bytes, all initially set to 0x00.

    #But, to help you more easily find bugs, instead I'll declare it with a
    #value already. If any of the old value gets printed, you'll know that your
    #code did not work exactly right.
    str: .asciiz "!@#$%^&*()!@#$%^&*()!@#$%^&*()!@#"

    .align 2
    old_sp_value:   .word 0
    failed_check:   .asciiz "One or more registers was corrupted by your code.\n"

    prompt:         .asciiz "Enter a number: "
    result_str:     .asciiz "In binary your number is:\n"