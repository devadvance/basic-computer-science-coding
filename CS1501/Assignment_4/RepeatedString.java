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
Assignment 4
*/

import java.util.*;
import java.io.*;

public class RepeatedString
{
	// Declare charAccess outside of any methods so that it is able to be used regardless of what method needs to increment it.
	// Some of the search methods require multiple methods which all incremement charAccess.
	// Long is necessary since int may be too short for files over 200K.
	private static long charAccess;
	
	public static void main(String[] args)
	{
		try
		{
			Scanner scan = new Scanner(System.in);
			String input;
			int searchMethod = 0;
			int inputMethod = 0;
			char[] charArray = new char[0];
			
			charAccess = 0;
			System.out.println("\n\n++++++++++REPEATED STRING SEARCH++++++++++");
			
			while((inputMethod != 1) && (inputMethod != 2))
			{
				System.out.print("Enter 1 to input a file name, or 2 for manual input: ");
				inputMethod = scan.nextInt();
			}
			
			if (inputMethod == 1) // File input.
			{
				System.out.print("Please enter a file to search in: ");
				input = (String)scan.next();
				
				System.out.print("Loading file...");
				charArray = convertToCharArray(input, null);
				System.out.println("done!");
			}
			if (inputMethod == 2) // Manual input.
			{
				System.out.println("Please enter the text you want to search in:");
				input = scan.next();
				charArray = convertToCharArray(null, input);
			}
			
			while((searchMethod != 1) && (searchMethod != 2) && (searchMethod != 3) && (searchMethod != 4))
			{
				System.out.println("++++++++++++++++++++++++++++++++++++++++++");
				System.out.println("1 - Brute Force\n2 - Boyer-Moore\n3 - Suffix Search\n4 - Unoptimized Suffix Search (Extra Credit)");
				System.out.print("Enter a search type (1,2,3,4): ");
				searchMethod = scan.nextInt();
			}
			
			if (searchMethod == 1)
				Brute(charArray);
			if (searchMethod == 2)
				BoyerMoore(charArray);
			if (searchMethod == 3)
				algorithm3(charArray);
			if (searchMethod == 4)
				algorithm3unop(charArray);
			
		}
		catch(FileNotFoundException e)
		{
			System.out.println("\n\nYou entered an invalid file name!");
		}
		catch (IOException e)
		{
			System.out.println("\n\nAn IOException occured...uh oh!");
		}
		catch (OutOfMemoryError e)
		{
			System.out.println("\n\nOutOfMemoryError! You tried using too large of a file for this algorithm!");
		}
		catch (Exception e)
		{
			System.out.println("\n\nSome unknown error occurred. Please contact support :)");
		}
	}
	
	// Brute force method. This is not exactly the same as on the assignment, and has been optimized slightly.
	// It will skip checking patterns shorter than the current longest in some cases. Thus, runtime is reduced 25/26 times.
	private static void Brute(char[] charArray)
	{
		System.out.println("\nPlease note that the Brute Force method is optimized slightly.\nOnce a repeated substring has been found, it starts by checking the next loop at the position of the max substring found.\nThis way, 25/26 times, it will fail faster.");
		System.out.print("\nStarting search...");
		StopWatch sw = new StopWatch();
		sw.start();
		
		int maxL = 0;
		int maxR = 0;
		int maxLength = 0;
		for (int L = 0; L < charArray.length - 1; L++) //For L = 1 to n do 
		{
			for (int R = L + 1; R < charArray.length - 1; R++) //For R = 1 to n do
			{
				for (int K = 0; K < charArray.length - R; K++)
				{
					
					charAccess++;
					if ((maxLength + R > charArray.length - 1))
						break;
					if (charArray[L + maxLength] != charArray[R + maxLength])
						break;
					if (charArray[L + K] != charArray[R + K])
					{
						if (K > maxLength)
						{
							maxLength = K;
							maxL = L;
							maxR = R;
						}
						break;
					}
				}
			}
		}
		String maxString = String.copyValueOf(charArray,maxL,(maxLength));
		
		sw.stop();
		System.out.println("done!");
		
		System.out.println("\n1) Algorithm 1 was used.\n2) Length of longest match is " + maxString.length() + "\n3) The longest matching string is \"" + maxString + "\"\n4) Start position of first occurence is " + maxL + "\n5) Start position of second occurence is " + maxR);
		System.out.println("6) Total number of character accesses is " + charAccess);
		System.out.println("7) Average character accesses per character in the file is " + charAccess / (long)charArray.length);
		System.out.println("\nTotal run time: " + formatSeconds(sw.read()));
		
		sw.reset();
	}
	
