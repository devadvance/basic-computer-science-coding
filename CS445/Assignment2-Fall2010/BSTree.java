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
Assignment 2
*/

import java.util.Arrays;

public class BSTree<T extends Comparable<? super T>>
{
	//Variable declaration.
	private T[] data;
	private int[][] links;
	private int root;
	private int length;
	boolean unfinished;

	//Constructor allocates memory for an empty binary search tree. Initial array size is 10.
	//Initializes the root and length variables. Root is set to -1 initially to indicate that there is no root node.
	public BSTree()
	{
		root = -1;
		length = 0;
		data = (T[])(new Comparable[10]);
		links = new int[10][2];
	}
	
	//If there is no room is the array-space, expand the space (remember the AList,
	//similar here but more bookkeeping necessary). Insert the item into the tree.
	//See the algorithm on the last page.
	public void add(T item)
	{
		//Check to see if the arrays are full. If so, call the doubleArrays method.
		if (length == data.length)
			doubleArrays();
		
		//If this is the root item, set the correct variables and place it at the root of the tree.
		if (root == -1)
		{
			root = 0;
			data[length] = item;
			links[root][0] = -1;
			links[root][1] = -1;
			length++;
		}
		//If this is not the root item, begin looking for the place to add the new item.
		//Note: this implementation allows duplicates, but does not handle duplicates immediately following the original entry!
		//Duplicate rules were not specified for the assignment but for part 2 there will not be any duplicates (dictionary has no duplicates).
		else
		{
			//Declare and set variables for adding new items.
			int L = root;
			int K = length;
			length++;
			data[K] = item;
			int child = 0;
			
			//Set unfinished to true. It keeps tracks of the loop being done, and allows it to escape after adding the item properly.
			unfinished = true;
			
			//Loop to make sure it checks to the bottom of the tree. Could be done recursively with more coding. Look into.
			do{
				//If the new item is less than the current "root" node, go to the left subtree.
				if (data[K].compareTo(data[L]) <= 0)
				{
					child = 0;
					//System.out.println("Child is left");
				}
				//If the new item is greater than the current "root" node, go to the right subtree.
				if (data[K].compareTo(data[L]) >= 1)
				{
					child = 1;
					//System.out.println("Child is right");
				}
				
				//Check to see if the node has a child. If it does, make that child the new "root" and redo the process.
				//Otherwise, make the new item the child of the current "root" node.
				if (links[L][child] == -1)
				{
					//System.out.println("Stored item number " + K + " as a child of " + L);
					links[L][child] = K;
					links[K][0] = -1;
					links[K][1] = -1;
					unfinished = false;
					L = root;
				}
				else
				{
					L = links[L][child];
				}
			}while(unfinished);
		}
	}
	
	//Method used to double the array space.
	private void doubleArrays()
	{
		//Initialize the temporary arrays.
		T[] tempdata = (T[])(new Comparable[data.length * 2]);
		int[][] templinks = new int[links.length * 2][2];
		
		//First loop. Copies from the data array into the tempdata array.
		for (int i = 0; i < data.length;i++)
		{
			tempdata[i] = data[i];
		}
		
		//References the memory from tempdata to data.
		data = tempdata;
		
		//Second loop. Copies from the links array into the templinks array.
		for (int j = 0; j < links.length; j++)
		{
			for (int k = 0; k < 2; k++)
			{
				templinks[j][k] = links[j][k];
			}
		}
		
		//Refernces the memory from templinks to links.
		links = templinks;
	}
	
	//Display the root, the length, the links, and the data for this tree.
	public void printTree()
	{
		//Prints the title for the links section.
		System.out.println("\nLinks:");
		
		//Outer loop for the height of the links array.
		for (int i = 0; i < length; i++)
		{
			System.out.print(" " + i);
			
			//Inner loop for the width of the links array.
			for (int j = 0; j < 2; j++)
			{
				//Proper spacing to account for negative/positive/big values taking up different widths. Needs better coding.
				//REVISE!
				if (links[i][j] < 0)
					System.out.print(" |  " + links[i][j]);
				else if (links[i][j] >= 1000)
					System.out.print(" |" + links[i][j]);
				else if (links[i][j] >= 100)
					System.out.print(" | " + links[i][j]);
				else if (links[i][j] >= 10)
					System.out.print(" |  " + links[i][j]);
				else
					System.out.print(" |   " + links[i][j]);
			}
			System.out.print("\n");
		}
		
		//Prints the title for the data section.
		System.out.println("\nData:");
		
		//Loop for the data array.
		for (int k = 0; k < length; k++)
		{
			System.out.print(" " + k + " " + data[k] + "\n");
		}
	}
	
	//Return true is item is in this tree; otherwise, return false. Public method to call the private recursive method contains.
	public boolean contains(T item)
	{
		return contains(item, root);
	}
	
	//Private recursive method. Takes an item and an index and checks recursively to see if the items is located in the tree.
	//Check to see about changing from T to Comparable. Otherwise how would this be done?!?!
	private boolean contains(T item, int index)
	{
		if (index == -1)
			return false;
		else if (item.compareTo(data[index]) < 0)
			return contains(item, links[index][0]);
		else if (item.compareTo(data[index]) > 0)
			return contains(item, links[index][1]);
		else if (item.compareTo(data[index]) == 0)
			return true;
		else
			return false;
	}
	
	//Return the height of this tree. The height of tree is the number of levels in the tree. 
	//The height of the tree is the example above is 3. 
	//I suggest this be implemented recursively, see page 681 of our text.
	//Public method to call the private recursive method height.
	public int height()
	{
		return height(root);
	}
	
	//Private recursive height method. Takes an index, checks to see if the node exists by the index being -1 or not.
	//Book uses nodes, which are not used in coding this tree (per the assignment-dictated methods). This is a modified algorithm.
	private int height(int index)
	{
		int height = 0;
		
		//Makes sure that the node exists. If the value from the previous link is -1, then this node does not exist.
		if (index != -1)
			height = 1 + Math.max(height(links[index][0]),height(links[index][1]));

		return height;
	}
	
	//The data can be printed in sorted using what is called an inorder traversal.
	//See page 682 in our textbook for a recursive algorithm.
	//Public method to call the private recursive method inorderTraverse.
	public void inorderTraverse()
	{
		inorderTraverse(root);
	}
	
	//Private recursive inorderTraverse method. Takes an index, checks to see if the node exists by the index being -1 or not.
	//Book uses nodes, which are not used in coding this tree (per the assignment-dictated methods). This is a modified algorithm.
	private void inorderTraverse(int index)
	{
		//Makes sure that the node exists. If the value from the previous link is -1, then this node does not exist.
		if (index != -1)
		{
			//Recursively ventures all the way down to the left, then printing the current node, then ventures all the way down to the right.
			inorderTraverse(links[index][0]);
			System.out.println(data[index]);
			inorderTraverse(links[index][1]);
		}
	}
}