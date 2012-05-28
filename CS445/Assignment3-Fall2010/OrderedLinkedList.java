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
Assignment 3
*/

//Built from scratch due to the assignment not specifying whether inheritance was allowed or not. Uses nodes for better data organization.
public class OrderedLinkedList<T extends Comparable<? super T>>
{
	private Node<T> firstNode; // reference to first node
	private int length;    // number of entries in list
	
	public OrderedLinkedList()
	{
		firstNode = null;
		length = 0;
	}
	
	public int getLength()
	{
		return length;
	}
	
	public void add(T newData)
	{
		Node<T> newNode = new Node<T>(newData);
		
		if (firstNode == null)
		{
			firstNode = newNode;
			length++;
		}
		else
		{
			boolean placed = false;
			Node<T> currentNode = firstNode;
			Node<T> previousNode = null;
			
			while (!placed)
			{
				if ((newData.compareTo(currentNode.getData()) < 0))
				{
					newNode.setNext(previousNode.getNext());
					previousNode.setNext(newNode);
					length++;
					placed = true;
				}
				else if (currentNode.getNext() == null)
				{
					currentNode.setNext(newNode);
					length++;
					placed = true;
				}
				else
				{
					previousNode = currentNode;
					currentNode = currentNode.getNext();
				}
			}
		}
	}
	
	public T remove(int itemnumber) //only works if the list is currently being used
	{
		T returnvalue = null;
		
		if (length == 0)
			return returnvalue;
		
		if (length > 0 && (itemnumber >= 1) && (itemnumber <= length))
		{
			if (itemnumber == 1)
			{
				returnvalue = firstNode.getData();
				firstNode = null;
			}
			else                           // case 2: itemnumber > 1
			{
				Node<T> nodeBefore = getNodeAt(itemnumber-1);
				Node<T> nodeToRemove = nodeBefore.getNext();
				Node<T> nodeAfter = nodeToRemove.getNext();
				nodeBefore.setNext(nodeAfter);
				returnvalue = nodeToRemove.getData();
			}
			
			length--;
		}

		return returnvalue;     
	}
	
	//Modified from normal LList
	private Node getNodeAt(int itemnumber)
	{
		Node<T> currentNode = firstNode;
		
		if (currentNode == null)
			return null;
		
		for (int i = 1; i < itemnumber; i++)
			currentNode = currentNode.getNext();
		
		return currentNode;
    }
	
	//return -1 if not found, otherwise it returns the item number in the list. 
	//Only returns the first instance of an item if there are duplicates
	public int find(T newData)
	{
	 	boolean found = false;
		int itemnumber = -1;
		int i = 0;
		
	 	Node<T> currentNode = firstNode;
		i++;
		
	 	while (!found && (currentNode != null))
		{
			if (newData.compareTo(currentNode.getData()) == 0)
			{
				itemnumber = i;
				found = true;
			}
			else
			{
				currentNode = currentNode.getNext();
				i++;
			}
		}
		
		return itemnumber;
    }
	
	//Modified from normal LList
	public void display()
	{
	    Node<T> currentNode = firstNode;

	    while(currentNode != null)
		{
			System.out.println(currentNode.getData());
			currentNode = currentNode.getNext();
		}
	}
	
	//returns -1 if there are no duplicates. If there are, it returns the item number of the first duplicate.
	public int firstDuplicate()
	{
		int itemnumber = -1;
		int i = 1;
		boolean found = false;
		T currentData = null;
		Node<T> currentNode = firstNode;
		Node<T> nextNode = currentNode.getNext();
		T nextData = null;
		
		if (currentNode == null)
			return itemnumber;
		
		while (i <= length && !found && currentNode.getNext() != null)
		{
			currentData = currentNode.getData();
			nextNode = currentNode.getNext();
			nextData = nextNode.getData();
			
			if (currentData.compareTo(nextData) == 0)
			{
				itemnumber = i + 1;
				found = true;
			}
			else
			{
				i++;
				currentNode = nextNode;
			}
		}
		
		return itemnumber;
	}
	
	public T getData(int itemnumber)
	{
		Node<T> currentNode = getNodeAt(itemnumber);
		return currentNode.getData();
	}
	
	/**public String toString()
	{
		String tempstring = null;
		Node<T> currentNode = firstNode;

	    while(currentNode != null)
		{
			tempstring.concat(currentNode.getData());
			currentNode = currentNode.getNext();
		}
		
		return tempstring;
	}*/
	
	
}