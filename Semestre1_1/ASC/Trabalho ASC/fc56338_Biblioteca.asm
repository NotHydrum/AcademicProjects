;fc56338
section .rodata
STDIN equ 0
STDOUT equ 1
SYS_read equ 0
SYS_write equ 1
SYS_open equ 2
SYS_close equ 3
SYS_exit equ 60
SYS_creat equ 85
S_IRUSR equ 00400q ;owner, read permission
S_IWUSR equ 00200q ;owner, write permission

section .text
;prints string to console
;rsi - address of string to print
;rdx - number of bytes to print
global print
print:
    mov rax, SYS_write
    mov rdi, STDOUT
    syscall
    ret

;opens file
;rdi - file to open
;rsi - permissions (0 to read only, 1 to write only, 2 to read and write)
;returns file descriptor (successful) or a negative number (unsuccessful), in rax
global openFile
openFile:
    mov rax, SYS_open
    syscall
    ret

;reads file and moves it to given location
;rdi - file descriptor
;rsi - location to move file to
;rsx - number of bytes to read
;returns file in given location (successful) or a negative number in rax (unsuccessful)
global readFile
readFile:
    mov rax, SYS_read
    syscall
    ret
    
;creates and opens file
;rdi - file name, ending in NULL
;returns file descriptor (successful) or a negative number (unsuccessful), in rax
global createFile
createFile:
    mov rax, SYS_creat
    mov rsi, S_IRUSR | S_IWUSR
    syscall
    ret
    
;writes given string to given file
;rdi - file descriptor
;rsi - adress of string to write
;rsx - number of bytes to write
global writeToFile
writeToFile:
    mov rax, SYS_write
    syscall
    ret
    
global recuperarBit
recuperarBit:
    shl cl, 1
    and bl, 00000001b
    or cl, bl
    ret

;ends program
global endProgram
endProgram:
    mov rax, SYS_exit
    xor rdi, rdi
    syscall