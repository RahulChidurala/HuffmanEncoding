import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

//Use this file as example (13kb): C:/Users/Rahul/Desktop/MinesweeperFrame.java

public class Huff 
{
	public static void main(String[] args)
	{
		String filePath = args[0];
		System.out.println("Compressing file: "+filePath);
		File file = new File(filePath);
		byte[] unsortedBytes = new byte[(int)file.length()];
		ArrayList<Integer> encodedList;
		//byte[] unsortedBytes = {(byte)-35, (byte)41,(byte) -35,(byte) 41,(byte) 116,(byte) -35, (byte)22, (byte)-35, (byte)116};
		try
		{
			FileInputStream rawInput = new FileInputStream(file);
			unsortedBytes = new byte[rawInput.available()];
			rawInput.read(unsortedBytes);
			rawInput.close();
		}
		catch(Exception e)
		{
			System.out.println("Error:");
			e.printStackTrace();
		}
		//printBytes(unsortedBytes);
		HuffTree tree = new HuffTree(unsortedBytes);
		
		Node b;
		StringBuilder encodedString = new StringBuilder("");
		HashMap<Byte, Node> finalMap = new HashMap<Byte, Node>();
		
		for(int i=-127; i<=128; i++)
		{
			if(tree.hashMap.get((byte)i).frequency != 0)
			{
				finalMap.put((byte) i, tree.hashMap.get((byte)i));
			}
		}
		System.out.println("Before compression KB: "+unsortedBytes.length/1024);
		for(int i=0; i<unsortedBytes.length; i++)
		{
			b = tree.hashMap.get(unsortedBytes[i]);
			encodedString.append(b.encodedBits);
		}
		int ignoreEndPadding = 0;
		while(encodedString.toString().length()%8 != 0)
		{
			encodedString.append("0");
			ignoreEndPadding++;
		}
		String enc = encodedString.toString();
		encodedList = new ArrayList<>(enc.length());
		int lim = encodedString.toString().length();
		//*****************ArrayList
		for(int i=0; i<lim; i++)
		{
			encodedList.add((int)(enc.charAt(i)) - 48); //NOT SURE WHY IT STARTS AT 48; OH IT WAS CONVERTING CHARS TO ITS INT VALUES
		}
		byte[] encoded = Twiddle.bitsToBytes(encodedList);
		System.out.println("After compression KB: "+encoded.length/1024);
		File outFile = new File(filePath+".huff");
		HuffObject huffObj = new HuffObject(encoded, tree.arrangedPriorityQueue, ignoreEndPadding, unsortedBytes.length);
		try 
		{
			FileOutputStream out = new FileOutputStream(outFile);
			ObjectOutputStream outObj = new ObjectOutputStream(out);
			outObj.writeObject(huffObj);
			out.close();
			outObj.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.println("Total file size with tree KB: "+outFile.length()/1024);
	}
	
	public static void printBytes(byte[] bytes)
	{
		String str = "Start: ";
		System.out.println(bytes.length);
		for(int i=0; i<bytes.length; i++)
		{
			//System.out.println(i+": "+bytes[i]);
			str+=bytes[i]+", ";
		}
		System.out.println(str);
	}
}
