package NewTime.lang.brainfuck;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Compressor {

	public static void main(String[] args) {
		Compressor compressor = new Compressor();
		compressor.compressFile(new File("res/example.txt"), new File("res/example_compressed.txt"));
		byte[] newOps = compressor.decompressFile(new File("res/example_compressed.txt"), new File("res/example_decompressed.txt"));
		BrainFuck test = new BrainFuck(new String(newOps));
	}
	
	public byte[] compressFile(File input, File output) {
		byte[] buffer = null;
		try {
			buffer = new byte[(int) input.length()];
			DataInputStream in = new DataInputStream(new FileInputStream(input));
			in.read(buffer);
			
			buffer = encode(buffer);
			buffer = clean(buffer);
			buffer = compress(buffer);
			
			DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
			out.write(buffer);
			out.flush();
			out.close();			
		}catch(IOException e) {
			e.printStackTrace();
		}
				
		return buffer;
	}
	
	public byte[] decompressFile(File input, File output) {
		byte[] buffer = null;
		try {
			buffer = new byte[(int) input.length()];
			DataInputStream in = new DataInputStream(new FileInputStream(input));
			in.read(buffer);
			
			buffer = clean(buffer);
			buffer = decompress(buffer);
			buffer = decode(buffer);
			
			if(output != null) {
				DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
				out.write(buffer);
				out.flush();
				out.close();		
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
				
		return buffer;
	}
	
	public byte[] clean(byte[] buffer) {
		String raw = new String(buffer);
		raw = raw.replace("\r", "");
		raw = raw.replace("\n", "");
		raw = raw.replace("\r", "");
		raw.trim();
		return raw.getBytes();
	}
	
	public byte[] encode(byte[] buffer) {		
		for(int i = 0; i < buffer.length; i++) {
			byte b = buffer[i];
			if(b == (byte)'<') {buffer[i] = 0; continue;}
			if(b == (byte)'>') {buffer[i] = 1; continue;}
			if(b == (byte)'+') {buffer[i] = 2; continue;}
			if(b == (byte)'-') {buffer[i] = 3; continue;}
			if(b == (byte)',') {buffer[i] = 4; continue;}
			if(b == (byte)'.') {buffer[i] = 5; continue;}
			if(b == (byte)'[') {buffer[i] = 6; continue;}
			if(b == (byte)']') {buffer[i] = 7; continue;}	
			if(b == (byte)' ') {b = (byte)' ';}			
		}
		return buffer;
	}
	
	public byte[] decode(byte[] buffer) {
		for(int i = 0; i < buffer.length; i++) {
			byte b = buffer[i];
			if(b == 0) {buffer[i] = '<'; continue;}
			if(b == 1) {buffer[i] = '>'; continue;}
			if(b == 2) {buffer[i] = '+'; continue;}
			if(b == 3) {buffer[i] = '-'; continue;}
			if(b == 4) {buffer[i] = ','; continue;}
			if(b == 5) {buffer[i] = '.'; continue;}
			if(b == 6) {buffer[i] = '['; continue;}
			if(b == 7) {buffer[i] = ']'; continue;}		
		}
		return buffer;
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
			buffer = new byte[operators.length*2];
		}		
		for(int i = 0; i < operators.length; i++) {
			byte start = (byte) ((operators[i] << (8*3+4)) >> (8*3+4));
			byte end = (byte) (operators[i] >> 4);
			
			if(i*2 < buffer.length) {
				buffer[i*2] = start;
			}
			if(i*2+1 < buffer.length) {
				buffer[i*2+1] = end;
			}
		}

		return buffer;
	}
	
}
