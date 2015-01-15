@for a comment
	.data
	@.balign 4
vara : .word 12
varb : .word 23
	.text
	.global _start
_start:
	ldr r1, =vara
	ldr r0, [r1]
	bl min_caml_print_int
	bl min_caml_print_newline

	mov r0, r0, LSL #1
	sub r0, r0, #1
	bl min_caml_print_int
	bl min_caml_print_newline

	ldr r1, =varb
	str r0, [r1]

	ldr r3, [r1]
	add r0, r3, #5
	bl min_caml_print_int
	bl min_caml_print_newline
	
	bl min_caml_exit

