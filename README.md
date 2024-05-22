# Java Computer/CPU Emulator Readme

## Overview

This Java program is a simple CPU emulator that simulates the execution of instructions stored in a ROM (Read-Only Memory) using a CPU that interacts with both the ROM and a RAM (Random Access Memory). The program reads instructions from a text file, processes them through the CPU, and outputs the results.

## Project Structure

The program consists of the following main classes:

- `Main`: The entry point of the program.
- `CPU`: The central processing unit that fetches, decodes, and executes instructions.
- `ALU`: The arithmetic logic unit that performs operations based on control bits.
- `ROM`: The read-only memory that stores instructions.
- `RAM`: The random access memory that stores data.

## Usage

### Prerequisites

- Ensure you have Java installed on your system.
- Create a file named `rom.txt` that contains the instructions for the CPU.

### Running the Program

1. Compile the Java files:
    ```sh
    javac Main.java
    ```
2. Run the compiled program:
    ```sh
    java Main
    ```

### rom.txt File Format

The `rom.txt` file should contain instructions in a format that the CPU can understand. Each line represents one instruction, and each character in the line is a binary digit (0 or 1). The length of each line should not exceed the number of columns defined in the `ROM` class (16 in this case).

## Code Explanation

### Main Class

The `Main` class initializes the ROM and RAM, loads instructions from the `rom.txt` file into the ROM, and creates a CPU instance to execute these instructions.

```java
public class Main {
    public static void main(String[] args) {
        ROM rom = new ROM(8, 16);
        short sizeram = 32767;
        RAM ram = new RAM(sizeram);
        rom.loadRomFromFile("rom.txt");

        CPU cpu = new CPU(rom, ram);
        cpu.executeInstructions();
        System.out.print("finish");
    }
}
```

### CPU Class

The `CPU` class is responsible for executing instructions stored in the ROM. It uses the `ALU` class for arithmetic and logic operations and interacts with the `RAM` to store and retrieve data.

### ALU Class

The `ALU` (Arithmetic Logic Unit) performs operations based on the control bits set by the CPU. It supports operations such as addition, bitwise AND, negation, and zeroing inputs.

### ROM Class

The `ROM` class stores the instructions loaded from the `rom.txt` file. It provides methods to retrieve instructions and convert them from binary to integer.

### RAM Class

The `RAM` class simulates a simple memory module. It allows setting and getting values at specific addresses and printing the first few elements for debugging purposes.

## Example

### rom.txt

```plaintext
1110101010101010
0001101010101010
...
```

This file should contain binary instructions, each line representing one instruction.

### Sample Output

```plaintext
go register A
ALU
registerA: 32767
registerD: 0
ram: 0
finish
```

## Additional Information

- The program prints the contents of registers and RAM after executing each instruction, which helps in debugging and understanding the instruction flow.
- The `handleRegisters` method in the `CPU` class manages updates to the registers and RAM based on the decoded instruction.
- The `jump` method in the `CPU` class handles conditional and unconditional jumps based on the ALU output.

This emulator provides a basic framework for understanding how a CPU fetches, decodes, and executes instructions, and it can be extended with additional features and more complex instruction sets.