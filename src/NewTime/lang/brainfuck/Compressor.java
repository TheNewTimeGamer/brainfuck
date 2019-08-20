package NewTime.lang.brainfuck;

import java.util.Arrays;

public class Compressor {

	public static void main(String[] args) {
		byte[] operators = {1,0,1,0,1,1,0};
		System.out.println(Arrays.toString(operators));
		Compressor compressor = new Compressor();
		byte[] compressed = compressor.compress(operators);
		byte[] decompressed = compressor.decompress(compressed);
		System.out.println(Arrays.toString(compressed));
		System.out.println(Arrays.toString(decompressed));
	}
	
	public byte[] compress(byte[] operators) {
		byte[] buffer = null;
		if(operators.length % 2 == 0) {
			buffer = new byte[(operators.length/2)];
		}else {
			buffer = new byte[(operators.length/2)+1];
		}
		
		for(int i = 0; i < operators.length; i+=2) {
			byte start = operators[i];
			if(i+1 >= operators.length) {
				buffer[i/2] = start;
				break;
			}
			byte end = operators[i+1];
			end = (byte) (end << 4);
			buffer[i/2] = (byte) (start | end);
		}
		return buffer;
	}
	
	public byte[] decompress(byte[] operators) {
		byte[] buffer = null;
		if(operators[operators.length-1] < 8) {
			buffer = new byte[operators.length*2-1];
		}else {
			buffer = new byte[operators.length];
		}		
		for(int i = 0; i < operators.length; i++) {
			byte start = (byte) ((operators[i] << (8*3+4)) >> (8*3+4));
			byte end = (byte) (operators[i] >> 4);
			
			buffer[i*2] = start;
			if(i*2+1 < buffer.length) {
				buffer[i*2+1] = end;
			}
		}

		return buffer;
	}
	
}
