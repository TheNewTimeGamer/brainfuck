package NewTime.lang.brainfuck;

import java.util.Arrays;

public class Compressor {

	public static void main(String[] args) {
		byte[] operators = {4,5,6,7};
		System.out.println(Arrays.toString(operators));
		Compressor compressor = new Compressor();
		byte[] compressed = compressor.compress(operators);
		byte[] decompressed = compressor.decompress(compressed);
		System.out.println(Arrays.toString(compressed));
		System.out.println(Arrays.toString(decompressed));
	}
	
	public byte[] compress(byte[] operators) {
		byte[] buffer = new byte[operators.length/2];
		
		for(int i = 0; i < operators.length; i+=2) {
			byte start = operators[i];
			byte end = operators[i+1];
			System.out.print("Values: " + start + ", " + end);
			end = (byte) (end << 4);
			System.out.print(" -> " + start + ", " + end);
			buffer[i/2] = (byte) (start | end);
			System.out.println(" = " + start + " + " + end + " = " + ((byte)(start | end)));
		}
		return buffer;
	}
	
	public byte[] decompress(byte[] operators) {
		byte[] buffer = new byte[operators.length*2];
		
		for(int i = 0; i < operators.length; i++) {
			byte start = (byte) ((operators[i] << (8*3+4)) >> (8*3+4));
			byte end = (byte) (operators[i] >> 4);
			
			System.out.println(operators[i] + " = " + start + ", " + end + " -> " + i + " & " + (i+1));
			
			buffer[i*2] = start;
			buffer[i*2+1] = end;
		}
		
		return buffer;
	}
	
}
