package NewTime.lang.brainfuck;

import java.io.IOException;
import java.util.Arrays;

public class BrainFuck {

	public static final byte SHIFT_RIGHT = ((byte)'>');
	public static final byte SHIFT_LEFT  = ((byte)'<');
	public static final byte INCREMENT   = ((byte)'+');
	public static final byte DECREMENT   = ((byte)'-');
	public static final byte WRITE		 = ((byte)'.');
	public static final byte READ		 = ((byte)',');
	public static final byte LOOP_START	 = ((byte)'[');
	public static final byte LOOP_END	 = ((byte)']');
		
	public BrainFuck(String code) {
		byte[] operators = new byte[code.length()];
		char[] chars = code.toCharArray();
		int changes = clean(operators, chars);
		double start = System.currentTimeMillis();
		byte[] memory = run(operators);
		System.out.println("");
		System.out.println("Execution completed in " + ((System.currentTimeMillis()-start)/1000) + " seconds");
		System.out.println("\r\nMemory dump:");

		for(int i = 0; i < memory.length; i++) {
			int b = Byte.toUnsignedInt(memory[i]);
			if(b < 10) {System.out.print(" ");}
			if(b < 100) {System.out.print(" ");}
			System.out.print(b + "|");
			if(i != 0 && i % 16 == 0) {
				System.out.println("");
			}
		}
	}
	
	private int clean(byte[] operators, char[] code) {
		int changeCount = 0;
		int operatorIndex = 0;		
		for(char c : code) {
			if(isOperator(c)) {
				operators[operatorIndex] = (byte)c;
				operatorIndex++;
			}
		}		
		return changeCount;
	}
	
	private boolean isOperator(char c) {
		if(c == INCREMENT   || c== DECREMENT) 	{return true;}
		if(c == SHIFT_RIGHT || c== SHIFT_LEFT)	{return true;}
		if(c == LOOP_START  || c== LOOP_END) 	{return true;}
		if(c == READ 		|| c == WRITE) 		{return true;}
		return false;
	}
	
	private byte[] run(byte[] operators) {
		int code = 0;
		int loopStart = 0;
		
		byte pointer = Byte.MIN_VALUE;
		byte[] memory = new byte[256];
		
		for(int i = 0; i < operators.length; i++) {
			switch(operators[i]) {
				case SHIFT_RIGHT:
					pointer++;
					break;
				case SHIFT_LEFT:
					pointer--;
					break;
				case INCREMENT:
					memory[pointer+128]++;
					break;
				case DECREMENT:
					memory[pointer+128]--;
					break;
				case WRITE:
					System.out.write(memory[pointer+128]);
					System.out.flush();
					break;
				case READ:
					try {
						memory[pointer+128] = System.in.readNBytes(1)[0];
					}catch(IOException e) {e.printStackTrace();}
					break;
				case LOOP_START:
					loopStart = i;
					break;
				case LOOP_END:
					if(memory[pointer+128] != 0) {
						i = loopStart;
					}
					break;
			}
		}
		
		return memory;
	}
	
}
