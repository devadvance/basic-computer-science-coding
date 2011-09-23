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
Assignment 1
*/

// Uses a 2D matrix to represent the adjacency matrix of the graph.
// This could have been done with a Edge objects, but in terms of simplifying the process, the matrix was used.

import java.util.Scanner;
import java.io.*;
import java.math.*;

public class Graph implements Cloneable
{
	private int[][] adjMatrix;
	private int numVertex;
	private int numEdges;
	
	// Constructor to create a copy of the input Graph.
	public Graph(Graph G)
	{
		this.numVertex = G.getVertex();
		this.numEdges = G.getEdges();
		this.adjMatrix = G.getMatrix().clone();
		
		
	}
	
	
	public Graph(int v)
	{
		numVertex = v;
		
		// Initialize the adjacency  matrix.
		adjMatrix = new int[numVertex][numVertex];

		// Set all matrix values initially to 0;
		for (int i = 0; i < adjMatrix.length;i++)
		{
			for (int j = 0; j < adjMatrix[i].length;j++)
			{
				adjMatrix[i][j] = 0;
			}
		}
		
		numEdges = 0;
		
	}
	
	
	
	public Graph(String filename) throws FileNotFoundException
	{
		int tempr;
		int tempc;
		
		
		File inputfile = new File(filename);
		Scanner inputfilescan = new Scanner(inputfile);
		numVertex = inputfilescan.nextInt();
		
		// Initialize the adjacency  matrix.
		adjMatrix = new int[numVertex][numVertex];

		// Set all matrix values initially to 0;
		for (int i = 0; i < adjMatrix.length;i++)
		{
			for (int j = 0; j < adjMatrix[i].length;j++)
			{
				adjMatrix[i][j] = 0;
			}
		}
		
		// Set numEdges to 0;
		numEdges = 0;
		
		// Change appropriate matrix values to 1.
		while(inputfilescan.hasNext())
		{
				tempr = inputfilescan.nextInt();
				tempc = inputfilescan.nextInt();
				
				adjMatrix[tempr - 1][tempc - 1] = 1;
				adjMatrix[tempc - 1][tempr - 1] = 1;
				
				// Incremement number of edges.
				numEdges++;
				
		}
	}
	
	// Method to add an edge to the Graph.
	public void addEdge(int i, int j) 
	{
		if (i > 0 && i <= numVertex && j > 0 && j <= numVertex)
		{
			if ((adjMatrix[i - 1][j - 1] == 0) && (adjMatrix[j - 1][i - 1] == 0))
				numEdges++;
			adjMatrix[i - 1][j - 1] = 1;
			adjMatrix[j - 1][i - 1] = 1;
			
		}
	}
	
	// Method to remove an edge from the Graph.
	public void removeEdge(int i, int j)
	{
		if (i > 0 && i <= numVertex && j > 0 && j <= numVertex) {
			adjMatrix[i - 1][j - 1] = 0;
			adjMatrix[j - 1][i - 1] = 0;
			if ((adjMatrix[i - 1][j - 1] == 1) && (adjMatrix[j - 1][i - 1] == 1))
				numEdges++;
		}
	}
	
	// Returns the number of vertices.
	public int getVertex()
	{
		return numVertex;
	}
	
	// Returns the number of edges.
	public int getEdges()
	{
		return numEdges;
	}
	
	// Returns the adjacency matrix of the graph.
	public int[][] getMatrix()
	{
		return (int[][])adjMatrix.clone();
	}
	
	public boolean isEdge(int i, int j)
	{
		if (adjMatrix[i - 1][j - 1] == 1)
			return true;
		else
			return false;
	}
	
	
	// ++Return the union of this graph and other graph.
	public Graph union(Graph G)
	{
		Graph tempGraph = new Graph(numVertex + G.getVertex());
		
		int row = 0;
		int column = 0;
		for (int i = 1;i <= numVertex;i++)
		{
			row++;
			column = 0;
			for (int j = 1;j <= numVertex;j++)
			{
				column++;
				if (adjMatrix[i-1][j-1] == 1)
					tempGraph.addEdge(row,column);
			}
		}
		
		for (int k = 1;k <= G.getVertex();k++)
		{
			row++;
			column = numVertex;
			for (int l = 1;l <= G.getVertex();l++)
			{
				column++;
				if (G.getMatrix()[k-1][l-1] == 1)
					tempGraph.addEdge(row,column);
			}
		}
		
		return tempGraph;
	}
	

