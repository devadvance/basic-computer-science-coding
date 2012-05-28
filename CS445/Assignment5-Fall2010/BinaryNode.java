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

public class BinaryNode <T> implements java.io.Serializable
{
	private T data;
	private BinaryNode <T> left;
	private BinaryNode <T> right;
	
	
	public BinaryNode ()
	{
		this (null);  // call next constructor
	} // end default constructor
	
	
	public BinaryNode (T dataPortion)
	{
		this (dataPortion, null, null); // call next constructor
	} // end constructor
	
	
	public BinaryNode (T dataPortion, BinaryNode <T> leftChild, BinaryNode <T> rightSibling)
	{
		data = dataPortion;
		left = leftChild;
		right = rightSibling;
	} // end constructor
	
	
	public T getData ()
	{
		return data;
	} // end getData
	
	
	public void setData (T newData)
	{
		data = newData;
	} // end setData
	
	
	public BinaryNode <T> getLeftChild ()
	{
		return left;
	} // end getLeftChild
	
	
	public void setLeftChild (BinaryNode <T> leftChild)
	{
		left = (BinaryNode <T>) leftChild;
	} // end setLeftChild
	
	
	public boolean hasLeftChild ()
	{
		return left != null;
	} // end hasLeftChild
	
	
	public BinaryNode <T> getRightSibling ()
	{
		return right;
	} // end getRightSibling
	
	
	public void setRightSibling (BinaryNode <T> RightSibling)
	{
		right = (BinaryNode <T>) RightSibling;
	} // end setRightSibling
	
	
	public boolean hasRightSibling ()
	{
		return right != null;
	} // end hasLeftChild
	
	
	
	public boolean isLeaf ()
	{
		return (left == null) && (right == null);
	} // end isLeaf
	
	
	public BinaryNode <T> copy()
	{
		BinaryNode <T> newRoot = new BinaryNode <T> (data);
		if (left != null)
			newRoot.left = (BinaryNode <T> ) left.copy ();
		if (right != null)
			newRoot.right = (BinaryNode <T> ) right.copy ();
		return newRoot;
	} // end copy
	
	
	public int getHeight ()
	{
		return getHeight (this); // call private getHeight
	} // end getHeight
	
	
	private int getHeight (BinaryNode <T> node)
	{
		int height = 0;
		if (node != null)
			height = 1 + Math.max (getHeight (node.left),
					getHeight (node.right));
		return height;
	} // end getHeight
	
	
	public int getNumberOfNodes ()
	{
		int leftNumber = 0;
		int rightNumber = 0;
		if (left != null)
			leftNumber = left.getNumberOfNodes ();
		
		if (right != null)
			rightNumber = right.getNumberOfNodes ();
		return 1 + leftNumber + rightNumber;
	} // end getNumberOfNodes
	
} // end BinaryNode