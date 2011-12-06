/**
Copyright (c) 2011, Matt Joseph
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

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ArrayList;

public class LabeledGraph
{
	//maps a label to the numerical value of a vertex
	private Hashtable<String, Integer> table;
	
	//holds all the unique labels in the graph G
	private String[] keys;
	
	//class implementing a directed graph via the adjacency list form
	//DO NOT USE the adjacency matrix form
	private WeightedGraph G;
	
	public LabeledGraph(int INTIAL_CAPACITY)
	{
		G = new WeightedGraph(INTIAL_CAPACITY);
		keys = new String[INTIAL_CAPACITY];
		table = new Hashtable<String, Integer>();
	}
	
	public LabeledGraph()
	{
		G = new WeightedGraph(10);
		keys = new String[10];
		table = new Hashtable<String, Integer>();
	}
	
	private String[] doubleArray()
	{
		String[] NewKeys = new String[2 * keys.length];
		System.arraycopy(keys, 0, NewKeys, 0, keys.length);
		return NewKeys;
		
	}
	
	public int getVertID(String vert)
	{
		return table.get(vert);
	}
	
	public void addEdge(String s, String d, int w)
	{
		int temps,tempd;
		addVertex(s);
		addVertex(d);
		
		temps = getVertID(s);
		tempd = getVertID(d);
		
		
		G.addEdge(temps,tempd,w);
	}
	
	public void addVertex(String vert)
	{
		Integer tempInt = table.get(vert);
		if (tempInt == null)
		{
			if (G.getNumVertex() >= keys.length)
				keys = doubleArray();
			tempInt = G.getNumVertex();
			keys[tempInt] = vert;
			table.put(vert,tempInt);
			//Debug
			//System.out.println(tempInt);
			G.addVertex(tempInt);
		}
	}
	
	public String toString()
	{
		
		String tempString = G.toString();
		
		tempString += "\n\nKeys:";
		
		for (int i = 0; i < G.getNumVertex();i++)
			tempString += "\n" + i + ":" + keys[i];
		
		return tempString;
	}
	
	//returns Dijkstra information. Chose to return as opposed to print because the information is then easier to handle.
	public String Dijkstra(int v)
	{
		String tempString = "\n\nDistance from " + v + " (" + keys[v] + ") to vertex:";
		int[] tempIntArr = G.Dijkstra(G, v);
		
		for (int k = 0;k < tempIntArr.length;k++)
		{
			tempString += "\n" + k + " : " + tempIntArr[k];
		}
		
		
		tempString += "\n";
		
		ArrayList tempAL;
		
		for (int m = 0;m < G.getNumVertex();m++)
		{
			if (m != v)
			{
				tempAL = G.getPath(v,m);
				
				for(int n = 0;n < tempAL.size();n++)
				{
					if (n == 0)
						tempString += "\n" + keys[(Integer)tempAL.get(n)];
					else
						tempString += "->" + keys[(Integer)tempAL.get(n)];
				}
			}
		}
			
		return tempString;
	}
}