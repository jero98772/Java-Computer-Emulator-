import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        // Initialize ROM and RAM
        
        ROM rom = new ROM(8, 16);
        short sizeram = 32767;
        RAM ram = new RAM(sizeram );


        rom.loadRomFromFile("rom.txt");

        // Create CPU with ROM and RAM
        CPU cpu = new CPU(rom, ram);

        // Execute instructions
        cpu.executeInstructions();
        System.out.print("finish");
    }
}



// CPU class
class CPU {
    private ROM rom;
    private RAM ram;
    private short registerD;
    private short registerA;
    ALU alu = new ALU();

    public CPU(ROM rom, RAM ram) {
        this.rom = rom;
        this.ram = ram;
    }

    // Method to execute a single instruction from ROM
    public void executeInstructions() {
        System.out.println(rom.getNumRows());
        for (short instructionAddress = 0; instructionAddress < rom.getNumRows(); instructionAddress++) {
    
        
            Vector<Integer> instruction = rom.getValuesAt(instructionAddress);
            //Vector<Integer> instruction = rom.convertShortToBitVector(rom.getValuesAt(instructionAddress));

            
            short typeInstruccion = instruction.get(0).shortValue(); 
            if (typeInstruccion==1){// is C intricion
                short a = instruction.get(3).shortValue();   // a
                short zx = instruction.get(4).shortValue();  // c1
                short nx = instruction.get(5).shortValue();  // c2
                short zy = instruction.get(6).shortValue();  // c3
                short ny = instruction.get(7).shortValue();  // c4
                short f = instruction.get(8).shortValue();   // c5
                short no = instruction.get(9).shortValue();  // c6
                short d1 = instruction.get(10).shortValue();
                short d2 = instruction.get(11).shortValue();
                short d3 = instruction.get(12).shortValue();
                short j1 = instruction.get(13).shortValue();
                short j2 = instruction.get(14).shortValue();
                short j3 = instruction.get(15).shortValue();

                if(a==0){//register A
                    System.out.println("go register A");
                    this.alu.setInputs(this.registerD,this.registerA);
                }else{// memory
                    System.out.println("go memory");
                    this.alu.setInputs(this.registerD,this.ram.getValueAt(this.registerA));
                }
                this.alu.setControlBits(zx, nx, zy, ny, f, no);
                this.alu.compute();
                System.out.println("ALU");
                System.out.println(this.alu.getOut());
                this.handleRegisters(d1, d2, d3, this.alu.getOut());
                instructionAddress=this.jump(j1, j2, j3, this.alu.getOut() ,instructionAddress);
            
            }else{
    		//else is a instruccion
                short result = 0;
                int shiftAmount = 0;
                int intValue = binaryVectorToInt(instruction);

                 this.registerA = (short) intValue;

            }
        System.out.println("registerA:");
        System.out.println( this.registerA);
        System.out.println("registerD");
        System.out.println( this.registerD);
        System.out.println("");

        System.out.println("");

        }
        this.ram.printFirstElements(4);
    }

    public short jump(short j1, short j2, short j3, short out ,short romPosition) {
        if (j1 == 1 && j2 == 0 && j3 == 0 && out > 0) { // JGT
            romPosition = registerA;
        } else if (j1 == 1 && j2 == 0 && j3 == 1 && out == 0) { // JEQ
            romPosition = registerA;
        } else if (j1 == 1 && j2 == 0 && j3 == 1 && out >= 0) { // JGE
            romPosition = registerA;
        } else if (j1 == 0 && j2 == 1 && j3 == 0 && out < 0) { // JLT
            romPosition = registerA;
        } else if (j1 == 0 && j2 == 1 && j3 == 1 && out != 0) { // JNE
            romPosition = registerA;
        } else if (j1 == 0 && j2 == 1 && j3 == 1 && out <= 0) { // JLE
            romPosition = registerA;
        } else if (j1 == 1 && j2 == 1 && j3 == 1) { // JMP
            romPosition = registerA;
        } else {
            
        }
        return romPosition;
    }

    public void handleRegisters(short d1, short d2, short d3, short value) {

        if (d1 == 1) {
        System.out.println("registerA");
        System.out.println( value);
            this.registerA = value; // Store in A register
        }
        if (d2 == 1) {
            System.out.println("registerD");
            System.out.println( value);
            this.registerD = value; // Store in D register
        }
        if (d3 == 1) {
            System.out.println("ram");
            System.out.println( value);
            this.ram.setValueAt(value,this.registerA); // Store in RAM at address A
        }
    }
    private static int binaryVectorToInt(Vector<Integer> binaryVector) {
        int result = 0;
        int size = binaryVector.size();
        for (int i = 0; i < size; i++) {
            result |= (binaryVector.get(i) << (size - 1 - i));
        }
        return result;
    }
}



