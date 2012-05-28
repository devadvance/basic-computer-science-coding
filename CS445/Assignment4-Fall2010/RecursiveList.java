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
Assignment 4

Notes:
The diagram provided with the assignment shows each recursive list having '(' as the header.
I maintained this format, since it also works with the main class provided with the assignment.
However, the print method does not work in its original form. I modified it to work with the 
diagram version of the recursive list.

*/

import java.util.Scanner;

public class RecursiveList implements Cloneable
{
	private Node head;
	private String slist;
	private int unicount;
	//Added unicount to keep track of the position as the slist is processed. This is extremely important.
	//Otherwise another recursive build method would be needed with an int allowed as input.
	//you may add additional data fields if needed

	//I’m giving you this contructor. -George Novacky
	public RecursiveList()
	{
		head = new Node('(');
	}
	
	//This constructor builds a RecursiveList represented by a string s. You may 
	//assume s is a valid list. -George Novacky
	public RecursiveList(String s)
	{
		slist = s;
		slist = slist.replaceAll(",","");
		
		unicount = 0;
		//System.out.println(slist + " and length: " + slist.length());
		
		if (slist.length() == 1)
			head = new Node(s);
		else
		{
			head = build();
		}
	}

	//Displays this list. That is suppose the RecursiveList shown above is 
	//stored in memory as variable list. To call list.print() displays 
	//(A,(B,C),D). I’m giving you this method for debugging purposes.
	public void print()
	{
		print(head);
	}
	
	//This method is modified to properly print.
	public void print(Node p)
	{
		while(p != null)
		{
			if(p.atom)
			{
				System.out.printf("%c",p.data);
				if(p.next != null)
					System.out.print(',');
			}
			else
			{
				if(p.newlist != null)
				{
					System.out.printf("%c",p.data);
					print(p.newlist);
					System.out.print(')');
				}
				else
					System.out.print("()");
				
				if(p.next != null)
					System.out.print(',');
			}
		
			p = p.next;
		}
	}


	//Builds the Recursive list from the string representation slist. Used to 
	//set variable head = build();
	public Node build()
	{
		Node tempnodeA = null;
		Node tempnodeB = null;
		Node firsttempnode = null;
		
		while (unicount < slist.length())
		{
			if (slist.charAt(unicount) == ')') //ends the current build (reached the end of the inner recursive list)
				break;
			
			if (unicount > 0) //if the list is going past 1 entry; maintains the proper return
			{
				tempnodeA = tempnodeB;
				tempnodeA.next = new Node(slist.charAt(unicount));
				tempnodeB = tempnodeA.next;
				unicount++;
			}
			else
			{
				tempnodeA = new Node(slist.charAt(unicount));
				tempnodeB = tempnodeA;
				firsttempnode = tempnodeA;
				unicount++;
			}
			
			if (tempnodeB.data == '(')
			{
				RecursiveList templist = new RecursiveList(slist.substring(unicount));
				tempnodeB.newlist = templist.head;
				
				unicount += templist.unicount + 1;
				
			}
		}
		return firsttempnode;
	}

	//Clone a RecursiveList
	public Object clone()
	{
		RecursiveList theCopy = null;
		RecursiveList templist = null;
		Node copyNode = null;
		
		try
		{
			theCopy = (RecursiveList)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			System.err.println("RecursiveList cannot clone: "  + e.toString());
		}
		
		theCopy.head = (Node)this.head.clone();
		//System.out.println("RList Head: " + theCopy.head.data);
		copyNode = theCopy.head;
		
		while(copyNode.next != null || copyNode.newlist != null)
		{
			if (copyNode.newlist != null)
			{
				
				templist = new RecursiveList(copyNode.newlist.data.toString());
				
				templist.head = (Node)copyNode.newlist.clone();
				templist = (RecursiveList)templist.clone();
				copyNode.newlist = templist.head;
				//templist.print();
				//System.out.println("End Inner RList");
				
				if (copyNode.next == null)
					break;
			}
			
			if (copyNode.next != null)
			{
				//System.out.println("Next Occurs");
				copyNode.next = (Node)copyNode.next.clone();
				copyNode = copyNode.next;
			}
		}
		
		return theCopy;
	}
	
