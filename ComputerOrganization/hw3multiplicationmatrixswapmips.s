############################################################################# 
#############################################################################
## Assignment 3: Daniel Campos(campod2)
#############################################################################
#############################################################################

#############################################################################
#############################################################################
## Data segment
#############################################################################
############################################################################# 
    .data
matrix_a:     .word 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
matrix_b:     .word 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
swap:         .word 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0 
result:       .word 0, 0, 0, 0, 0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0
  
newline:      .asciiz "\n"
tab:          .asciiz "\t"
    

############################################################################# 
#############################################################################
## Text segment
#############################################################################
#############################################################################
  
    .text          # this is program code
    .align 2         # instructions must be on word boundaries
    .globl main        # main is a global label
    .globl matrix_swap
    .globl matrix_multiply
    .globl matrix_print

############################################################################# 
main:
#############################################################################
    # alloc stack and store $ra
    sub $sp, $sp, 4
    sw $ra, 0($sp)

    # load matrix A, swap and N into arg regs
    la $a0, matrix_a
    la $a1, swap
    li $a2, 4
    jal matrix_swap
  
    # load A, B and C and N into arg regs
    la $a0, swap
    la $a1, matrix_b
    la $a2, result
    li $a3, 4
    jal matrix_multiply

    la $a0, result
    jal matrix_print

    # restore $ra, free stack and return
    lw $ra, 0($sp)
    add $sp, $sp, 4
    jr $ra


##############################################################################
matrix_swap: 
##############################################################################
# does matrix swap for a specific 4x4 matrix like assignment 1.
# This is a leaf function and can be done without any stack allocation.
# It is ok if you use the stack.  
   			la $t0, matrix_a 	# load matrix  into $t0
			la $t1, swap		# load swap address into $t1
			li $t2, 0		# loop1 variable
			li $t4, 0		# loop2 variable
			
			
		sLoop1:
			beq $t4, 5, end_sLoop1	# runs the outer loop 4 times
		sLoop2: 
			beq $t2, 2, end_sLoop2	# the internal loop will run twice for each external loop
			lb $t3, 0($t0)		# load matrix location into $t3
			addi $t1, $t1, 8	# move swap to swap[i][j+2]
			sb $t3, 0($t1)		# store matrix in its new proper spot in swap.
			sub $t1, $t1, 8		# move swap position back
			addi $t0, $t0, 8	# move matrix to [i][j+2]
			lb $t3, 0($t0)		# load matrix two spots to right into $t3
			sb $t3, 0($t1)		# store matrix[i][j+2] in swap
			sub $t0, $t0, 8		# move matrix to original location
			addi $t0, $t0, 4	# move to next element in matrix
			addi $t1, $t1, 4	# move to next element in swap
			addi $t2, $t2, 1	# increment loop2 variable
			j sLoop2
	end_sLoop2:
			addi $t4, $t4, 1	# increment Loop1
			addi $t0, $t0, 8	# move over a row in matrix
			addi $t1, $t1, 8	# move over a row in swap
			li $t2, 0			# restare the variables
			j sLoop1
	end_sLoop1:
			
                jr $ra  
##############################################################################
matrix_multiply: 
##############################################################################
# mult matrices swap and B together of square size N and store in result.
#Prof Jeremy Johnson's code from Drexel University to write this function


        #alloc stack and store regs
        sub $sp,$sp,12    # push two words on the stack
        sw  $s0,0($sp)
        sw  $s1,4($sp)
        sw  $s2,8($sp)
        li $t7, 0
        li  $s0,0     # i counter and is set to 0
    Loop1: li  $s1,0     # j counter and is set to 0
        Loop2: li  $s2,0     # k counter and is set to 0
            inner:
                #Swap [i][k] and loaded into $t2
                mul $t0,$s0,$a3
                add $t1,$t0,$s2
                mul $t1,$t1,$a3
                add $t2,$a0,$t1
                lw $t4,($t2)#stored in $t4

                #B[k][j] computer and loaded into $t2
                mul $t0,$s2,$a3
                add $t1,$t0,$s1
                mul $t1,$t1,$a3
                add $t2,$t1,$a1
                lw $t5,($t2)#stored in $t5

                #result[k][j] computer and loaded into $s2
                mul $t6,$t4,$t5
                add $t7,$t7,$t6
                add $s2,$s2,1
            bne $s2,$a3,inner #ends the inner loop
            #multiply swap * B and store in $t1
            mul $t0,$s0,$a3#multiply swap * B and store in $t1
            add $t1,$t0,$s1
            #multiply other part added with $t1 and stored in $t2 
            mul $t1,$t1,$a3
            add $t2,$t1,$a2
            sw $t7,($t2) #saved into ($t7)
            li $t7, 0 
        add $s1,$s1,1#adds one to j counter
        bne $s1,$a3,Loop2#closed loop2
        add $s0,$s0,1 #adds one to final count in i counter
    bne $s0,$a3,Loop1#closed loop1 when conditions are with

    #restore save regs from stack
    exit: lw  $s0,0($sp)
        lw  $s1,4($sp)
        lw  $s2,8($sp)

    #free stack and return
    add $sp,$sp,12
    jr  $ra


##############################################################################
matrix_print:
##############################################################################
    # alloc stack and store regs.
    sub $sp, $sp, 16
    sw $ra, 0($sp)
    sw $s0, 4($sp)
    sw $s1, 8($sp)
    sw $a0, 12($sp)

    li $t0, 4 # size of array
    li $t1, 1 #move one point
    li $t2, 2 #move 2 bits over
    li $t5, 8 #move it 8 bits over

    la $t3, result  #load the matrix into $t3

  ##first loop
  Loop3: blt $t0,$t1, Loop4 #named loop3 since loop1 and 2 are taken but serves the purpose of first loop
        addi $t1, $t1, 1 #increase counter
      lw $t4, ($t3) #first value into t4
      addi $t3, $t3, 4 #moves to next col
      li  $v0, 1
      move  $a0, $t4    
      syscall #move
      li  $v0, 4
      la  $a0, tab  #print tab
      syscall
      j Loop3
  ##second loop
Loop4: blt $t0,$t2, NEWBREAK#second internal loop
    addi $t2, $t2, 1
    li  $v0, 4        
    la  $a0, newline  # print newline
    syscall
    li $t1, 1
j Loop4
#ending the function
   NEWBREAK:
      li  $v0, 4        
      la  $a0, newline  # print newline
      syscall #output
      lw $ra, 0($sp)
      lw $s0, 4($sp)
      lw $s1, 8($sp)
      lw $a0, 12($sp)
      add $sp, $sp, 16
      jr $ra
