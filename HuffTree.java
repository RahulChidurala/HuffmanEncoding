import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Iterator;

public class HuffTree implements Serializable
{
	HashMap<Byte, Node> hashMap;
	HashMap<Byte, String> encodedBitMap = new HashMap<Byte, String>();
	PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
	PriorityQueue<Node> arrangedPriorityQueue;
	String encodedBits = "";
	byte[] unsortedBytes;
	
	public HuffTree(byte[] unsortedBytes)
	{	
		this.unsortedBytes = unsortedBytes;
		this.setup();
		arrangedPriorityQueue = this.makeTree(priorityQueue);
		//System.out.println(arrangedPriorityQueue.size()+", "+arrangedPriorityQueue.peek());
		Node superNode = arrangedPriorityQueue.peek();
		this.encodeFromTree(superNode);
		this.reverseAll();
	}
	
	public void setup()
	{
		this.hashMap = createEmptyTree();
		
		Node tempNode;
		
		for(int i=0; i<unsortedBytes.length; i++)
		{
			tempNode = hashMap.get(unsortedBytes[i]);
			tempNode.frequency++;
		}
		
		for(int i=-127; i<=128; i++)
		{
			if(hashMap.get((byte)i).frequency != 0)
			{
				priorityQueue.add(hashMap.get((byte)i));
			}
		}
	}
	
	public static HashMap<Byte, Node> createEmptyTree()
	{
		HashMap<Byte, Node> hashMap1 = new HashMap<>();
		
		for(int i= -127; i<=128; i++)
		{
			hashMap1.put((byte)i, new Node((byte)i, 0));
		}
		return hashMap1;
	}
	
	public PriorityQueue<Node> makeTree(PriorityQueue<Node> p)
	{
		Node first, second, combined;
		if(p.size() != 1)
		{
			first = p.poll();
			second = p.poll();
			combined = new Node(first, second);
			p.add(combined);
			makeTree(p);
		}
		return p;
	}
	
	public void encodeFromTree(Node n)
	{
		if(n.isLeaf)
		{
			
		}
		else
		{
			n.leftChild.addEncodedBit("0");
			n.rightChild.addEncodedBit("1");
			n.passDownEncodedBitsToChildren(n);
			encodeFromTree(n.leftChild);
			encodeFromTree(n.rightChild);
		}
	}
	
	public void reverseAll()
	{
		Node a;
		for(int i=-127; i<=128; i++)
		{
			a = hashMap.get((byte)i);
			if(a.frequency != 0)
			{
				a.reverseEncodedBits();
			}
		}
	}
	
}


