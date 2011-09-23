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
Assignment 3
*/

import java.io.*;
import java.util.*;
import java.text.*;

public class Compress
{
    public static void main(String[] args)
	{
		try
		{
			String outputName;
			
			// Checks to see if there is a file extension.
			if (args[0].charAt(args[0].length() - 4) == '.')
				outputName = args[0].substring(0, args[0].length() - 4);
			else
				outputName = args[0];
			
			File outputfilename = new File(outputName + ".zip");
			FileWriter output = new FileWriter(outputfilename);
			
			if (args.length >= 2 && (args[1].compareTo("echo") == 0))
			{
				String[] tempStrings = echoOutput(args[0]);
				System.out.println(tempStrings[0]);
				output.write(tempStrings[1]);
			}
			else
				output.write(output(args[0]));
			
			output.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("You entered an invalid file name!");
		}
		catch (IOException e)
		{
			System.out.println("An IOException occured...uh oh.");
		}
	}
	
	// output method. This method simply returns a String containing the compressed data.
	// No other informatinon, including whitespace or delimiters, are added.
	public static String output(String nameOfFile) throws FileNotFoundException, IOException
	{
		
		File inputfilename = new File(nameOfFile);
		FileReader reader = new FileReader(inputfilename);
		
		int tempint;
		String outputString = "";
		
		HuffmanTree hTree = new HuffmanTree();
		
		// Read the next int into a temporary integer variable.
		tempint = reader.read();
		while(tempint != -1)
		{
			outputString += hTree.add((char)tempint);
			tempint = reader.read();
		}
		
		reader.close();
		return outputString;
	}
	
	
	// echoOutput method. This method returns a String array.
	// Index of 0 contains a String of information along with the compressed output.
	// Index of 1 contains a String containing the compressed data only, no additional data or spaces.
	public static String[] echoOutput(String nameOfFile) throws FileNotFoundException, IOException
	{
		File inputfilename = new File(nameOfFile);
		FileReader reader = new FileReader(inputfilename);
		
		DecimalFormat ratioFormat = new DecimalFormat("#0.0");
		
		int tempint;
		double compressionRatio = 0;
		int tempTransmitBitCount = 0;
		int tempReadBitCount = 0;
		
		String outputString = "";
		String tempString = "";
		
		String[] returnOutput = new String[2];
		returnOutput[1] = "";
		
		HuffmanTree hTree = new HuffmanTree();
		
		// Read the next int into a temporary integer variable.
		tempint = reader.read();
		while(tempint != -1)
		{
			// Set tempstring equal to proper representation of current character.
			tempString = hTree.add((char)tempint);
			
			// Update the overall string
			returnOutput[1] += tempString;
			
			tempTransmitBitCount = tempTransmitBitCount + tempString.length();
			// NOTE: The tempReadBitCount is added to by 8. This should be done by its actual number of bits, given by Integer.toBinaryString(tempint).length()
			// However, this method does not compute the leading zeros. We are also assuming that the ASCII value must occupy 1 byte (8 bits) so therefore
			// this logic is sound.
			tempReadBitCount = (tempReadBitCount + 8);
			compressionRatio = (1 - ((double)tempTransmitBitCount / (double)tempReadBitCount)) * 100;
			
			outputString += (tempString + "\nbits read = " + tempReadBitCount + "\nbits transmitted = " + tempTransmitBitCount + "\ncompression ratio = " + ratioFormat.format(rounded(compressionRatio)) + "\n");
			
			tempint = reader.read();
		}
		
		// Final values.
		outputString += ("\nFinal:\nbits read = " + tempReadBitCount + "\nbits transmitted = " + tempTransmitBitCount + "\ncompression ratio = " + ratioFormat.format(rounded(compressionRatio)) + "\n");
		
		// Change index 0 reference to be the final outputString.
		returnOutput[0] = outputString;
		
		reader.close();
		return returnOutput;
	}
	
	// Round method. DecimalFormat does not round, it simple chops off the end of the double. Therefore, this method is used to ensure proper output.
	public static double rounded(double doubleToRound)
	{
		double temp = doubleToRound * 10;
		temp = Math.round(temp);
		temp = temp / 10;
		return temp;
	}
	
}