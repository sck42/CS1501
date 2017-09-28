import java.io.*;

public class DLB
{
	private Node root; 
	public DLB()
	{
		root = new Node(' ', -1, null, null); 
	}
	
	public boolean add(String s, double index)
	{
		char[] word = s.toCharArray(); 
		boolean done = false; 
		Node currentNode = root; 
		Node newNode; 
		
		for(int i = 0; i < word.length; i++)
		{
			//We make a down entry when its the first word in the DLB or when adding a new word's letter. 
			if(currentNode.down == null)
			{
				newNode = new Node(word[i]); 
				currentNode.down = newNode; 
				currentNode = currentNode.down; 
			}
			else
			{
				currentNode = currentNode.down; 
				if(currentNode.c != word[i])
				{
					if(currentNode.right == null)
					{
						//We make a right entry whenever we need to make an addition to a prefix. 
						newNode = new Node(word[i]); 		
						currentNode.right = newNode; 
						currentNode = currentNode.right; 
					}
					else
					{
						//Find the place where prefix ends. 
						breakloop:
						while(currentNode.right != null)
						{
							currentNode = currentNode.right; 
							if(currentNode.c == word[i])
							{
								break breakloop; 
							}
							if(currentNode.right == null)
							{ 
								newNode = new Node(word[i]); 
								currentNode.right = newNode; 
								currentNode = currentNode.right;
							}
						}
					}
				} 
			}
			//Add the time to the DLB. 
			if(i == word.length - 1 && currentNode.down == null)
			{
				newNode = new Node(' ', index); 				
				currentNode.down = newNode; 
				currentNode = currentNode.down;
				done = true; 
			}
			else if(i == word.length - 1 && currentNode.down != null)
			{
				currentNode = currentNode.down;
				while(currentNode.right != null)
				{
					currentNode = currentNode.right; 
				}
				newNode = new Node(' ', index); 				
				currentNode.right = newNode; 
				currentNode = currentNode.right;
				done = true; 
			}
			
		}
		return done; 
	}
	public double find(String s)
	{
		char[] word = s.toCharArray(); 
		Node currentNode = root; 
		currentNode = currentNode.down; 
		//Look at each character of the word. 
		for(int i = 0; i < word.length; i++)
		{
			whileloop:
			while(currentNode.down != null || currentNode.right != null)
			{
				//Go right if current character doesn't match and Right node is avaiable. 
				if(currentNode.c != word[i] && currentNode.right != null)
				{
					currentNode = currentNode.right; 
				}
				//Can't go right and current character isn't so word isn't in DLB. Return -1
				else if(currentNode.c != word[i] && currentNode.right == null)
				{ 
					return -1; 
				}
				//Find the place where the character matches. 
				else if(currentNode.c == word[i])
				{
					currentNode = currentNode.down; 
					break whileloop;
				}
			}
		}
		//Find the last node of the word and return the time. 
		if(currentNode.down == null)
		{
			return currentNode.val; 
		}
		else
		{
			
			while(currentNode.right != null)
			{
				currentNode = currentNode.right; 
				if(currentNode.c == ' ')
				{
					return currentNode.val; 
				}
			}
		}
		return -1; 
	}
	//Create Node class. 
	private class Node
	{
		private char c; 
		private double val = -1.0; 
		private Node right; 
		private Node down; 
		private Node(char a)
		{
			c = a; 
			val = -1; 
			right = null; 
			down = null; 
		}
		private Node(char a, double x)
		{
			this(a, x, null, null); 
		}
		private Node(char a, double x, Node rightNode, Node downNode)
		{
			c = a; 
			val = x; 
			right = rightNode; 
			down = downNode; 
		}
		
	}
}