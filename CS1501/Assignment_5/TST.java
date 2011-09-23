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
Assignment 5
*/

import java.util.*;
import java.io.*;
import java.lang.*;

public class TST
{
	/**
	* Varible declaration.
	*/
	private Node root; // Root node of the TST. Set to the first character of the first string when added.
	public ArrayList<String> TST_AL = new ArrayList<String>(); // Contains all words (NO DUPLICATES) in the order that they were added.
	
	/**
	* Initializes the TST. Sets the root Node to an empty Node.
	*/
	public TST()
	{
		Node root = new Node();
	}
	
	/**
	* Does a search to see if the TST contains a specific string.
	* This method works whether there is an asterisk(s) or not in the string. If not, it reduces to contains()
	* @return ArrayList<String> containing Strings with either a prefix or the found string, designated by the occurence of the termination character '!'
	*/
    public ArrayList<String> match(String s)
    {
        ArrayList<String> matchAL = new ArrayList<String>();
        ArrayList<Integer> asterPositions = new ArrayList<Integer>();
        int[] offset;
        
		// Find all of asterisks, if any.
        for(int position = 0;position <= s.length() - 1;position++)
        {
            if (s.charAt(position) == '*')
                asterPositions.add(position);
        }
        
        int numAster = asterPositions.size();
        
		// Only do this if there is at least one asterisk in the string.
        if (numAster >= 1)
        {
            int totalLoops = (int)Math.pow((double)26,(double)numAster);
            int currentNumloops = 1;
            offset = new int[asterPositions.size()];
            for (int i =0;i <offset.length;i++)
                offset[i] = 0;
            
            StringBuilder modified = new StringBuilder("");
            modified.append(s);
            
            while(currentNumloops <= totalLoops)
            {
				// This part changes what the asterisks are going to stand for.
				// It works like an odometer, where once the last * position gets to 26, it changes the one before it, and it moves to the left.
                for (int x = numAster - 1;x > 0; x--)
                {
                    if (currentNumloops % ((Math.pow((double)26, (double)numAster - x))) == 0)
                    {
                        offset[x] = 0;
                        offset[x - 1]++;
                    }
                }
                
				// Changes the * to an upper case letter depending on the current value in the offset array.
                for (int y = 0;y <= numAster - 1;y++)
                    modified.setCharAt(asterPositions.get(y), (char)(65 + offset[y]));
                
				String tem = modified.toString();
                matchAL.add(contains(modified.toString()));
                currentNumloops++;
                offset[numAster - 1]++;
            }
        }
        else
            matchAL.add(contains(s)); // match reduces to just a single contains call if there are no asterisks in the entire string.
        
		// Return the ArrayList of contains returns.
        return matchAL;
    }
    
	/**
	* Wrapper method for the recursive contains method.
	* @return If passed nothing, returns '.', else it returns the prefix of what was found or the word string and a termination character.
	*/
	public String contains(String s)
	{
		if ((s == null) || (s.compareTo("") == 0))
			return "."; // returns this if contains was passed nothing.
		else
			return contains (s, 0, root);
	}
	
	/**
	* Does a search to see if the TST contains a specific string.
	* This method is recursive.
	* @return String containing either a prefix or the found string, designated by the occurence of the termination character '!'
	*/
	private String contains(String s, int position, Node currentNode)
	{
		if (((s.length() - 1) == position) && (s.charAt(position) == currentNode.nodeCharVal) && (currentNode.isTerminationNode == true)) // If this is the last character in the string and it matches
			return currentNode.nodeCharVal + "!"; // Includes a termination character if the search is done, and this is a termination and not just a prefix
        else if (((s.length() - 1) == position) && (s.charAt(position) == currentNode.nodeCharVal))
            return "" + currentNode.nodeCharVal;
		else if ((s.charAt(position) < currentNode.nodeCharVal) && (currentNode.left != null))
			return contains(s, position, currentNode.left);
		else if ((s.charAt(position) == currentNode.nodeCharVal) && (currentNode.middle != null)) // If it matches but is not the last character in the string
			return currentNode.nodeCharVal + contains(s, position + 1, currentNode.middle);
		else if ((s.charAt(position) > currentNode.nodeCharVal) && (currentNode.right != null))
			return contains(s, position, currentNode.right);
		else if (s.charAt(position) == currentNode.nodeCharVal)
			return "" + currentNode.nodeCharVal; // Returns just the character because it matched, but its only a prefix so there's no termination character
		else
			return ""; // Returns nothing because it was not matched, and there is nothing left to search for the current character
	}
	
	/**
	* Wrapper insert() calls the recursive insert below.
	*/
	public void insert(String s)
	{
		String tempString;
		
		if (!((s == null) || (s.compareTo("") == 0)))
		{
		
			if ((root == null))
			{
				root = new Node(s.charAt(0));
				insert(s, 0, root);
                TST_AL.add(s);
			}
			else
			{
				tempString = contains(s);
				if ((tempString == null) || (tempString.compareTo("") == 0) || !(tempString.charAt(tempString.length() - 1) == '!'))
				{
					insert(s, 0, root);
					TST_AL.add(s);
				}
				
			}
		}
	}
	
	/**
	* Inserts a new string recursively.
	*/
	private void insert(String s, int position, Node currentNode)
	{
		if (!((s.length()) == position))
		{
			if ((s.charAt(position) < currentNode.nodeCharVal))
			{
				if (currentNode.left == null)
					currentNode.left = new Node(s.charAt(position));
				insert(s, position, currentNode.left);
			}
			else if ((s.charAt(position) == currentNode.nodeCharVal))
			{
				if (!((s.length() - 1) == position))
				{
					if (currentNode.middle == null)
						currentNode.middle = new Node(s.charAt(position + 1));
					insert(s, position + 1, currentNode.middle);
				}
                else currentNode.isTerminationNode = true;
			}
			else if ((s.charAt(position) > currentNode.nodeCharVal))
			{
				if (currentNode.right == null)
					currentNode.right = new Node(s.charAt(position));
				insert(s, position, currentNode.right);
			}
		}
	}
	
	/**
	* Private Node class for use in this TST only.
	*/
	private class Node
	{
		public char nodeCharVal;
        public boolean isTerminationNode;
		public Node left, middle, right;
		
		public Node()
		{}
		
		public Node(char val)
		{
			nodeCharVal = val;
            isTerminationNode = false;
		}
		
	}
}