	/** Begin the section of methods devoted to the Boyer-Moore search type. 
	This includes the actual algorithm, as well as generating the good and bad tables, and the search itself.
	Runtime would be improved if arraycopy and copyValueOf were not used.
	But for the sake of simplicity, and the fact that it would not improve it by much, I kept it this way.
	*/
	// Makes the bad shift array
	private static int[] makeBad(char[] tempString)
	{
		int[] badcharArray = new int[256];
		char[] charArray = tempString;
		
		for (int i = 0; i < 256;i++)
			badcharArray[i] = -1;
		
		for (int j = charArray.length - 1; j >= 0; j--)
		{
			if (badcharArray[(int)charArray[j]] == -1)
			{
				badcharArray[(int)charArray[j]] = charArray.length - j - 1;
				charAccess++;
			}
		}
		
		int tempint = -1;
		
		for (int k = charArray.length - 2; k >= 0;k--)
		{
			if (charArray[k] == charArray[charArray.length - 1])
				tempint = k;
		}
		badcharArray[(int)charArray[charArray.length - 1]] = charArray.length - tempint - 1;
        
        for(int p = 0; p < badcharArray.length - 1;p++)
        {
            if (badcharArray[p] == -1)
                badcharArray[p] = tempString.length;
        }
		
		return badcharArray;
	}
	
	// Makes the good shift array
	private static int[] makeGood(char[] tempString)
	{
		int[] goodArray = new int[tempString.length];
		char[] charArray = tempString;
		char[] temporary;
		boolean found = false;
		int tempShiftVal;
		
		for (int i = charArray.length - 1; i >= 0;i--)
		{
			temporary = String.copyValueOf(charArray, i, charArray.length - i).toCharArray();
			found = false;
			tempShiftVal = 0;
			
			for (int j = 1; j <= charArray.length;j++)
			{
				charAccess++;
				// In the case that you are just checking the last character in the charArray
				if ((temporary.length == 1) && (temporary[0] != charArray[charArray.length - j]))
				{
					found = true;
					tempShiftVal = j;
					break;
				}
				else if (((i - j) >= 0) && ((String.copyValueOf(charArray, i - j + 1, temporary.length - 1)).compareTo(String.copyValueOf(temporary, 1, temporary.length - 1)) == 0) && (temporary[0] != charArray[i - j]))
				{
					found = true;
					tempShiftVal = j;
					break;
				}
				else if (((i - j) < 0) && (String.copyValueOf(temporary, 0 - (i - j), temporary.length + (i - j))).compareTo(String.copyValueOf(charArray, 0, temporary.length + (i - j))) == 0)
				{
					found = true;
					tempShiftVal = j;
					break;
				}
			}
			
			if (found)
					goodArray[i] = tempShiftVal;
			else
				goodArray[i] = charArray.length;
		}
		
		return goodArray;
	}
    