	//Returns the tail or “cooder” of this list (you will need to use clone()).
	//Uses clone as specified. Also properly creates a new RecursiveList before cloning it. No initialization issues that way.
	public RecursiveList cdr()
	{
		RecursiveList templist = new RecursiveList();
		
		if (head != null && head.newlist != null)
		{
			if (head.newlist == null || head.newlist.next == null)
				return templist;
			else
			{
				templist.head.newlist = this.head.newlist.next;
				return (RecursiveList)templist.clone();
			}
		}
		else
			return templist;
	}
	
	//Returns the head or “car” of this list. The head may or may not be a 
	//RecursiveList that’s why an Object is returned.
	public Object car()
	{
		if (this.head == null || this.head.newlist == null)
			return null;
		if (this.head.newlist.atom == true)
			return this.head.newlist.data;
		else
		{
			RecursiveList templist = new RecursiveList();
			templist.head = this.head.newlist;
			return templist;
		}
	}
	
	//isEmpty method
	public boolean isEmpty()
	{
		if (head != null && head.newlist != null)
			return false;
		else
			return true;
	}
	
	//++++++++++++++++++++
	//BEGIN DRIVER SECTION
	//++++++++++++++++++++
	
	public static void main (String[] args)
	{
		Scanner input = new Scanner(System.in);
		String inputlist;
		
		System.out.println("This driver tests the functions of the RecursiveList class.");
		System.out.print("\nEnter the list in string format (ex: (A,(B,C),D)  ): ");
		inputlist = input.nextLine();
		System.out.println("Results:\n");
		
		RecursiveList list = new RecursiveList(inputlist);
		System.out.print("list = ");
		list.print();
		System.out.println();

		RecursiveList listc = (RecursiveList)list.clone();
		System.out.print("listc (clone of list) = ");
		listc.print();
		System.out.println();


		list.head.newlist.data = '1';
		System.out.print("after change list = ");
		list.print();
		System.out.println();
		System.out.print("listc (clone of list) = ");
		listc.print();
		System.out.println();


		Object carlist = list.car();
		System.out.print("car(list) = ");
		if(carlist instanceof RecursiveList)
		{
			((RecursiveList)carlist).print();
			System.out.println();
		}
		else
		{
			System.out.println((Character)carlist);
		}
		
		RecursiveList cdrlist = list.cdr();
		System.out.print("cdr(list) = ");
		cdrlist.print();
		System.out.println();
		
		System.out.println("length (list) = " + length(list));
		System.out.println("length (cdrlist) = " + length(cdrlist));
		
		
		
		
	}
	
	public static int length(RecursiveList L)
	{
		if(L.isEmpty())
			return 0;
		else
		{	
			return 1 + length(L.cdr());
		}
	}


	public static Object last(RecursiveList L)
	{
		if(L.cdr().isEmpty())
			return L.car();
		else
			return last(L.cdr());
	}
	
	//++++++++++++++++++++
	//END DRIVER SECTION
	//++++++++++++++++++++
	private class Node implements Cloneable
	{
		private Character data;
		private boolean atom;
		private Node next;
		private Node newlist;

		 //If item is a left parenthesis, atom is false; otherwise if item is a letter then atom is true.
		public Node(Object item)
		{
			try
			{
				data = (Character)item;
				if (data == '(')
					atom = false;
				else
					atom = true;
			}
			catch (Exception e)
			{
				
			}
		}

		//clone this Node. NOTE: This is both a deep + shallow clone. The next and newlist fields still link to the ORIGINAL Nodes.
		//Otherwise, this would inadvertently be a recursive clone method, which is NOT desired in this case.
		public Object clone()
		{
			Node theCopy = null;
			
			try
			{
				theCopy = (Node)super.clone();
			}
			catch(CloneNotSupportedException e)
			{
				System.err.println("Node cannot clone: "  + e.toString());
			}
			
			theCopy.data = new Character(this.data);  //Deep clone of the data field
			theCopy.atom = new Boolean(this.atom);  //Deep clone of the atom boolean
			if(this.next != null)
				theCopy.next = this.next;  //Shallow clone. Otherwise it implies recursion which should not be part of the inner Node class.
			if(this.newlist != null)
				theCopy.newlist = this.newlist;  //Same as above; shallow for the same reason.
			return theCopy;
			
		}
	}
}