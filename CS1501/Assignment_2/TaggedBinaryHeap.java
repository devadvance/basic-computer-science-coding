/**
Copyright (c) 2011, encryptstream
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
CS1501
Assignment 2
*/

//Note: v + 1 is used in many placed because while there can be a vertex 0 outside of the TBH, the TBH does not support a vertex at HeapLoc 0.
//This is because the formula used for the children is 2i and 2i+1, which does not work if the parent is at location 0!!!

public class TaggedBinaryHeap
{
	//Default capacity of the heap
	private static final int INITIAL_CAPACITY = 20;
	//Number of elements currently in the heap
	private int numElements;
	//Heap array
	private int[] heap;
	//Vertex to weight mapping
	private int[] VertexToWeight;
	//Vertex to heap location
	private int[] VertexToHeapLoc;
	//Used to reset the heap if the last element is being removed.
	private int MaxElements;
	
	//Constructor that takes no options, uses the default capacity which is 20.
	public TaggedBinaryHeap()
	{
		this(INITIAL_CAPACITY);
	}
	
	//Constructor that takes an integer value for the initial size of the TBH.
	//This will be the most frequently used constructor.
	public TaggedBinaryHeap(int initcapacity)
	{
		numElements = 0;
		MaxElements = initcapacity;
		heap = new int[initcapacity + 1];
		VertexToWeight = new int[initcapacity + 1];
		VertexToHeapLoc = new int[initcapacity + 1];
	}
	
	//Insert method. Takes a vertex v and weight/priority.
	public void insert(int v, int weight)
	{
		v = v + 1;
		// Debug
		// System.out.println("(TBH)Insert v (" + v + ") with weight w (" + weight + ")");
		//In reviewing how PQs are done, it seems that the best option is to treat it like a maxHeap and use negative values.
		//That way upheap and downheap are done normally.
		VertexToWeight[v] = -weight;
		// Debug code. Comment out unless needed.
		// System.out.println("Inserting V: " + (v - 1) + " with weight: " + weight);
		heap[++numElements] = v;
		VertexToHeapLoc[v] = numElements;
		upheap(v); 
	}
	
	//change method. Use the VertexToHeapLoc array to locate the vertex, then change the value of its weight in the VertexToWeight array.
	//upheap goes first, followed by downHeap. Neither may actually move the item.
	public void change(int v, int NewWeight)
	{
		v = v + 1;
		// Debug code. Comment out unless needed.
		// System.out.println("(TBH)Change v (" + v + ") to NewWeight w (" + NewWeight + ")");
		int HeapLoc = VertexToHeapLoc[v];
		VertexToWeight[v] = -NewWeight;
		
		if (VertexToWeight[heap[HeapLoc/2]] < (-NewWeight))
			upheap(v);
		else
			downHeap(v);
	}
	
	//remove method. After removing, shift the bottom leaf to the top spot, delete the leaf, and then downHeap if needed.
	public WeightedEdge remove()
	{
		
		int v = heap[1];
		int w = -VertexToWeight[v];
		// Debug code. Comment out unless needed.
		// System.out.println("(TBH)Remove v (" + v + ") with weight w (" + w + ")");
		
		VertexToHeapLoc[v] = 0;
		if (numElements == 1)
		{
			heap = new int[MaxElements + 1];
			numElements = 0;
			VertexToHeapLoc = new int[MaxElements + 1];
			VertexToWeight = new int[MaxElements + 1];
		}
		else
		{
			heap[1] = heap[numElements--];
			VertexToHeapLoc[heap[1]]=1;
			downHeap(heap[1]);
		}
		//Uses WeightedEdge. A new binary pair object type could be created, but WeightedEdge can be substituted and is just as relevant.
		WeightedEdge vw = new WeightedEdge(v - 1,w);
		return vw;
		
	}
	
	// Checks against the VertexToHeapLoc array to see if it contains a vertex. If it is equal to zero then it does not exist in the TBH.
	public boolean contains(int v)
	{
		v = v + 1;
		if (VertexToHeapLoc[v] != 0)
			return true;
		else
			return false;
	}
	
	public int getPlace(int v)
	{
		v = v + 1;
		return VertexToHeapLoc[v];
	}
	
	//downHeap method. Shift an item down in the heap. Used when removing an item. Note that it may not actually shift anything.
	public void downHeap(int v)
	{
		
		int k = VertexToHeapLoc[v];
		int j;
		int tempInt = heap[k];
		
		while (k <= numElements/2)
		{
			j = k+k;
			if (j<numElements && VertexToWeight[heap[j]] <  VertexToWeight[heap[j+1]])
				j++;

			if (VertexToWeight[tempInt] >= VertexToWeight[heap[j]])
				break;
			

			VertexToHeapLoc[heap[j]]=k;
			heap[k] = heap[j]; 
			k = j;
		}

		heap[k] = v;
		VertexToHeapLoc[v]=k;

	}
	
	public void upheap(int v)
	{
		if (numElements != 1)
		{
			
			int temp;
			temp = VertexToWeight[v];
			int k = VertexToHeapLoc[v];
			// Debug code. Comment out unless needed.
			// System.out.println("(TBH)Upheap Start with v (" + v + ") and HeapLoc of v k (" + k + ") and a weight (" + temp + ")");
			while ( VertexToWeight[ heap[k/2] ] <= temp)
			{
				// Debug code. Comment out unless needed.
				// System.out.println("(TBH)Upheap Comparing with heap[k/2] (" + heap[k/2] + ") and HeapLoc of VertexToHeapLoc[heap[k/2]] k (" + VertexToHeapLoc[heap[k/2]] + ") and a weight (" + temp + ")");
				// System.out.println("(TBH)Upheap Comparing VertexToWeight[ heap[k/2] ] (" + VertexToWeight[ heap[k/2] ] + ") and temp (" + temp + ")");
				VertexToHeapLoc[heap[k/2]] = k;
				heap[k] = heap[k/2];
				k = k/2;
				if (k == 0)
					break;
				
			}
			
			heap[k] = v;
			VertexToHeapLoc[v] = k;
			// Debug code. Comment out unless needed.
			// System.out.println("(TBH)Upheap End with v (" + v + ") and HeapLoc of v k (" + k + ") and a weight (" + temp + ")");
			
		}
	}
	
	//isEmpty method. 
	public boolean isEmpty()
	{
		if (numElements == 0)
			return true;
		else
			return false;
	}
	
}