import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.io.FileOutputStream;

//C:/Users/Rahul/Desktop/MinesweeperFrame.java

public class Puff 
{
	static HuffObject thing;
	static byte[] toDecode = null;
	static ArrayList<Integer> arrayOfBits;
	static PriorityQueue<Node> tree;
	static int ignorePadding;
	static byte[] finalBytes = null;
	static Node top;
	static int lim;
	static int lim2;
	static int index = 0;
	static int index2 = 0;
	static int originalByteArraySize;
	
	public static void main(String[] args)
	{
		String filePath = args[0];
		System.out.println("Decompressing file: "+filePath);
		File fil = new File(filePath);
		int index = filePath.indexOf(".huff");
		String newFilePath = filePath.substring(0,index);
		
		FileInputStream in;
		ObjectInputStream inObj;
		
		if(filePath.contains(".huff"))
		{
			try
			{
				File newFile = new File(newFilePath);
				in = new FileInputStream(fil);
				inObj = new ObjectInputStream(in);
				thing = (HuffObject) inObj.readObject();
				toDecode = thing.compressedBytes;
				System.out.println("Compressed KB: "+ toDecode.length/1024);
				tree = thing.arrangedPriorityQueue;
				top = tree.peek();
				ignorePadding = thing.ignoreEndPadding;
				originalByteArraySize = thing.byteArraySize;
				FileOutputStream out = new FileOutputStream(newFilePath);
				decodeFromTree(toDecode);
				out.write(finalBytes);
				out.close();
				System.out.println("Decompressed KB: " + finalBytes.length/1024);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		else
		{
			System.out.println("Please give a .huff file");
		}
	}
	
	public static void decodeFromTree(byte[] toDecode)
	{
		arrayOfBits = (ArrayList<Integer>) Twiddle.bytesToBits(toDecode);
		lim = arrayOfBits.size()-ignorePadding;
		lim2 = arrayOfBits.size();
		finalBytes = new byte[originalByteArraySize];
		try
		{
			finishDecoding(top);
		}
		catch(StackOverflowError e1)
		{
			System.out.println("Please increase JVM's heap size using the '-Xss1000m' tag");
			e1.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("Error: arraySize: "+originalByteArraySize+", total array bit: "+arrayOfBits.size()+", index: "+index+", index2: "+index2);
			//e.printStackTrace();
		}
	}
	
	public static void finishDecoding(Node n)
	{
		Node temp;
		if(index<lim)
		{
			temp = n.getChildFromBinary(arrayOfBits.get(index));
			if(temp.isLeaf)
			{
				if(index2<finalBytes.length)
				{
					finalBytes[index2] = temp.b;
					index++;
					index2++;
					finishDecoding(top);
				}
			}
			else
			{
				index++;
				finishDecoding(temp);
			}
		}
		
	}
}
