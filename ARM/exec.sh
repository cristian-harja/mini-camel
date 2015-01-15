arm-none-eabi-as -o $1.o $1.s libmincaml.S
arm-none-eabi-ld -o $1 $1.o
qemu-arm ./$1
rm $1.o