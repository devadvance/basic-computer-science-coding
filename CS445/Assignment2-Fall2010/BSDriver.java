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

import java.util.*;

public class BSDriver
{
	public static void main (String[] args)
	{
		Scanner input = new Scanner(System.in);
		BSTree<String> testtree = new BSTree<String>();
		String checking;
		
		System.out.print("How many items do you want to enter: ");
		int numitems = Integer.parseInt(input.nextLine());
		
		System.out.println("Please input " + numitems + " items.");
		
		for (int i = 1; i<=numitems;i++)
		{
		System.out.print("Enter item number " + i + ": ");
		testtree.add(input.nextLine());
		}
		
		testtree.printTree();
		System.out.println("\nHeight is: " + testtree.height() + "\n");
		System.out.println("inorderTraverse is: \n");
		testtree.inorderTraverse();
		
		System.out.println("\nTesting the contains method:\n");
		
		while(true)
		{
			System.out.print("What do you want to check? Type exit! to end. : ");
			checking = input.nextLine();
			if (checking.equals("exit!"))
				break;
			System.out.println("Does it contain " + checking + "? " + testtree.contains(checking));
			
		}

		
	}
}