    // Performs the actual BoyerMoore search
    private static int search(char[] pattern, char[] charArray)
    {
		boolean found = false;
		int returnPosition = -1;
		int currentPosition = 0;
		int badCharPosition = -1;
		char[] patternArray = pattern;
        
		int[] badArray = makeBad(pattern);
		int[] goodArray = makeGood(pattern);
        
        
        while(!found) // while the pattern has not been found
        {
            if (currentPosition + pattern.length > charArray.length) // make sure it doesn't try to check past the end of the charArray
                break;
            
            else // check to see if the pattern matches
            {
                
                badCharPosition = -1; // this refers to the bad character position in regards to the comparison between the pattern and the current text substring thats being searched
                
                for(int c = patternArray.length - 1;c >= 0;c--)
                {
					charAccess++;
                    if ( patternArray[c] != charArray[currentPosition + c] )
                    {
                        badCharPosition = c;
                        break;
                    }
                }
                
                if (badCharPosition == -1)
                    found = true;
            }
            
            if (found)
			{
				returnPosition = currentPosition;
				break;
			}
            else
                currentPosition = currentPosition + Math.max(goodArray[badCharPosition],badArray[(int)charArray[currentPosition + badCharPosition]] + 1 - (patternArray.length - badCharPosition));
		}
        return returnPosition;
	}
	
	// Internal method to perform the repeated string search using Boyer Moore. 
	private static void BoyerMoore(char[] charArray)
	{
        System.out.print("\nStarting search...");
		StopWatch sw = new StopWatch();
		sw.start();
		
		char[] pattern;
        char[] text;
        String maxPrefix = "";
        int tempPosition = -1;
		int firstPos = -1;
        int secondPos = -1;
        
        for(int i = 0;i < charArray.length;i++)
        {
            for(int j = maxPrefix.length() + 1;j <= charArray.length - i - 1;j++) //Starts at maxPrefix.length() + 1 so as not to repeat smaller/equivalent cases.
            {
                //pattern = String.copyValueOf(charArray,i,j);
				pattern = new char[j];
				System.arraycopy(charArray, i, pattern, 0, j);
                text = new char[charArray.length - i];
                
                for (int copy = i + 1;copy <= charArray.length - 1;copy++)
				{
                    text[copy - i - 1] = charArray[copy];
					charAccess++;
				}
				
                tempPosition = search(pattern, text);
                if (tempPosition == -1)
                    break;
                else
                {
                    if (pattern.length > maxPrefix.length())
                    {
						firstPos = i;
                        secondPos = tempPosition;
                        maxPrefix = String.valueOf(pattern);
						
                    }
                }
            }
        }
		
        sw.stop();
		System.out.println("done!");
		
		System.out.println("\n1) Algorithm 2 was used.\n2) Length of longest match is " + maxPrefix.length() + "\n3) The longest matching string is \"" + maxPrefix + "\"\n4) Start position of first occurence is " + firstPos + "\n5) Start position of second occurence is " + secondPos);
		System.out.println("6) Total number of character accesses is " + charAccess);
		System.out.println("7) Average character accesses per character in the file is " + charAccess / (long)charArray.length);
		System.out.println("\nTotal run time: " + formatSeconds(sw.read()));
		
		sw.reset();
		
	}
	/** End of the Boyer-Moore methods. */
	
	
	/** Begin the section of methods devoted to the suffix/prefix search type. 
	This includes the actual algorithm, as well as generating the position references as wrapped classes, and sorting, then finally searching.
	*/
	// Implementation of the third algorithm. This is the method that is called to perform the search. Includes generating suffixes and sorting.
	private static void algorithm3(char[] charArray)
	{
		System.out.print("\nStarting search...");
		StopWatch sw = new StopWatch();
		sw.start();
		
        String maxPrefix = "";
        int firstPos = -1;
        int secondPos = -1;
        String temp = "";
		
		// First create the IntToChar stuff
		ArrayList<IntToIndex> intArray = new ArrayList<IntToIndex>();
		for(int i=0; i < charArray.length; i++)
		{
			intArray.add(new IntToIndex(charArray, i));
		}
		
		Collections.sort(intArray);
		// END
        
        for(int i=0; i < charArray.length - 1; i++)
		{
            if(charArray.length - intArray.get(i).theindex >= maxPrefix.length())
			{
                temp = findMaxPrefix(intArray.get(i).theindex, intArray.get(i + 1).theindex, charArray);
                
                if(temp.length() > maxPrefix.length())
				{
                    maxPrefix = temp;
                    firstPos = intArray.get(i).theindex;
                    secondPos = intArray.get(i + 1).theindex;
                }
            }
        }
		sw.stop();
		System.out.println("done!");
		
		System.out.println("\n1) Algorithm 3 was used.\n2) Length of longest match is " + maxPrefix.length() + "\n3) The longest matching string is \"" + maxPrefix + "\"\n4) Start position of first occurence is " + firstPos + "\n5) Start position of second occurence is " + secondPos);
		System.out.println("6) Total number of character accesses is " + charAccess);
		System.out.println("7) Average character accesses per character in the file is " + charAccess / (long)charArray.length);
		System.out.println("\nTotal run time: " + formatSeconds(sw.read()));
		
		sw.reset();
	}
	