public class ALU {
    // Inputs
    short x;
    short y;

    // Control bits
    short zx;
    short nx;
    short zy;
    short ny;
    short f;
    short no;

    // Outputs
    short out1;
    short out;
    short zr;
    short ng;
    short x1;
    short x2;
    short y1;
    short y2;
    public void setInputs(short x, short y) {
        this.x = x;
        this.y = y;
    }

    public void setControlBits(short zx, short nx, short zy, short ny, short f, short no) {
        this.zx = zx;
        this.nx = nx;
        this.zy = zy;
        this.ny = ny;
        this.f = f;
        this.no = no;
    }
    public ALU() {
        /*
          x=outDReg,
          y=outAorM,
          zx=instruction[11],   // c1
          nx=instruction[10],   // c2
          zy=instruction[9],    // c3
          ny=instruction[8],    // c4
          f =instruction[7],    // c5
          no=instruction[6],    // c6
          out=outALU,
          out=outM,
          zr=isZeroOut,
          ng=isNegOut
        */
        this.x = x;
        this.y = y;
        this.zx = zx;
        this.nx = nx;
        this.zy = zy;
        this.ny = ny;
        this.f = f;
        this.no = no;
        this.out1 = out1;
        this.out =out;
        this.zr = zr;
        this.ng = ng;
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
    }
    public void compute() {

    this.x1 = (short) (this.zx == 1 ? 0 : this.x);           // Zero the x input
    this.x2 = (short) (this.nx == 1 ? ~this.x1 : this.x1);        // Negate the x input
    this.y1 = (short) (this.zy == 1 ? 0 : this.y);           // Zero the y input
    this.y2 = (short) (this.ny == 1 ? ~this.y1 : this.y1);        // Negate the y input

    this.out1 = (short) (this.f == 1 ? this.x2 + this.y2 : this.x2 & this.y2);  // Perform the operation
    this.out = (short) (this.no == 1 ? ~this.out1 : this.out1);             // Negate the output

    this.zr = (short) (this.out == 0 ? 1 : 0);                        // Set the zero flag
    this.ng = (short) ((short) ((this.out >> 15) & 1) == 1 ? 1 : 0);     // Set the negative flag
        System.out.println(this.y);

    }
    public short getOut() {
        return this.out;
    }

    public short getZr() {
        return this.zr;
    }

    public short getNg() {
        return this.ng;
    }
}
// convert romvector of shorts  
public class ROM {
    private Vector<Vector<Integer>> rom;

    public ROM(int rows, int columns) {
        rom = new Vector<>(rows);
        for (int i = 0; i < rows; i++) {
            rom.add(new Vector<>(columns));
            for (int j = 0; j < columns; j++) {
                rom.get(i).add(0); // Initialize with zeros
            }
        }
    }

    // Method to load ROM from a text file
    public void loadRomFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int rowIndex = 0;

            while ((line = br.readLine()) != null && rowIndex < rom.size()) {
                // Ensure the line is not longer than the number of columns in rom
                if (line.length() > rom.get(rowIndex).size()) {
                    line = line.substring(0, rom.get(rowIndex).size());
                }

                for (int colIndex = 0; colIndex < line.length() && colIndex < rom.get(rowIndex).size(); colIndex++) {
                    // Convert each character to an integer
                    int value = Character.getNumericValue(line.charAt(colIndex));
                    rom.get(rowIndex).set(colIndex, value);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get the values at a specific position
    public Vector<Integer> getValuesAt(int position) {
        if (position >= 0 && position < rom.size()) {
            return rom.get(position);
        } else {
            throw new IndexOutOfBoundsException("Invalid ROM position: " + position);
        }
    }
    public static List<Short> convertShortToBitVector(short value) {
        List<Short> bitVector = new ArrayList<>(16);
        
        for (int i = 0; i < 16; i++) {
            // Shift right and mask with 1 to get the bit at position i
            bitVector.add(0, (short)((value >> i) & 1));
        }
        
        return bitVector;
    }    // Method to get the number of rows
    public int getNumRows() {
        return rom.size();
    }


}

class RAM {
    private short[] ram;

    public RAM(short size) {
        ram = new short[size];
    }

    // Method to set a value at a specific position
    public void setValueAt(short value, short position) {
        if (position >= 0 && position < ram.length) {
            ram[position] = value;
        } else {
            throw new IndexOutOfBoundsException("Invalid RAM position: " + position);
        }
    }

    // Method to get a value at a specific position
    public short getValueAt(short position) {
        if (position >= 0 && position < ram.length) {
            return ram[position];
        } else {
            throw new IndexOutOfBoundsException("Invalid RAM position: " + position);
        }
    }
    public void printFirstElements(int elementsToPrint) {

        for (int i = 0; i < elementsToPrint && i < ram.length; i++) {
            System.out.println(ram[i]);
        }
    }
}
