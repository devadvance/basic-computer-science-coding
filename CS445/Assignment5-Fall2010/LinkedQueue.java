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

// Linked Queue as used in class discussion.

public class LinkedQueue <T> implements java.io.Serializable
{
	private Node firstNode; // references node at front of queue
	private Node lastNode;  // references node at back of queue

	public LinkedQueue ()
	{
		firstNode = null;
		lastNode = null;
	} // end default constructor


	public void enqueue (T newEntry)
	{
		Node newNode = new Node (newEntry, null);
		if (isEmpty ())
			firstNode = newNode;
		else
			lastNode.setNextNode (newNode);
		lastNode = newNode;
	} // end enqueue


	public T getFront ()
	{
		T front = null;
		if (!isEmpty ())
			front = firstNode.getData ();
		return front;
	} // end getFront


	public T dequeue ()
	{
		T front = null;
		if (!isEmpty ())
		{
			front = firstNode.getData ();
			firstNode = firstNode.getNextNode ();
			if (firstNode == null)
				lastNode = null;
		} // end if
		return front;
	} // end dequeue


	public boolean isEmpty ()
	{
		return (firstNode == null) && (lastNode == null);
	} // end isEmpty


	public void clear ()
	{
		firstNode = null;
		lastNode = null;
	} // end clear

	// Private node class for use with the LinkedQueue only!
	// Created to work with the provided LinkedQueue class.
	private class Node implements java.io.Serializable
	{
		private T data; // entry in queue
		private Node next; // link to next node
		
		// Constructor that creates an empty node.
		public Node ()
		{
			data = null;
			next = null;
		}
		
		// Constructor that creates a node based on data.
		public Node (T dataitem)
		{
			data = dataitem;
			next = null;
		}
		
		// Constructor that creates a node based on data as well as a reference to the next Node.
		public Node (T dataitem, Node nextitem)
		{
			data = dataitem;
			next = nextitem;
		}
		
		// Returns the next Node.
		public Node getNextNode()
		{
			return next;
		}
		
		// Sets the next Node.
		public void setNextNode(Node thenext)
		{
			next = thenext;
		}
		
		// Gets the data of the Node.
		public T getData()
		{
			return data;
		}
		
		// Sets the data of the Node.
		public void setData(T thedata)
		{
			data = thedata;
		}

	}
} // end LinkedQueue