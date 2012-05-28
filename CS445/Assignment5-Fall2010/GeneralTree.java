/**
Copyright (c) 2012, Matt Joseph
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


University of Pittsburgh
CS445
Assignment 5

*/

import java.util.ArrayList; // ArrayList for later use

public class GeneralTree <T>
{
	GTNode<T> root;
	
	// Constructor that creates an empty tree.
	public GeneralTree()
	{
		root = null;
	}
	// Constructor that creates a tree with a root node containing the given data.
	public GeneralTree(T dataitem)
	{
		root = new GTNode(dataitem);
	}
	
	public GeneralTree(T dataitem, ArrayList<GTNode> listitem)
	{
		root = new GTNode(dataitem,listitem);
	}
	
	// Constructor that creates a General Tree from a provided Binary Tree (Left child, right sibling type).
	// Similar code to the Level Order Traverse found in the Binary Tree.
	public GeneralTree(BinaryTree<T> theBinaryTree)
	{
		root = new GTNode(theBinaryTree.getRootNode().getData());
		BinaryNode tempnode = theBinaryTree.getRootNode();
		GTNode tempGTnode;
		GTNode tempGTnodeB;
		LinkedQueue<BinaryNode> q = new LinkedQueue();
		LinkedQueue<GTNode> GTq = new LinkedQueue();
		
		tempGTnode = root;
		
		while (tempnode != null)
		{
			if (tempnode.hasLeftChild())
			{
				q.enqueue(tempnode.getLeftChild());
				tempGTnodeB = new GTNode(tempnode.getLeftChild().getData());
				tempGTnode.addChild(tempGTnodeB);
				GTq.enqueue(tempGTnodeB);
			}
			
			tempnode = tempnode.getLeftChild();
			
			if (tempnode != null && tempnode.hasRightSibling())
			{
				
				while (tempnode.hasRightSibling())
				{
					q.enqueue(tempnode.getRightSibling());
					tempGTnodeB = new GTNode(tempnode.getRightSibling().getData());
					tempGTnode.addChild(tempGTnodeB);
					GTq.enqueue(tempGTnodeB);
					// Change to new right sibling
					tempnode = tempnode.getRightSibling();
					
				}
			}
			tempnode = q.dequeue();
			tempGTnode = GTq.dequeue();
		}
	}
	
	// Given method that prints the General Tree.
	public void printGeneralTree()
	{
		LinkedQueue<GTNode> queue = new LinkedQueue();
		GTNode p = null;
		System.out.printf("\nGeneral Tree Representation\nNode    Children\n");  	     
		queue.enqueue(root);
		while(!queue.isEmpty())
		{
			p = queue.dequeue();
			System.out.printf("%4d   ", p.data);
			for(int k=0; k<p.children.size(); k++)
			{
				queue.enqueue((GTNode)p.children.get(k));			   
				System.out.printf("%4d",((GTNode)p.children.get(k)).data);
			}
			System.out.println();
		}
	}
	
	// Private GTNode class. For use with the General Tree only.
	private class GTNode<T>{
		private T data;
		private ArrayList<GTNode<T>> children;
		
		public GTNode()
		{
			data = null;
			children = new ArrayList();
		}
		
		public GTNode(T dataitem)
		{
			data = dataitem;
			children = new ArrayList();
		}
		
		public GTNode(T dataitem, ArrayList<GTNode<T>> listitem)
		{
			data = dataitem;
			children = listitem;
		}
		
		public ArrayList<GTNode<T>> getChildrenList()
		{
			ArrayList<GTNode<T>> temp;
			temp = (ArrayList<GTNode<T>>)children.clone();
			return temp;
		}
		
		public void addChild (GTNode<T> thenode)
		{
			children.add(thenode);
		}
	}
}