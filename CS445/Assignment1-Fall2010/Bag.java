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
Assignment 1
*/

import java.util.ArrayList;

public class Bag<T>
{
	private ArrayList<T> store;
	private ArrayList<Integer> frequency;
	private int tempindex;
	private int tempfreq;
	
	
	public Bag()
	{
		store = new ArrayList<T>();
		frequency = new ArrayList<Integer>();
	}
	
	
	public void add(T item)
	{
		
		// Check to see if the item already exists
		if (store.contains(item))
		{
			//Use the index of the item to increase the frequency counter
			
			tempindex = store.indexOf(item);
			tempfreq = frequency.get(tempindex);
			frequency.set(tempindex,tempfreq + 1);
		}
		else
		{
			store.add(item);
			frequency.add(1);
			
		}
	}
	
	
	public boolean remove(T item)
	{
		if (store.contains(item))
		{
			tempindex = store.indexOf(item);
			tempfreq = frequency.get(tempindex);
			
			if (tempfreq == 1)
			{
				frequency.remove(tempindex);
				store.remove(tempindex);
			
			}
			else

				frequency.set(tempindex,tempfreq - 1);
			
			return true;
		}
		else
			return false;
	}
	
	
	public int frequency(T item)
	{
		if (store.contains(item))
		{
			tempindex = store.indexOf(item);
			return frequency.get(tempindex);
		}
		else
			return 0;
	}
	
	
	public int size()
	{
		int tempsize = 0;
		
		for(int i = 0;i < frequency.size();i++)
			tempsize = tempsize + frequency.get(i);

		return tempsize;
	}
	
	
	public ArrayList<T> collectItemsWithFrequency(int ... freq)
	{
		
		ArrayList<T> itemswithfreq = new ArrayList<T>();
		
		int i;
		
		for(i = 0;i < freq.length;i++)
		{
			for(int j = 0;j < frequency.size();j++)
			{
				if (frequency.get(j) == freq[i])
					itemswithfreq.add(store.get(j));
			}
		}
		return itemswithfreq;
	}
	
	
	public ArrayList<Object> get(int index)
	{
		ArrayList<Object> IndexAL = new ArrayList<Object>();
		
		if (index < store.size())
		{
			IndexAL.add(store.get(index));
			IndexAL.add(frequency.get(index));
			return IndexAL;
		}
		else
			return null;
	}
	
	
	public String toString()
	{
		String tempString = "[";
		
		if (store.size() > 0)
		{
			for (int i = 0;i < store.size();i++)
			{
				if (i != 0)
					tempString = tempString.concat(", ");
				
				tempString = tempString.concat("[").concat(String.valueOf(store.get(i))).concat(", ").concat(String.valueOf(frequency.get(i))).concat("]");
			}
			
			tempString = tempString.concat("]");
			return tempString;
		}
		
		else
			return null;
			
	}
}