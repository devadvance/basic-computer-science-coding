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

public class Node
{
    private Node parent;
    private int position;
    private int weight;
    private int label;
    private int character;
	
	// Begin constructors ****************************
    public Node()
	{
        this.parent = null;
        position = 0;
        weight = 0;
        label = 0;
		character = -1;
    }
	
	public Node(int pos, Node par, int w, int l)
	{
		position = pos;
		parent = par;
		weight = w;
		label = l;
	}
	
	public Node(int pos, Node par, int w, int l, int c)
	{
		position = pos;
		parent = par;
		weight = w;
		label = l;
		character = c;
	}
	// End constructors ******************************
	
	// Begin accessor methods ************************
	public void setPosition(int pos){
		position = pos;
	}
	public void setWeight(int w){
		weight = w;
	}
	public void setLabel(int l){
		label = l;
	}
	public void setCharacter(int c){
		character = c;
	}
	public void setParent(Node par){
		parent = par;
	}
	// End accessor methods **************************
	
	// Begin mutator methods *************************
	public int getPosition(){
		return position;
	}
	public int getWeight(){
		return weight;
	}
	public int getLabel(){
		return label;
	}
	public int getCharacter(){
		return character;
	}
	public Node getParent(){
		return parent;
	}
	// End mutator methods ***************************
	
	// Methods for future use or debugging purposes.
	public String toString()
	{
		String output = "<" + position + "," + parent.getPosition() + "," + weight + "," + label + "," + character + ">";
		return output;
	}
}