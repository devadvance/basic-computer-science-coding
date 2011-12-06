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
Assignment 3
*/

public class HuffmanTree
{
	public static Node[] characters;
    public Node[] rlo;
    public Node p;
    public Node NYT;
    
	// Default constructor.
    public HuffmanTree()
	{
		// Declare arrays.
		characters = new Node[256];
		rlo = new Node[256];
		
		// Create NYT.
		NYT = new Node(255,null,0,0,-1);
		rlo[255] = NYT;
		
		// Set p to NYT initially.
		p = NYT;
    }
    
	// Checks to see if it is in the tree.
	public boolean characterInTree(char c)
	{
        return this.characters[(int)c] == null;
	}
	
	// Adds a character to the tree. Checks to see if swaps are needed as well.
    public String add(char c)
	{
		String outputString = "";
		Node newInternal = null;
		Node newLeaf = null;
		
        // Checks to see if the character is already in the tree. Increases weight by 1 if it is.
		if(!characterInTree(c)){
			characters[(int)c].setWeight(characters[(int)c].getWeight() + 1);
			outputString = getCodeWordForCharacter(c);
		}
		
		else{
			// Create new nodes.
			newInternal = new Node(NYT.getPosition(),NYT.getParent(),0,0,-1);
			newLeaf = new Node(NYT.getPosition() - 1,newInternal,1,1,(int)c);
			
			// Update NYT
			NYT.setPosition(NYT.getPosition() - 2);
			NYT.setParent(newInternal);
			
			// Relocate nodes in rlo
			rlo[NYT.getPosition()] = NYT;
			rlo[newInternal.getPosition()] = newInternal;
			rlo[newLeaf.getPosition()] = newLeaf;
			
			// Location mapping
			characters[(int)c] = newLeaf;
			
			// Locate p to correct node
			p = newLeaf.getParent();
			
			// Add ASCII value to outputString since it was not previously in the tree.
			// Also includes the NYT code word at the beginning.
			outputString = getCodeWordForNYT();
			outputString += Integer.toBinaryString((int)c);
			//System.out.println("Integer.toBinaryString(tempint)" + Integer.toBinaryString((int)c) + " and outputstring=" + outputString);
			
		}
		
		// Update p.
		p = characters[(int)c];
		int swapTarget = p.getPosition();
		for(int i = p.getPosition();i <= 254;i += 2){
			if (rlo[i].getWeight() < rlo[p.getPosition()].getWeight())
				swapTarget = i;
		}
		if (swapTarget != p.getPosition())
			charSwap(rlo[p.getPosition()],rlo[swapTarget]);
			
		// Update the weights going all the way up the tree to the root node.
		// Update p.
		p = characters[(int)c].getParent();
		while(p != null)
		{
			p.setWeight(p.getWeight() + 1);
			p = p.getParent();
		}
		
		// Check to see if any sibling swaps need to be done.
		for(int i = NYT.getPosition() + 3;i <= 254;i += 2)
		{
			if (rlo[i].getWeight() < rlo[i - 1].getWeight())
				siblingSwap(rlo[i],rlo[i - 1]);
		}
		
		return outputString;
    }
	
	// Swap for if the nodes have different parents.
	public void charSwap(Node lowerNode, Node higherNode)
	{
		int tempCharacter = lowerNode.getCharacter();
		int tempWeight = lowerNode.getWeight();
		
		lowerNode.setCharacter(higherNode.getCharacter());
		lowerNode.setWeight(higherNode.getWeight());
		
		higherNode.setCharacter(tempCharacter);
		higherNode.setWeight(tempWeight);
		
		characters[lowerNode.getCharacter()] = rlo[lowerNode.getPosition()];
		characters[higherNode.getCharacter()] = rlo[higherNode.getPosition()];
	}
	
	// Swap for if the nodes havethe same parent.
	public void siblingSwap(Node leftNode, Node rightNode)
	{
		int tempCharacter = leftNode.getCharacter();
		int tempWeight = leftNode.getWeight();
		
		leftNode.setCharacter(rightNode.getCharacter());
		leftNode.setWeight(rightNode.getWeight());
		
		rightNode.setCharacter(tempCharacter);
		rightNode.setWeight(tempWeight);
		
		// Update character array depending on which node was a character and which node was an internal node.
		// -1 implies an internal node.
		if (leftNode.getCharacter() != -1)
		{
			characters[leftNode.getCharacter()] = leftNode;
			rlo[rightNode.getPosition() - 1].setParent(rightNode);
			rlo[rightNode.getPosition() - 2].setParent(rightNode);
		}
		if (rightNode.getCharacter() != - 1)
		{
			characters[rightNode.getCharacter()] = rightNode;
			rlo[leftNode.getPosition() - 2].setParent(leftNode);
			rlo[leftNode.getPosition() - 3].setParent(leftNode);
		}
	}
	
	// Returns the code word for the character in the tree. Should be used before updating locations.
	public String getCodeWordForCharacter(char c)
	{
		String outputString = "";
		Node tempNode = characters[(int)c];
		
		while(tempNode.getParent() != null)
		{
			outputString = tempNode.getLabel() + outputString;
			tempNode = tempNode.getParent();
		}
		return outputString;
	}
	
	// Returns the code word for the NYT node in the tree. Should be used before updating locations.
	public String getCodeWordForNYT()
	{
		String outputString = "";
		Node tempNode = NYT;
		
		while(tempNode.getParent() != null)
		{
			outputString = tempNode.getLabel() + outputString;
			tempNode = tempNode.getParent();
		}
		
		return outputString;
	}
}