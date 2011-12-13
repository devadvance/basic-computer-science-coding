Matt Joseph
mgj7@pitt.edu
University of Pittsburgh
CS447 Project 3


>>>>> How To Use <<<<<
Load the necesary instructions to ROM and data to RAM. Enable the simulation.
To start the processor, enable the clock.
To reset, use the built-in reset option in Logisim.


>>>>> General Info <<<<<

My version of the JrMIPS processor takes the following approach:
> High number of wires but minimum number of complex subcircuits.
> Most subcircuits are single-purpose so as to enable quick additions to the circuit.
> There is only one (1) ALU. This ALU is used for arithmetic and branching.
> Shifting, jumps, put, and halt are all done right in the main circuit.

LED and Hex displays are in the bottom right. Bottom left is PC.

Logic circuits include:
ALU_Control - Determines the ALU operation and carryin/inverts/etc
I_OR_R - Determines the instruction type
put_or_halt - Checks if its a put or halt instruction
RegWrite - Enables/Disables register writing depending on the instruction
branchType - If it is a branch, this determines which type (00 is bz, 01 is bx, 10 is bp, and 11 is bn)
MemOpType - Determines the memory operation type (lw or sw)
MainOpType - Determines what type of op it is:
	00 -> Arithmetic/Branch
	01 -> Memory (lw/sw)
	10 -> Shifts
	11 -> Jumps
ShiftLog - Determines the type of shift and what registers are involved
JumpLogic - Determines the type of jump
PC_Update_Type - Determines how the PC is going to be changed (jump/+1/etc)

Muxes/Demuxes are used a lot. This is because I split up all of the logic into distinct parts.


>>>>> Complexity <<<<<

This processor is seemingly complex, and in some cases, over-complex. 
The reasoning for this is because I chose to fully implement each type of instruction.
This includes the datapath and control signals, so there are multiple control paths that
end up being "layered" in a sense. I also avoided the use of subcircuits as it cuts down on
the ability to quickly glance and see what wires are active and such. This is also the reasoning
behind the use of splitters that split into 16 wires rather than selective splitters or bit
selectors. I personally like being able to see the data/control paths very well.


>>>>> What Works <<<<<
All instructions listed on the project3.pdf file should work.
This includes:
add, sub, addi, addiu, nor, and, sllv, srlv, sll, srl, lw, sw, halt, bz, bx, bp, bn, jal, jr, j, put.


>>>>> Known Issues/Quirks <<<<<
> Before the processor is started, most circuits show error/underfined. Does not affect operation.
> When a halt signal occurs, the clock continues to run, and this signal reachs other units on
the board. This causes an "oscillation" error. However, the processor does halt, and the LED does,
in fact, light up. It seems to me that if the error did not occur, nothing would happen.

