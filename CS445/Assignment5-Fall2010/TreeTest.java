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

// Tests both the Binary Tree and the General Tree.

import java.util.Stack;
import java.util.EmptyStackException;
import java.util.Scanner;

public class TreeTest
{
	public static void main(String[] args)
	{
		BinaryTree<Integer> testTree;
		char[] Lnode;
		char[] Rnode;
		BinaryNode[] b;
		Stack S = new Stack();
		String testData;
		
		Scanner input = new Scanner(System.in);
		
		System.out.println("This program tests both the Leftmost-child Right-Sibling and General Trees.");
		System.out.print("Enter encoded tree using proper syntax [ EX: ..(.()...)..()().) ]  ");
		testData = input.nextLine();
		
		
		int N = (testData.length() / 2);
		
		Lnode = new char[N];
		Rnode = new char[N];
		b = new BinaryNode[N];
		
		for (int i = 0; i <= (N - 1);i++)
		{
			Rnode[i] = testData.charAt(2 * i);
			Lnode[i] = testData.charAt(2 * i + 1);
			b[i] = new BinaryNode(i + 1);
			
		}
		
		// info stored in char arrays
		
		BinaryNode tempNode;
		
		for (int i = 0; i <= N - 1;i++)
		{
			if (Rnode[i] == '(')
			{
				S.push(b[i]);
			}
			
			if (Lnode[i] == ')')
			{
				try
				{
				tempNode = (BinaryNode)S.pop();
				tempNode.setRightSibling(b[i + 1]);
				}
				catch (EmptyStackException e)
				{
				}
			}
			else
				b[i].setLeftChild(b[i + 1]);
		}
		
		testTree = new BinaryTree(b[0]);
		
		System.out.println("\nNumber of nodes is: " + testTree.getNumberOfNodes() + "\n");
		testTree.printLevelOrderTraverse();
		
		GeneralTree<Integer> testGT = new GeneralTree(testTree);
		testGT.printGeneralTree();
	}
}