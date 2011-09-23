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
CS401
Assignment 2
*/

/** ************************* BallPlayer.java *************************************************
*** This is the BallPlayer class which is used to create BallPlayer a BallPlayer object. 	***
***********************************************************************************************/

public class BallPlayer
{
		// Declaring variables
		int numyear;
		private int numhits,numsingles,numdoubles,numtriples,numhomeruns,numatbats;
		private double batavg;
		String firstname,lastname;
		
		// Constructor
		public BallPlayer(String fname, String sname, int yr)
		{
			firstname = fname;
			lastname = sname;
			numyear = yr;
		}
		
		public int year()
		{
			return numyear;
		}
		
		public void setHits(int h)
		{
			numhits = h;
		}
		
		public int hits()
		{
			return numhits;
		}
		
		public void setSingles(int s)
		{
			numsingles = s;
		}
		
		public int singles()
		{
			return numsingles;
		}
		
		public void setDoubles(int d)
		{
			numdoubles = d;
		}
		
		public int doubles()
		{
			return numdoubles;
		}
		
		public void setTriples(int t)
		{
			numtriples = t;
		}
		
		public int triples()
		{
			return numtriples;
		}
		
		public void setHomeRuns(int h)
		{
			numhomeruns = h;
		}
		
		public int homeRuns()
		{
			return numhomeruns;
		}
		
		public void setAtBats(int a)
		{
			numatbats = a;
		}
		
		public int atBats()
		{
			return numatbats;
		}
		
		public double battingAverage()
		{
			return ((double)numhits / (double)numatbats);
		}
		
		public double sluggingPercentage()
		{
			return (((4 * (double)numhomeruns) + (3 * (double)numtriples) + (2 * (double)numdoubles) + (double)numsingles) / ((double)numatbats));
		}
		
		public String printableName()    
		{
			return (lastname + ", " + firstname.charAt(0) + ".");
		}
		
		public static void heading()
		{
			System.out.println("Player          YR   AB   S   D   T  HR    BA     SP");
		}
		
		public void display()
		{
			
			System.out.printf("%-15s %4d %-4d %-3d %-3d %-2d %-5d %-6.3f %-4.3f \n",printableName(),numyear,atBats(),singles(),doubles(),triples(),homeRuns(),battingAverage(),sluggingPercentage());
			
		}
		
		public int compareTo(BallPlayer other) // implement Comparable interface
		{
			if ((other.sluggingPercentage()) > sluggingPercentage())
				return -1;
			if ((other.sluggingPercentage()) == sluggingPercentage())
				return 0;
			if ((other.sluggingPercentage()) < sluggingPercentage())
				return 1;
			return -2;
		}
}