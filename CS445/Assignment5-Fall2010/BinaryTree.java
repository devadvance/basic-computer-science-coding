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

public class BinaryTree <T> implements java.io.Serializable
{
	private BinaryNode<T> root;
	
	// Constructor that uses a previously created node as the root node of the tree.
	public BinaryTree (BinaryNode <T> rootNode)
	{
		setRootNode(rootNode);
	}
	
	// Constructor that creates an empty tree.
	public BinaryTree ()
	{
		root = null;
	}
	
	// Constructor that uses root data to set the root node.
	public BinaryTree (T rootData)
	{
		root = new BinaryNode <T> (rootData);
	}
	
	// Constructor that uses two trees as subtrees.
	public BinaryTree (T rootData, BinaryTree <T> leftTree, BinaryTree <T> rightTree)
	{
		privateSetTree (rootData, leftTree, rightTree);
	}
	
	// Sets the tree using data.
	public void setTree (T rootData)
	{
		root = new BinaryNode <T> (rootData);
	}
	
	public void setRootNode(BinaryNode<T> thenode)
	{
		root = thenode.copy();
	}
	
	// Calls the next private method.
	public void setTree (T rootData, BinaryTree <T> leftTree, BinaryTree <T> rightTree)
	{
		privateSetTree (rootData, (BinaryTree <T> ) leftTree,(BinaryTree <T> ) rightTree);
	}
	
	// Private method that uses two trees as subtrees.
	private void privateSetTree (T rootData, BinaryTree <T> leftTree, BinaryTree <T> rightTree)
	{
		root = new BinaryNode <T> (rootData);
		if ((leftTree != null) && !leftTree.isEmpty ())
			root.setLeftChild (leftTree.root);
		if ((rightTree != null) && !rightTree.isEmpty ())
		{
			if (rightTree != leftTree)
				root.setRightSibling (rightTree.root);
			else
				root.setRightSibling (rightTree.root.copy ());
		} // end if
		if ((leftTree != null) && (leftTree != this))
			leftTree.clear ();
		if ((rightTree != null) && (rightTree != this))
			rightTree.clear ();
	}
	
	
	
	// Returns the height of the tree. Uses the getHeight method of the nodes themselves.
	public int getHeight ()
	{
		return root.getHeight ();
	}
	
	// Returns the number of nodes in the tree.
	public int getNumberOfNodes ()
	{
		return root.getNumberOfNodes ();
	}
	
	// Get the data from the root node.
	public T getRootData ()
	{
		T rootData = null;
		if (root != null)
			rootData = root.getData ();
		return rootData;
	}
	
	// Checks to see if the tree is empty by checking for the existence of the root node.
	public boolean isEmpty ()
	{
		return root == null;
	}
	
	// Clears the tree by dereferencing the root node.
	public void clear ()
	{
		root = null;
	}
	
	// Public method to get the root node of the tree.
	// This will be used by any other external method for purposes such as converting tree type.
	public BinaryNode <T> getRootNode ()
	{
		return root;
	}
	
	// This method prints a Level Order Traverse in tabular format.
	// Uses a LinkedQueue (external class).
	public void printLevelOrderTraverse()
	{
		String tempstring = "Node\tLeftMostChild\tRightSiblings of LeftMostChild\n";
		LinkedQueue<BinaryNode> q = new LinkedQueue();
		BinaryNode tempnode = root;
		
		// If the temporary node is not null.
		while (tempnode != null)
		{
			tempstring += "  ";
			tempstring += tempnode.getData();
			if (tempnode.hasLeftChild())
			{
				q.enqueue(tempnode.getLeftChild());
				tempstring += "\t\t";
				tempstring += (tempnode.getLeftChild().getData()).toString();
			}
			
			tempnode = tempnode.getLeftChild();
			
			// If the temporary node is not null and has a right sibling.
			if (tempnode != null && tempnode.hasRightSibling())
			{
				tempstring += "\t";
				
				while (tempnode.hasRightSibling())
				{
					q.enqueue(tempnode.getRightSibling());
					tempstring += "   ";
					tempstring += (tempnode.getRightSibling().getData()).toString();
					// Change to new right sibling.
					tempnode = tempnode.getRightSibling();
					
				}
			}
			tempnode = q.dequeue();
			tempstring += "\n";
		}
		// Print the table after it has been constructed.
		System.out.print(tempstring);
	}
}