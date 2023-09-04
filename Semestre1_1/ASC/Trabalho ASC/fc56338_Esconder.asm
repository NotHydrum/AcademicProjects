;fc56338
extern print
extern openFile
extern readFile
extern createFile
extern writeToFile
extern endProgram

section .data

section .bss
messageBuffer: resb MSG_BUFF_SIZE
pictureBuffer: resb PIC_BUFF_SIZE

section .rodata
error_msg: db "Erro: Parâmetros inválidos!"
error_msg_length: equ $-error_msg
nextLine: db 10
nextLine_length: equ $-nextLine
NULL equ 0
MSG_BUFF_SIZE equ 1024
PIC_BUFF_SIZE equ 0xFFFFFF
O_RDONLY equ 0
O_RDWR equ 2

section .text
global _start
_start:
    ;obtain number of arguments
    pop rcx
    ;verify if exactly 3 parameters were written
    cmp cl, 4
    jne error
    ;useless argument
    pop rdi
    
openMessage:
    ;open message file
    pop rdi
    call openFile
    ;verify success
    cmp rax, 0
    jl error

readMessage:
    ;read message file
    mov rdi, rax
    mov rsi, messageBuffer
    mov rdx, MSG_BUFF_SIZE
    call readFile
    ;verify success
    cmp rax, 0
    jl error
    
openPicture:
    ;open picture file
    pop rdi
    mov rsi, O_RDONLY
    call openFile
    ;verify success
    cmp rax, 0
    jl error
    mov r10, rax

readPicture:
    ;read picture file
    mov rdi, rax
    mov rsi, pictureBuffer
    mov rdx, PIC_BUFF_SIZE
    call readFile
    ;verify success
    cmp rax, 0
    jl error
    
size:
    ;read picture size
    mov r8d, dword [pictureBuffer+2]
    
offset:
    ;read picture offset
    mov r9d, dword [pictureBuffer+10]
    
createMod:
    ;create modded picture file
    pop rdi
    call createFile
    ;verify success
    cmp rax, 0
    jl error
    mov r11, rax
    
    xor r12, r12
hide:
    ;hides in picture 3 characters (24 bits) per loop
    ;if any are null, hides null and jumps to write
    cmp r9d, r8d
    jge write
    ;1:1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 7
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;1:2
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 6
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;1:3
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 5
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;1:4  skips transparency byte
    add r9d, 2
    cmp r9d, r8d
    jge write
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 4
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;1:5
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 3
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;1:6
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 2
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;1:7  skips transparency byte
    add r9d, 2
    cmp r9d, r8d
    jge write
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 1
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;1:8
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 0
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    cmp byte [messageBuffer+r12], 0
    je write
    add r12, 1
    ;2:1
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 7
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;2:2  skips transparency byte
    add r9d, 2
    cmp r9d, r8d
    jge write
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 6
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;2:3
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 5
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;2:4
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 4
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;2:5  skips transparency byte
    add r9d, 2
    cmp r9d, r8d
    jge write
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 3
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;2:6
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 2
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;2:7
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 1
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;2:8  skips transparency byte
    add r9d, 2
    cmp r9d, r8d
    jge write
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 0
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    cmp byte [messageBuffer+r12], 0
    je write
    add r12, 1
    ;3:1
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 7
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;3:2
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 6
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;3:3  skips transparency byte
    add r9d, 2
    cmp r9d, r8d
    jge write
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 5
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;3:4
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 4
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;3:5
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 3
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;3:6  skips transparency byte
    add r9d, 2
    cmp r9d, r8d
    jge write
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 2
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;3:7
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 1
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    ;3:8
    add r9d, 1
    mov r13b, byte [messageBuffer+r12]
    shr r13b, 0
    and r13b, 00000001b
    and byte [pictureBuffer+r9d], 11111110b
    or byte [pictureBuffer+r9d], r13b
    cmp byte [messageBuffer+r12], 0
    je write
    add r12, 1
    add r9d, 2
    jmp hide
    
write:
    ;write modded picture
    xor rdx, rdx
    mov rdi, r11
    mov rsi, pictureBuffer
    mov edx, r8d
    call writeToFile
    jmp end

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