	// Finds the max prefix of two parts of a char array
    private static String findMaxPrefix(int index1, int index2, char[] charArray)
	{
        StringBuffer prefix = new StringBuffer("");
        
        for(int i=0; i < (charArray.length - index1) && i < (charArray.length - index2); i++)
		{
			charAccess++;
            if(charArray[index1+i] == charArray[index2+i])
                prefix.append(charArray[index1+i]);
			else
				break;
        }
        return prefix.toString();
    }
	
	// Wrapper class of the int value that represents the location in the charArray of a suffix.
	public static class IntToIndex implements Comparable<IntToIndex>
	{
		public int theindex;
		public char[] originalCharArray;
		
		public IntToIndex(char[] input, int inputB)
		{
			theindex = inputB;
			originalCharArray = input;
		}
		
		// Required for comparable to work!
		public int compareTo(IntToIndex second)
		{
			int returnValue = -2; // The value of -2 is set for the later case where 
			
			for (int i = 0; i <= Math.min(this.originalCharArray.length - this.theindex, second.originalCharArray.length - second.theindex) - 1;i++)
			{
				if (this.originalCharArray[i + this.theindex] < second.originalCharArray[i + second.theindex])
				{
					returnValue = -1;
					break;
				}
				if (this.originalCharArray[i + this.theindex] > second.originalCharArray[i + second.theindex])
				{
					returnValue = 1;
					break;
				}
			}
			
			if (returnValue == -2)
			{
				if ((this.originalCharArray.length - this.theindex) < (second.originalCharArray.length - second.theindex))
					returnValue = -1;
				else if ((this.originalCharArray.length - this.theindex) > (second.originalCharArray.length - second.theindex))
					returnValue = 1;
				else
					returnValue = 0;
			}
			
			return returnValue;
		}
	}
	
