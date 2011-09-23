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

// GraphDriver for the Graph class. Alerts the user if improper formatting is used when calling this program.


import java.util.*;
import java.io.*;

public class GraphDriver
{
	public static void main (String[] args)
	{
		Polynomial tempPoly;
		int tempInt;
		Graph graphG = new Graph(1);
		Graph graphH = new Graph(1);
		
		// This try includes more than one exception to catch; see below for the different catches.
		try
		{
			graphG = new Graph(args[0]);
			graphH = new Graph(args[1]);
		
		
			// Create FileWriter and BufferedWriter.
			FileWriter fstream = new FileWriter("output.txt");
			BufferedWriter output = new BufferedWriter(fstream);
			
			System.out.println("========================================");
			System.out.println("All console output is for debugging/algorithm verification purposes ONLY.\nSee output.txt for generated output!");
			System.out.println("========================================\n");
			
			// Graph G
			System.out.println("\nInfo Regarding Graph G");
			output.write("\n\n\nGraph G:");
			output.write(graphG.toString());
			output.flush();
			tempPoly = graphG.chromatic();
			output.write("\nChromatic Polynomial P(G, x) = " + tempPoly);
			tempInt = chromaticNum(tempPoly);
			output.write("\n\nChromatic Number = " + tempInt);
			
			
			// Graph H
			System.out.println("\nInfo Regarding Graph H");
			output.write("\n\n\nGraph H:");
			output.write(graphH.toString());
			output.flush();
			tempPoly = graphH.chromatic();
			output.write("\nChromatic Polynomial P(H, x) = " + tempPoly);
			tempInt = chromaticNum(tempPoly);
			output.write("\n\nChromatic Number = " + tempInt);
			
			
			// Graph G U H (Union)
			System.out.println("\nInfo Regarding Graph G U H (Union)");
			output.write("\n\n\nGraph G U H (Union):");
			output.write(graphG.union(graphH).toString());
			output.flush();
			tempPoly = graphG.union(graphH).chromatic();
			output.write("\nChromatic Polynomial P(G U H, x) = " + tempPoly);
			tempInt = chromaticNum(tempPoly);
			output.write("\n\nChromatic Number = " + tempInt);
			
			
			// Graph G + H (Join)
			System.out.println("\nInfo Regarding Graph G + H (Join)");
			output.write("\n\n\nGraph G + H (Join):");
			output.write(graphG.join(graphH).toString());
			output.flush();
			tempPoly = graphG.join(graphH).chromatic();
			output.write("\nChromatic Polynomial P(G + H, x) = " + tempPoly);
			tempInt = chromaticNum(tempPoly);
			output.write("\n\nChromatic Number = " + tempInt);
			output.flush();
			
			// Graph G o H (Kronecker Product)
			System.out.println("\nInfo Regarding Graph G o H (Kronecker Product):");
			output.write("\n\n\nGraph G o H (Kronecker Product):");
			output.write(graphG.tensor(graphH).toString());
			output.flush();
			tempPoly = graphG.tensor(graphH).chromatic();
			output.write("\nChromatic Polynomial P(G o H, x) = " + tempPoly);
			tempInt = chromaticNum(tempPoly);
			output.write("\n\nChromatic Number = " + tempInt);
			output.flush();
			
			// Graph G[H] (Composition)
			System.out.println("\nInfo Regarding Graph G[H] (Composition):");
			output.write("\n\n\nGraph G[H] (Composition):");
			output.write(graphG.composition(graphH).toString());
			output.flush();
			tempPoly = graphG.composition(graphH).chromatic();
			output.write("\nChromatic Polynomial P(Graph G[H], x) = " + tempPoly);
			tempInt = chromaticNum(tempPoly);
			output.write("\n\nChromatic Number = " + tempInt);
			output.flush();
			
			// Graph H[G](Composition)
			System.out.println("\nInfo Regarding Graph H[G] (Composition):");
			output.write("\n\n\nGraph H[G](Composition):");
			output.write(graphH.composition(graphG).toString());
			output.flush();
			tempPoly = graphH.composition(graphG).chromatic();
			output.write("\nChromatic Polynomial P(Graph H[G], x) = " + tempPoly);
			tempInt = chromaticNum(tempPoly);
			output.write("\n\nChromatic Number = " + tempInt);
			output.flush();
			
			// Graph G x H (Product)
			System.out.println("\nInfo Regarding Graph G x H (Product):");
			output.write("\n\n\nGraph G x H (Product):");
			output.write(graphG.product(graphH).toString());
			output.flush();
			tempPoly = graphG.product(graphH).chromatic();
			output.write("\nChromatic Polynomial P(G x H, x) = " + tempPoly);
			tempInt = chromaticNum(tempPoly);
			output.write("\n\nChromatic Number = " + tempInt);
			
			// Close the stream.
			output.close();
			
		}
		catch (FileNotFoundException e)
		{
			System.out.println("You entered an invalid file name. Please re-run with a proper pair of file names.");
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println("You did not enter enough file names. Please re-run with a proper pair of file names.");
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public static int chromaticNum(Polynomial poly)
	{
		int number = 0;
		int tempvalue;
		boolean found = false;
		
		while (!found)
		{
			number++;
			tempvalue = poly.evaluate(number).intValue();
			if (tempvalue >= 1)
				found = true;
		}
		
		return number;
	}
}