	// ++Return the join of this graph and other graph.
	public Graph join(Graph G)
	{
		Graph tempGraph = new Graph(numVertex + G.getVertex());
		
		int row = 0;
		int column = 0;
		for (int i = 1;i <= numVertex;i++)
		{
			row++;
			column = 0;
			for (int j = 1;j <= numVertex;j++)
			{
				column++;
				if (adjMatrix[i-1][j-1] == 1)
					tempGraph.addEdge(row,column);
			}
		}
		
		for (int k = 1;k <= G.getVertex();k++)
		{
			row++;
			column = numVertex;
			for (int l = 1;l <= G.getVertex();l++)
			{
				column++;
				if (G.getMatrix()[k-1][l-1] == 1)
					tempGraph.addEdge(row,column);
			}
		}
		
		for (int x = 1;x <= numVertex;x++)
		{
			for (int y = 1;y <= G.getVertex();y++)
			{
				tempGraph.addEdge(x,y + numVertex);
			}
		}
		
		return tempGraph;
	}
	
	// ++Return the Kronecker Product or tensor product of this graph and
	// ++other graph.
	public Graph tensor(Graph G)
	{
		Graph tempGraph = new Graph(numVertex * G.getVertex());
		int row = 0;
		int column = 0;
		
		for (int i = 1;i <= numVertex;i++)
		{
			
			for (int j = 1;j <= G.getVertex();j++)
			{
				row++;
				column = 0;
				
				for (int k = 1;k <= numVertex;k++)
				{
			
					for (int l = 1;l <= G.getVertex();l++)
					{
						column++;
						if ((adjMatrix[i-1][k-1] == 1) && (G.getMatrix()[j-1][l-1] == 1))
							tempGraph.addEdge(row,column);
					}
				}
			}
		}
		
		return tempGraph;
	}
	
	
	// ++Return the product of this graph and other graph.
	public Graph product(Graph G)
	{
		Graph tempGraph = new Graph(numVertex * G.getVertex());
		
		int row = 0;
		int column = 0;
		for (int i = 1;i <= numVertex;i++)
		{
			
			for (int j = 1;j <= G.getVertex();j++)
			{
				row++;
				column = 0;
				
				for (int k = 1;k <= numVertex;k++)
				{
			
					for (int l = 1;l <= G.getVertex();l++)
					{
						column++;
						
						if ((i == k) && (G.getMatrix()[j-1][l-1] == 1))
							tempGraph.addEdge(row,column);
						
						
						if ((adjMatrix[i-1][k-1] == 1) && (j == l))
							tempGraph.addEdge(row,column);
						
						
					}
				}
			}
		}
		
		return tempGraph;
	}
	
	
	// ++Return the composition of this graph and other graph.
	public Graph composition(Graph G)
	{
		Graph tempGraph = new Graph(numVertex * G.getVertex());
		
		int row = 0;
		int column = 0;
		for (int i = 1;i <= numVertex;i++)
		{
			
			for (int j = 1;j <= G.getVertex();j++)
			{
				row++;
				column = 0;
				
				for (int k = 1;k <= numVertex;k++)
				{
			
					for (int l = 1;l <= G.getVertex();l++)
					{
						column++;
						if (adjMatrix[i-1][k-1] == 1)
							tempGraph.addEdge(row,column);
						
						if ((i == k) && (G.getMatrix()[j-1][l-1] == 1))
							tempGraph.addEdge(row,column);
					}
				}
			}
		}
		
		return tempGraph;
	}	
	
	// This method is used by the chromatic polynomial method. This could be considered contraction, but it suffices for the polynomial.
	// The merged vertex is re-numbered to be the LAST vertex of the new graph, per the examples in class.
	// The remaining vertices use either there original numbering, or the next closest lower number, after the start and end rows/columns are removed.
	public Graph mergeVertices(int start, int end)
	{
		Graph tempGraph = new Graph(numVertex - 1);
		
		for (int i = 1;i <= numVertex;i++)
		{
			
			for (int j = 1;j <= numVertex;j++)
			{
				if (isEdge(i,j))
				{
					
					// Case 1
					if ((i == start) || i == end)
						break;
						
					// Case 2
					else if ((i < start) && (i < end))
					{
						if (j < start && j < end)
							tempGraph.addEdge(i,j);
						else if (j == start || j == end)
							tempGraph.addEdge(i, tempGraph.getVertex());
						else if (j > start && j < end)
							tempGraph.addEdge(i, j -1);
						else if (j > end)
							tempGraph.addEdge(i, j - 2);
					}
					
					//Case 3
					else if (i > start && i < end)
					{
						if (j < start && j < end)
							tempGraph.addEdge(i - 1,j);
						else if (j == start || j == end)
							tempGraph.addEdge(i - 1, tempGraph.getVertex());
						else if (j > start && j < end)
							tempGraph.addEdge(i - 1, j -1);
						else if (j > end)
							tempGraph.addEdge(i - 1, j - 2);
					}
					
					//Case 4
					else if (i > end)
					{
						if (j < start && j < end)
							tempGraph.addEdge(i - 2,j);
						else if (j == start || j == end)
							tempGraph.addEdge(i - 2, tempGraph.getVertex());
						else if (j > start && j < end)
							tempGraph.addEdge(i - 2, j -1);
						else if (j > end)
							tempGraph.addEdge(i - 2, j - 2);
					}
				}
			}
		}
		
		return tempGraph;
	}
	
