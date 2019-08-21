package NewTime.lang.brainfuck;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Compressor {

	/*
	 * Compressor (de-)compresses brainfuck code..
	 * 
	 * When compressing bainfuck code the following happens:
	 * 		First the code is translated into "brainfuck encoding" using the  "encode" method.
	 *  	Then the code is cleaned using the "clean" method to remove any unwanted characters.
	 * 		Finally the code is compressed using the "compress" method.
	 * 
	 * When decompressing brainfuck code the following happens:
	 * 		First the code is cleaned using the "clean" method to remove any unwanted characters.
	 * 		Then the code is decompressed using the "decompress" method.
	 * 		Finally the code is decoded from "brainfuck encoding" into utf-8.
	 * 
	 * Extra:
	 * 		compressFile:
	 * 			Load an input file, do all the stuff and output it to the given output file.
	 * 			If no output file is given, don't output to any file.
	 * 			Returns the compressed code.
	 * 
	 * 		decompressFile:
	 * 			Load the input file, do all the stuff and output it to the given output file.
	 * 			If no output file is given, don't output to any file.
	 * 			Returns the decompressed code.
	 */
	
	public static void main(String[] args) {
		Compressor compressor = new Compressor();
		compressor.compressFile(new File("res/example.txt"), new File("res/example_compressed.txt"));
		byte[] newOps = compressor.decompressFile(new File("res/example_compressed.txt"), new File("res/example_decompressed.txt"));
		BrainFuck test = new BrainFuck(new String(newOps));
	}

	/**
	 * Compress a brainfuck file.
	 * @param input File to be compressed.
	 * @param output File to which the compressed data should be written. If null, write to input file.
	 * @return byte array containing the compressed data.
	 */
	public byte[] compressFile(File input, File output) {
		byte[] buffer = null;
		try {
			buffer = new byte[(int) input.length()];
			DataInputStream in = new DataInputStream(new FileInputStream(input));
			in.read(buffer);
			in.close();
			
			buffer = encode(buffer);
			buffer = clean(buffer);
			buffer = compress(buffer);
			
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
	
	/**
	 * Decompress a compressed brainfuck file.
	 * @param input	File to be decompressed.
	 * @param output File to which the decompressed data should be written. If null, don't write to any file, just return the decompressed data.
	 * @return byte array containing the decompressed data.
	 */
	public byte[] decompressFile(File input, File output) {
		byte[] buffer = null;
		try {
			buffer = new byte[(int) input.length()];
			DataInputStream in = new DataInputStream(new FileInputStream(input));
			in.read(buffer);
			in.close();
			
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
	
	/**
	 * Remove any annoying characters.
	 * @param buffer byte array containing the code to be cleaned.
	 * @return byte array containing clean code.
	 */
	public byte[] clean(byte[] buffer) {
		String raw = new String(buffer);
		raw = raw.replace("\r", "");
		raw = raw.replace("\n", "");
		raw = raw.replace("\r", "");
		raw.trim();
		return raw.getBytes();
	}
	
	/**
	 * Encode the given byte array to follow "brainfuck encoding".
	 * @param buffer byte array to be encoded.
	 * @return byte array encoded in "brainfuck encoding".
	 */
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
	
	/**
	 * Decode the given byte array from "brainfuck encoding" back to utf-8.
	 * @param buffer byte array to be decoded.
	 * @return byte array encoded in utf-8
	 */
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
	
	/**
	 * Compress the given byte array.
	 * @param operators byte array to be compressed.
	 * @return byte array containing compressed operators.
	 */
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
	
	/**
	 * Decompress the given byte array.
	 * @param operators byte array to be decompressed.
	 * @return byte array containing decompressed operators.
	 */
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
