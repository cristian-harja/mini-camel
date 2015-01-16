@for a comment
	.data
	@.balign 4
	.text
	.global _start
_start:
	mov r0, #4
	mov r0, r0, LSL #2
	bl min_caml_print_int
	bl min_caml_print_newline
	
	bl min_caml_exit