	public Polynomial chromatic()
	{
		boolean foundEdge = false;
		Polynomial p = new Polynomial();
		int row = 1;
		int column = 1;
		double density;
		
		if (numVertex <= 1)
			density = 0.0;
		else
			density = ((2 * (double)numEdges) / ((double)numVertex * ((double)numVertex - 1)));
		
		
		
		if (density <= .5)
		{
			System.out.println("Using 1st method, density is " + density);
			if (numEdges == 0)
				p = new Polynomial(new BigInteger("" + 1), this.numVertex);
			else
			{
				for (int i = 1;i <= numVertex;i++)
				{
					
					for (int j = 1;j <= numVertex;j++)
					{
						if (isEdge(i,j))
						{
							foundEdge = true;
							column = j;
							j = numVertex + 1;
						}
					}
					
					if (foundEdge)
					{
						row = i;
						i = numVertex + 1;
					}
				}
				
				
				if (foundEdge)
				{
					Graph tempGraph = (Graph)this.clone();
					Graph tempGraphB;
					
					tempGraph.removeEdge(row,column);
					tempGraphB = mergeVertices(row,column);
					p = (tempGraph.Lchromatic()).minus(tempGraphB.Lchromatic());
				}
				else
				{
					p = new Polynomial(new BigInteger("" + 1), this.numVertex);
				}
			}
		}
		
		if (density > .5)
		{
			System.out.println("Using 2st method, density is " + density);
			// Base case
			if ((numEdges == ((numVertex * (numVertex - 1)) / 2)) || (numVertex <= 1))
			{
				for (int x = 0;x <= (numVertex - 1);x++)
				{
					p = p.times(new Polynomial(new BigInteger("" + x).negate()));
				}
				
			}
			else
			{
				for (int i = 1;i <= numVertex;i++)
				{
					for (int j = 1;j <= numVertex;j++)
					{
						if (!isEdge(i,j) && i != j)
						{
							foundEdge = true;
							column = j;
							j = numVertex + 1;
						}
					}
					
					if (foundEdge)
					{
						row = i;
						i = numVertex + 1;
					}
				}
				
				if ((foundEdge) && (row != column))
				{
					Graph tempGraph = (Graph)this.clone();
					Graph tempGraphB;
					
					tempGraph.addEdge(row,column);
					tempGraphB = mergeVertices(row,column);
					p = (tempGraph.Gchromatic()).plus(tempGraphB.Gchromatic());
				}
				else
				{
					for (int x = 0;x <= (numVertex - 1);x++)
					{
						p = p.times(new Polynomial(new BigInteger("" + x).negate()));
					}
				}
			}
			
		}
		return p;
	}
	
	// Recursive method for continuing if the initial graph had a density less than 1/2. Removes an edge from the graph, and uses the mergeVertices method.
	public Polynomial Lchromatic()
	{
		boolean foundEdge = false;
		Polynomial p = new Polynomial();
		int row = 1;
		int column = 1;
		
		if (numEdges == 0)
			p = new Polynomial(new BigInteger("" + 1), this.numVertex);
		else
		{
			
			for (int i = 1;i <= numVertex;i++)
			{
				
				for (int j = 1;j <= numVertex;j++)
				{
					if (isEdge(i,j))
					{
						foundEdge = true;
						column = j;
						j = numVertex + 1;
					}
				}
				
				if (foundEdge)
				{
					row = i;
					i = numVertex + 1;
				}
			}
			
			if (foundEdge)
			{
				Graph tempGraph = (Graph)this.clone();
				Graph tempGraphB;
				
				tempGraph.removeEdge(row,column);
				tempGraphB = mergeVertices(row,column);
				p = (tempGraph.Lchromatic()).minus(tempGraphB.Lchromatic());
			}
			else
			{
				p = new Polynomial(new BigInteger("" + 1), this.numVertex);
			}
		}
		return p;
		
	}
	
