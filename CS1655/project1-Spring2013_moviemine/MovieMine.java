/*
Copyright (c) 2013, Matt Joseph
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
CS1655
Assignment 1 - moviemine
Spring Semester 2013
*/

import java.io.FileNotFoundException;

public class MovieMine {
	
	private static final int POSITIVE = 0;
	private static final int NEGATIVE = 1;
	private static final int COMBINATION = 2;
	
	private static final String INSTRUCT = "Arguments must be in the format:" +
			"\n java MovieMine -minsup XXX -minconf YYY -maxmovies ZZZ -pos" +
			"\n XXX - the minSup as a decimal between 0 and 1" +
			"\n YYY - the minConf as a decimal between 0 and 1" +
			"\n ZZZ - max movies per rule" +
			"\n -pos/-neg/-comb - which table to use";
	
	public static void main (String[] args) {
		AprioriDatabase database = null;
		
		System.out.println("Arguments entered:");
		for (String temp : args) {
			System.out.println(temp);
		}
		
		int tableChoice;
		
		if ((args.length < 7) || (args.length > 7)) {
			System.out.println("Invalid number of arguments entered.\n" + INSTRUCT);
		}
		else if ((!args[0].equalsIgnoreCase("-minsup")) || (!args[2].equalsIgnoreCase("-minconf")) || (!args[4].equalsIgnoreCase("-maxmovies"))) {
			// The right number of arguments, but they weren't correct
			System.out.println("Invalid arguments entered.\n" + INSTRUCT);
		}
		else {
			if ((args[6].equalsIgnoreCase("-neg"))) {
				tableChoice = NEGATIVE;
			}
			else if ((args[6].equalsIgnoreCase("-pos"))) {
				tableChoice = POSITIVE;
			}
			else if ((args[6].equalsIgnoreCase("-comb"))) {
				tableChoice = COMBINATION;
			}
			else {
				// The table selection was bad
				System.out.println("Invalid table selection.\n" + INSTRUCT);
				return;
			}
			
			try {
				database = new AprioriDatabase("u.data", Double.parseDouble(args[1]), Double.parseDouble(args[3]), Integer.parseInt(args[5]), tableChoice);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			database.run();
		}
	}
}
