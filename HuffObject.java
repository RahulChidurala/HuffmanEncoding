import java.io.Serializable;
import java.util.HashMap;
import java.util.PriorityQueue;


public class HuffObject implements Serializable
{
	PriorityQueue<Node> arrangedPriorityQueue;
	byte[] compressedBytes;
	int ignoreEndPadding;
	int byteArraySize;
	
	public HuffObject(byte[] compressedBytes, PriorityQueue<Node> arrangedPriorityQueue, int ignoreEndPadding, int byteArraySize)
	{
		this.compressedBytes = compressedBytes;
		this.arrangedPriorityQueue = arrangedPriorityQueue;
		this.ignoreEndPadding = ignoreEndPadding;
		this.byteArraySize = byteArraySize;
	}
}