	// Unoptimized version of algorithm 3 using an array of strings as the suffix array.
	public static void algorithm3unop(char[] charArray) throws OutOfMemoryError
	{
		System.out.print("\nA heap error will occurr if the file is too large of this method!\nThis will run on 1K-3K files, NO LARGER.\nStarting search...");
		StopWatch sw = new StopWatch();
		sw.start();
		
		String maxPrefix;
		int length = charArray.length;
		String[] suffixArray = new String[length];
		
		for(int i=0; i < length; i++)
			suffixArray[i] = new String(charArray, i, length-i);
		
		Arrays.sort(suffixArray);
		
		int maxLength = 0;
		int tempInt = 0;
		int currentPrefix = 0;
		
		// Searches the entire suffix array.
		for(int z = 0; z < length-1; z++)
		{
			if(z+1 < length)
			{
				for(int x = 0; (x < (suffixArray[z].length())) && (x < (suffixArray[z+1].length())); x++)
				{
					charAccess++;
					if(suffixArray[z].charAt(x) == suffixArray[z+1].charAt(x))
					{
						tempInt++;
						if(tempInt > maxLength)
						{
							maxLength = tempInt;
							currentPrefix = z;
						}
					}
					else
						break;
				}
			}
			tempInt = 0;
		}
		sw.stop();
		System.out.println("done!");
		
		maxPrefix = suffixArray[currentPrefix].substring(0, maxLength);
		
		System.out.println("\n1) Unoptimized algorithm 3 was used.\n2) Length of longest match is " + maxPrefix.length() + "\n3) The longest matching string is \"" + maxPrefix + "\"");
		System.out.println("4) Total number of string comparisons is " + charAccess);
		System.out.println("Other information is not relevant to this method, due to strings being used!");
		System.out.println("\nTotal run time: " + formatSeconds(sw.read()));
		
		sw.reset();
	}
	/** End of the suffix search methods. */
	
	
	/**
	Below are the methods common to all search types.
	These include the watch, as well as the method to convert the a string to a char array, and reading a file.
	*/
	// Interal method to convert a given file (see fileName) to a character Array. Returns the character Array.
	private static char[] convertToCharArray(String fileName, String inputText) throws IOException,FileNotFoundException
	{
		ArrayList<Character> fileData = new ArrayList<Character>();
		
		if (fileName != null)
			fileData = readFile(fileName);
		else
		{
			for (int count = 0; count < inputText.length();count++)
				fileData.add(inputText.charAt(count));
		}
		char[] tempArray;
		char[] charArray;
		tempArray = new char[fileData.size()];
		int currentChar = 0;
		
		// Change ArrayList Data to char array
		for(int i = 0; i < fileData.size(); i++)
		{
			//If it is an uppercase alphabetical Character
			if((int)fileData.get(i) > 64 && (int)fileData.get(i) < 91)
			{
				tempArray[currentChar] = (char)((int)fileData.get(i) + 32);
				currentChar++;
			}

			// If it is a lowercase alphabetical character or a single white space
			else if((int)fileData.get(i) > 96 && (int)fileData.get(i) < 123)
			{
				tempArray[currentChar] = fileData.get(i);
				currentChar++;
			}

			// If it is whitespace
			else
			{
				if(currentChar != 0 && (int)tempArray[currentChar-1] != 32)
				{
				tempArray[currentChar] = (char)(32);
				currentChar++;
				}
			}
		}
		
		charArray = new char[currentChar];

		for(int l = 0; l<currentChar; l++)
			charArray[l] = tempArray[l];

		return charArray;
	}
	
	// Reads a file into a character ArrayList. Returns the ArrayList.
	private static ArrayList<Character> readFile(String fileName) throws IOException, FileNotFoundException
	{
		ArrayList<Character> tempAL = new ArrayList<Character>();
		File inputfilename = new File(fileName);
		FileReader reader = new FileReader(inputfilename);
		int tempint;
		
		tempint = reader.read();
		while(tempint != -1)
		{
			tempAL.add((char)tempint);
			tempint = reader.read();
		}
		
		reader.close();
		
		return tempAL;
	}
	
	private static class StopWatch
	{
		protected boolean running;  // is the clock on?
		protected long strt;        // starting millisecond count
		protected long accum;       // total milliseconds

		public StopWatch()
		// post: returns a stopped clock
		{
			running = false;
			strt = 0;
			accum = 0;
		}

		public void start()
		// pre: clock is stopped
		// post: starts clock, begins measuring possibly accumulated time
		{
			running = true;
			strt = System.currentTimeMillis();
		}

		public void stop()
		// pre: clock is running
		// post: stops clock, and accumulates time
		{
			running = false;
			accum += (System.currentTimeMillis() - strt);
		}

		public double read()
		// pre: clock is stopped.
		// post: returns the accumulated time on the clock in seconds
		{
			return (double)accum/(double)1000.0;
		}

		public void reset()
		// post: stops running clock and clears the accumulated time.
		{
			running = false;
			accum = 0;
		}

		public String toString()
		// post: returns a string representation of the clock
		{
			return "<Clock: "+ read() +" seconds>";
		}
	}
	
	private static String formatSeconds(double input)
	{
		String output = "";
		
		int hours = (int)input/3600;
		// 
		int tempminutes = (int)input%3600;
		// 
		int minutes = (int)tempminutes/60;
		// 
		int seconds = (int)tempminutes%60;
		// 
		
		if (hours > 0)
			output += hours + " hours, ";
		if (minutes >= 0)
			output += minutes + " minutes, ";
		if (seconds >= 0)
			output += seconds + " seconds";
		
		return output;
	}
	
	/** End of the shared methods. */
}

/** Last edited on Saturday, April 2nd 2011. */