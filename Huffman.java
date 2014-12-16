import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

//Use this file as example (13kb): C:/Users/Rahul/Desktop/MinesweeperFrame.java

public class Huffman 
{
	public static void main(String[] args)
	{
		this.run();
	}
	
	public void run()
	{
		String filePath = args[0];
		System.out.println("Compressing file: "+filePath);
		File file = new File(filePath);
		byte[] unsortedBytes = new byte[(int)file.length()];
		ArrayList<Integer> encodedList = new ArrayList<>();
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
		int counter = 0;
		System.out.println("Before compression bytes: "+unsortedBytes.length);
		for(int i=0; i<unsortedBytes.length; i++)
		{
			b = tree.hashMap.get(unsortedBytes[i]);
			encodedString.append(b.encodedBits);
			counter += b.encodedBits.length();
		}
		while(encodedString.toString().length()%8 != 0)
		{
			encodedString.append("0");
		}
		String enc = encodedString.toString();
		for(int i=0; i<encodedString.toString().length(); i++)
		{
			encodedList.add((int)(enc.charAt(i)));
		}
		
		byte[] encoded = Twiddle.bitsToBytes(encodedList);
		System.out.println("After compression bytes: "+encoded.length);
		File outFile = new File(filePath+".huff");
		
		try 
		{
			FileOutputStream out = new FileOutputStream(outFile);
			ObjectOutputStream outObj = new ObjectOutputStream(out);
			outObj.writeObject(tree.arrangedPriorityQueue);
			out.write(encoded);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
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
