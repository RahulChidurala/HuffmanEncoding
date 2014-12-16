import java.io.Serializable;


public class Node implements Comparable<Node>, Serializable
{
	int frequency = 0;
	byte b;
	boolean isLeaf, isParent, isLeftChild, isRightChild;
	Node leftChild, rightChild, parent;
	String encodedBits = "";
	String bit = "";
	String temp = "";
	
	public Node(byte b, int frequency)
	{
		this.b = b;
		this.frequency = frequency;
		isLeaf = true;
		isParent = false;
	}
	
	public Node(Node a, Node b)
	{
		this.frequency = a.frequency + b.frequency;
		isLeaf = false;
		isParent = true;
		if(a.frequency < b.frequency)
		{
			leftChild = a; a.isLeftChild = true; a.bit = "0"; a.parent = this;
			rightChild = b; b.isRightChild = true; b.bit = "1"; b.parent = this;
		}
		else
		{
			leftChild = b; b.isLeftChild = true; b.bit = "0"; b.parent = this;
			rightChild = a; a.isRightChild = true; a.bit = "1"; a.parent = this;
		}
	}
	
	public int compareTo(Node a) 
	{
		if(this.frequency < a.frequency)
		{
			return -1;
		}
		else if(this.frequency > a.frequency)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	public String toString()
	{
		if(this.isParent)
		{
			return "Parent: f = "+this.frequency;
		}
		else
		{
			return "Leaf: byte: "+this.b+", f = "+this.frequency+", Encoded Bit = "+encodedBits;
		}
	}
	
	public void addEncodedBit(String str)
	{
		encodedBits += str;
	}
	
	public void passDownEncodedBitsToChildren(Node n)
	{
		if(n.isParent)
		{
			n.leftChild.addEncodedBit(n.encodedBits);
			n.rightChild.addEncodedBit(n.encodedBits);
		}
	}

	public void passToLeaves(Node n)
	{
		if(n.isParent)
		{
			n.passDownEncodedBitsToChildren(n);
			passToLeaves(n.leftChild);
			passToLeaves(n.rightChild);
		}
	}
	
	public void reverseEncodedBits()
	{
		String t = "";
		for(int i=encodedBits.length()-1; i>=0; i--)
		{
			t += encodedBits.charAt(i);
		}
		encodedBits = t;
	}
	
	public Node getChildFromBinary(int i)
	{
		if(i == 0)
		{
			return this.leftChild;
		}
		else
		{
			return this.rightChild;
		}
	}
}
