;fc56338
extern print
extern openFile
extern readFile
extern recuperarBit
extern endProgram

section .data
char: db 0

section .bss
readBuffer: resb BUFF_SIZE

section .rodata
error_msg: db "Erro: Parâmetros inválidos!"
error_msg_length: equ $-error_msg
nextLine: db 10
nextLine_length: equ $-nextLine
NULL equ 0
O_RDONLY equ 0
BUFF_SIZE equ 2048

section .text
global _start
_start:
    ;obtain number of arguments
    pop rcx
    ;verify if exactly 1 parameter was written
    cmp cl, 2
    jne error
    
open:
    ;open picture file
    pop rdi
    pop rdi
    mov rsi, O_RDONLY
    call openFile
    ;verify success
    cmp rax, 0
    jl error
    
read:
    ;read picture file
    mov rdi, rax
    mov rsi, readBuffer
    mov rdx, BUFF_SIZE
    call readFile
    ;verify success
    cmp rax, 0
    jl error
    
offset:
    ;read file offset
    mov ebp, dword [readBuffer+10]
    
decode:
    ;decodes and prints 3 characters (24 bits) per loop. if any are null jumps to end
    xor rbx, rbx
    xor rcx, rcx
    ;1:1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;1:2
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;1:3
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;1:4  skips transparency byte
    add ebp, 2
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;1:5
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;1:6
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;1:7  skips transparency byte
    add ebp, 2
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;1:8  last bit
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    
    ;print character
    cmp cl, NULL
    je end
    mov [char], cl
    mov rsi, char
    mov rdx, 1
    call print
    xor rcx, rcx
    
    ;2:1
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;2:2  skips transparency byte
    add ebp, 2
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;2:3
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;2:4
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;2:5  skips transparency byte
    add ebp, 2
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;2:6
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;2:7
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;2:8  skips transparency byte  last bit
    add ebp, 2
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    
    ;print character
    cmp cl, NULL
    je end
    mov [char], cl
    mov rsi, char
    mov rdx, 1
    call print
    xor rcx, rcx
    
    ;3:1
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;3:2
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;3:3  skips transparency byte
    add ebp, 2
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;3:4
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;3:5
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;3:6  skips transparency byte
    add ebp, 2
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;3:7
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    ;3:8  last bit
    add ebp, 1
    mov bl, byte [readBuffer+ebp]
    call recuperarBit
    
    ;print character
    cmp cl, NULL
    je end
    mov [char], cl
    mov rsi, char
    mov rdx, 1
    call print
    add ebp, 2
    jmp decode
  
error:
    ;prints error message
    mov rdx, error_msg_length
    mov rsi, error_msg
    call print

end:
    ;prints to next line and ends program
    mov rsi, nextLine
    mov rdx, nextLine_length
    call print
    call endProgram