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

public class BagDriver
{
	public static void main(String[] args)
	{
		Bag<String> bag = new Bag();
		bag.add("M"); bag.add("Y");
		bag.add("M"); bag.add("P");
		bag.add("M"); bag.add("Y");
		bag.add("B"); bag.add("S");
		bag.add("S"); bag.add("M");
		bag.add("S"); bag.add("B");
		System.out.println("bag = " + bag + "\n");
		System.out.println("size of bag = " + bag.size() + "\n");
		System.out.println("frequency of Y = " + bag.frequency("Y") + "\n");
		System.out.println("frequency of Z = " + bag.frequency("Z") + "\n");
		System.out.println("The items with frequency 2 are " +
		bag.collectItemsWithFrequency(2) + "\n");
		System.out.println("The union of the items with frequency 1 and 4 are " +
		bag.collectItemsWithFrequency(1,4) + "\n");
		bag.remove("Y");
		bag.remove("P");
		bag.remove("M");
		System.out.println("bag = " + bag + " after removing Y, P and M\n");
		System.out.println("The item and frequency at index 3 is " + bag.get(3) + "\n");
		System.out.println("The item and frequency at index 9 is " + bag.get(9) + "\n");
	}
}