	// Recursive method for continuing if the initial graph had a density greater than 1/2. Adds the edge to the graph, and uses the mergeVertices method.
	public Polynomial Gchromatic()
	{
		boolean foundEdge = false;
		Polynomial p = new Polynomial();
		int row = 1;
		int column = 1;
		
		// Base case
		if ((numEdges == ((numVertex * (numVertex - 1)) / 2)) || (numVertex <= 1))
		{
			
			for (int x = 0;x <= (numVertex - 1);x++)
			{
				p = p.times(new Polynomial(new BigInteger("" + x).negate()));
			}
			
		}
		
		else
		{
			for (int i = 1;i <= numVertex;i++)
			{
				for (int j = 1;j <= numVertex;j++)
				{
					// If there is no edge, AND if the start and end points are not the same vertex, then the new edge is good.
					if (!isEdge(i,j) && i != j)
					{
						foundEdge = true;
						column = j;
						j = numVertex + 1;
					}
				}
				
				if (foundEdge)
				{
					row = i;
					i = numVertex + 1;
				}
			}
			
			
			if ((foundEdge) && (row != column))
			{
				Graph tempGraph = (Graph)this.clone();
				Graph tempGraphB;
				
				tempGraph.addEdge(row,column);
				tempGraphB = mergeVertices(row,column);
				// Recursive
				p = (tempGraph.Gchromatic()).plus(tempGraphB.Gchromatic());
				
				
			}
			else
			{
				for (int x = 0;x <= (numVertex - 1);x++)
				{
					p = p.times(new Polynomial(new BigInteger("" + x).negate()));
				}
			
			}
		}
		
		
		return p;
	}
	
	// toString method to allow output/printing of the graph.
	public String toString()
	{
		String tempstring = "";
		int[] degrees = new int[numVertex];
		
		// Set all initially to 0;
		for (int x = 0;x < numVertex;x++)
		{
			degrees[x] = 0;
		}
		
		tempstring += "\nThis graph has " + numVertex + " vertices and " + numEdges + " edges.\n";
		
		// Spacing for matrix.
		tempstring += "\n   ";
		for (int i = 1; i <= numVertex;i++)
		{
			if (i < 10)
				tempstring += "  ";
			if (i < 100 && i >= 10)
				tempstring += " ";
			tempstring += i;
		}
		
		for (int j = 1; j <= numVertex;j++)
		{
			tempstring += "\n";
			if (j < 10)
				tempstring += " ";
			tempstring += j;
			tempstring += "[";
			
			for (int k = 0; k < numVertex;k++)
			{
				tempstring += "  ";
				tempstring += adjMatrix[j - 1][k];
				if (adjMatrix[j - 1][k] == 1)
					degrees[j-1] = degrees[j-1] + 1;
			}
			
			tempstring += "]";
		}
		
		tempstring += "\n\nver | deg\n";
		for (int y = 1;y <= numVertex;y++)
		{
			// Adding the vertex to the string.
			if (y < 10)
				tempstring += "  " + y + " |";
			if (y < 100 && y >= 10)
				tempstring += " " + y + " |";
			
			// Adding the edge count to the string.
			if (degrees[y - 1] < 10)
				tempstring += "  " + degrees[y - 1];
			if (degrees[y - 1] < 100 && degrees[y - 1] >= 10)
				tempstring += " " + degrees[y - 1];
			
			tempstring += "\n";
		}
		
		tempstring += "----|----\n";
		

		return tempstring;
		
	}
	
	// Clone method. Necessary for the adding/removing of edges so that the original graph is undisturbed!
	public Object clone()
	{
		Graph theCopy = null;
		
		try
		{
			theCopy = (Graph)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			System.err.println("Graph cannot clone: "  + e.toString());
		}
		
		// Create a clone of the same size, then add edges via the addEdge method to the clone, reading from this graph's adjacency matrix.
		theCopy = new Graph(this.numVertex);
		for (int i = 1;i <= numVertex;i++)
		{
			for (int j = 1;j <= numVertex;j++)
			{
				if (this.isEdge(i,j))
					theCopy.addEdge(i,j);
			}
		}
		
		return theCopy;
	}
}