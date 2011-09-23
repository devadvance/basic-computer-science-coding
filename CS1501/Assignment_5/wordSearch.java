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
Assignment 5
*/

import java.util.*;
import java.io.*;
import java.lang.*;

public class wordSearch
{
	/**
	* Varible declaration.
	*/
	private static int N; // Size of the board.
	private static int dictionaryCounter; // Dictionary counter - number of words in the dictionary.
	private static TST gameTrie = new TST(); // Trie containing all the words on the board.
	private static TST dictionary; // Trie containing all the words in the dictionary.
	private static TST foundWords; // Trie containing the words that the user has found. Does not add duplicates.
	private static ArrayList<Character> boardAL; // Board in ArrayList form. Used as an step between disk and 2D array.
	private static char[][] boardArray; // Board in 2D Array form.
	
	/**
	* Main method. Runs the program.
	*/
	public static void main(String[] args)
	{
		String play, dictionaryFileName,boardFileName,word;
		Scanner input = new Scanner(System.in);
		do
		{
			play = "";
			dictionaryFileName = "";
			boardFileName = "";
			word = "";
			String tempCont;
			StopWatch s = new StopWatch();
			
			System.out.println("\nWelcome to WORD SEARCH!  Run with \"-help\" for cmd-line options.");
			if (args.length > 0)
			{
				if(args[0].compareTo("-help") == 0)
				{
					System.out.println("Options:");
					System.out.println("\"-board FILENAME\": Specifies game board file.");
					System.out.println("\"-dict FILENAME\": Specifies dictionary file.");
					System.out.print("Enter the filename for the dictionary: ");
				}
				
				for(int i = 0; i<args.length; i++)
				{
					if (args[i].compareTo("-dict") == 0)
					{
						dictionaryFileName = args[i+1];
					}
					
					else if (args[i].compareTo("-board") == 0)
					{
						boardFileName = args[i+1];
					}
				}
			}
			if(dictionaryFileName.compareTo("") == 0){
				System.out.print("Enter the filename for the dictionary: ");
				dictionaryFileName = input.next();
			}
			
			if(boardFileName.compareTo("") == 0){
				System.out.print("Enter the filename for the game board: ");
				boardFileName = input.next();
			}
			
			try
			{
				dictionary = createDictionary(dictionaryFileName);
				boardAL = readBoard(boardFileName);
				
				boardArray = convertTo2DArray(boardAL);
				
				exhaustiveSearch();
				ArrayList<String> anArrayList = gameTrie.TST_AL; // Gets the ArrayList containing all the words in the trie.
				
				printBoard();
				
				Collections.sort(anArrayList);
				
			}
			catch(FileNotFoundException e)
			{
				System.out.println("\n\nYou entered an invalid file name!");
			}
			catch (IOException e)
			{
				System.out.println("\n\nAn IOException occured...uh oh!");
			}
			catch (Exception e)
			{
				System.out.println("\n\nSome unknown error occurred. Please contact support :)");
			}
			
			System.out.println("You can now type in words.  Type a non-alphabetic symbol and ENTER to quit playing and list all the words on the game board.");
			
			foundWords = new TST();
			do
			{
				System.out.print("Enter a word in the crossword: ");
				word = input.next();
				System.out.print("You entered: " + word.toUpperCase());
				if ((((int)word.charAt(0) >= 65 && (int)word.charAt(0) <= 90) || ((int)word.charAt(0) >= 97 && (int)word.charAt(0) <= 122)))
					tempCont = gameTrie.contains(word.toUpperCase());
				else
					break;
				if (!(tempCont == null) && !(tempCont.compareTo("") == 0) && tempCont.charAt(tempCont.length() - 1) == '!')
				{
					System.out.println("...you found a word on the board!\n");
					foundWords.insert(word.toUpperCase());
				}
				else
					System.out.println("...you did NOT find a word on the board :(\n");
					
			}while(((int)word.charAt(0) >= 65 && (int)word.charAt(0) <= 90) || ((int)word.charAt(0) >= 97 && (int)word.charAt(0) <= 122));
			
			printResults();
			
			System.out.print("Play again? [Y/N] ");
			play = input.next();
			
			// In case the user wants to play again.
			dictionaryFileName = "";
			boardFileName = "";
		}while(play.charAt(0) == 'y' || play.charAt(0) == 'Y');
		
	}
	
	/**
	* Creates the dictionary trie from a given file name.
	* @return TST containing the dictionary.
	*/
	public static TST createDictionary(String fileName) throws FileNotFoundException,IOException
	{
		StopWatch d = new StopWatch();
		d.start();
		
		TST outputTST = new TST();
		ArrayList<String> tempAL = new ArrayList<String>();
		String temporaryWord;
		File dict = new File(fileName);
		Scanner input = new Scanner(dict);
		
		while (input.hasNext())
		{
			temporaryWord = input.next();
			tempAL.add(temporaryWord);
			dictionaryCounter++;
		}
		
		d.stop();
		input.close();
		System.out.printf("Reading dictionary (%d words) from disk: %.3f ms.\n", dictionaryCounter, d.readMS());
		
		d.reset();
		d.start();
		for (int x = 0;x < tempAL.size();x++)
			outputTST.insert(tempAL.get(x).toUpperCase());
		d.stop();
		
		System.out.printf("Putting dictionary into ternary-search-trie: %.3f ms.\n", d.readMS());
		
		return outputTST;
	}
	
	/**
	* Prints the game board out to the screen from the 2D Array.
	*/
	public static void printBoard()
	{
		StringBuilder output = new StringBuilder("\n\nGame Board:\n");
		
		for (int i = 0;i<N;i++)
		{
			for(int j = 0;j<N;j++)
			{
				output.append(boardArray[i][j] + " ");
			}
			output.append("\n");
		}
		System.out.println(output.toString());
	}
	
	/**
	* Prints the result of the game after the user has finished playing.
	* Uses the TST_ALs from the tries to print out the words. Much faster than searching the TST and printing it.
	*/
	public static void printResults()
	{
		StringBuilder output = new StringBuilder("\n");
		
		output.append("\nList of all words on the game board:\n" + gameTrie.TST_AL + "\n\n");
		output.append("List of words that you found:\n" + foundWords.TST_AL + "\n\n");
		output.append("You found " + foundWords.TST_AL.size() + " out of " + gameTrie.TST_AL.size() + " words.");
		output.append(" You found " + (int)(100 * ((double)foundWords.TST_AL.size() / (double)gameTrie.TST_AL.size())) + "% of the words!\n");
		
		System.out.println(output.toString());
		
	}
	
	/**
	* Performs the search on the game board for all of the words.
	* Calls the other 8 methods for the different directions for each character on the board.
	*/
	public static void exhaustiveSearch()
	{
		StopWatch sw = new StopWatch();
		sw.start();

		for(int row = 0; row < N; row++)
		{
			for(int col = 0; col < N; col++)
			{
				// Check each direction. The method are called in a clockwise order starting from the right.
				checkRight(row, col, new StringBuilder());
				checkDownRight(row, col, new StringBuilder());
				checkDown(row, col, new StringBuilder());
				checkDownLeft(row, col, new StringBuilder());
				checkLeft(row, col, new StringBuilder());
				checkUpLeft(row, col, new StringBuilder());
				checkUp(row, col, new StringBuilder());
				checkUpRight(row, col, new StringBuilder());
			}
		}
		sw.stop();
		System.out.printf("Time to find all words in gameboard: %.3f milliseconds.\n", sw.readMS());
	}
	
	/**
	* Checks for words to the right.
	*/
	public static void checkRight(int row, int col, StringBuilder sb)
	{
		String tempString;
		String currentWord;
		for(; col < N ; col++)
		{
			sb.append(boardArray[row][col]);
			if(sb.length() >= 4 && sb.length() <= N)
			{
				currentWord = sb.toString();
				ArrayList<String> matches = dictionary.match(currentWord);
				
				if(matches.size() > 0)
				{
					for (int x = 0; x <= matches.size() - 1;x++)
					{
						tempString = matches.get(x);
						if (!(tempString == null) && !(tempString.compareTo("") == 0) && tempString.charAt(tempString.length() - 1) == '!')
							gameTrie.insert(tempString.substring(0, tempString.length() - 1));
					}
				}
			}
		}
	}
	
	/**
	* Checks for words to the left.
	*/
	public static void checkLeft(int row, int col, StringBuilder sb)
	{
		String tempString;
		String currentWord;
		for(; col >= 0 ; col--)
		{
			sb.append(boardArray[row][col]);
			if(sb.length() >= 4 && sb.length() <= N)
			{
				currentWord = sb.toString();
				ArrayList<String> matches = dictionary.match(currentWord);
				
				if(matches.size() > 0)
				{
					for (int x = 0; x <= matches.size() - 1;x++)
					{
						tempString = matches.get(x);
						if (!(tempString == null) && !(tempString.compareTo("") == 0) && tempString.charAt(tempString.length() - 1) == '!')
							gameTrie.insert(tempString.substring(0, tempString.length() - 1));
					}
				}
			}
		}
	}
	
	/**
	* Checks for words downwards.
	*/
	public static void checkDown(int row, int col, StringBuilder sb)
	{
		String tempString;
		String currentWord;
		for(; row < N ; row++)
		{
			sb.append(boardArray[row][col]);
			if(sb.length() >= 4 && sb.length() <= N)
			{
				currentWord = sb.toString();
				ArrayList<String> matches = dictionary.match(currentWord);
				
				if(matches.size() > 0)
				{
					for (int x = 0; x <= matches.size() - 1;x++)
					{
						tempString = matches.get(x);
						if (!(tempString == null) && !(tempString.compareTo("") == 0) && tempString.charAt(tempString.length() - 1) == '!')
							gameTrie.insert(tempString.substring(0, tempString.length() - 1));
					}
				}
			}
		}
	}
	
	/**
	* Checks for words upwards.
	*/
	public static void checkUp(int row, int col, StringBuilder sb)
	{
		String tempString;
		String currentWord;
		for(; row >= 0 ; row--)
		{
			sb.append(boardArray[row][col]);
			if(sb.length() >= 4 && sb.length() <= N)
			{
				currentWord = sb.toString();
				ArrayList<String> matches = dictionary.match(currentWord);
				if(matches.size() > 0)
				{
					for (int x = 0; x <= matches.size() - 1;x++)
					{
						tempString = matches.get(x);
						if (!(tempString == null) && !(tempString.compareTo("") == 0) && tempString.charAt(tempString.length() - 1) == '!')
							gameTrie.insert(tempString.substring(0, tempString.length() - 1));
					}
				}
			}
		}
	}
	
	/**
	* Checks for words to the diagonal down right.
	*/
	public static void checkDownRight(int row, int col, StringBuilder sb)
	{
		String tempString;
		String currentWord;
		while((col < N) && row < N)
		{
			sb.append(boardArray[row][col]);
			if(sb.length() >= 4 && sb.length() <= N)
			{
				currentWord = sb.toString();
				ArrayList<String> matches = dictionary.match(currentWord);
				
				if(matches.size() > 0)
				{
					for (int x = 0; x <= matches.size() - 1;x++)
					{
						tempString = matches.get(x);
						if (!(tempString == null) && !(tempString.compareTo("") == 0) && tempString.charAt(tempString.length() - 1) == '!')
							gameTrie.insert(tempString.substring(0, tempString.length() - 1));
					}
				}
			}
			col++;
			row++;
		}
	}
	
	/**
	* Checks for words to the diagonal up right.
	*/
	public static void checkUpRight(int row, int col, StringBuilder sb)
	{
		String tempString;
		String currentWord;
		while((col < N) && row >= 0)
		{
			sb.append(boardArray[row][col]);
			if(sb.length() >= 4 && sb.length() <= N)
			{
				currentWord = sb.toString();
				ArrayList<String> matches = dictionary.match(currentWord);
				
				if(matches.size() > 0)
				{
					for (int x = 0; x <= matches.size() - 1;x++)
					{
						tempString = matches.get(x);
						if (!(tempString == null) && !(tempString.compareTo("") == 0) && tempString.charAt(tempString.length() - 1) == '!')
							gameTrie.insert(tempString.substring(0, tempString.length() - 1));
					}
				}
			}
			col++;
			row--;
		}
	}
	
	/**
	* Checks for words to the diagonal down left.
	*/
	public static void checkDownLeft(int row, int col, StringBuilder sb)
	{
		String tempString;
		String currentWord;
		while((col >= 0) && row < N)
		{
			sb.append(boardArray[row][col]);
			if(sb.length() >= 4 && sb.length() <= N)
			{
				currentWord = sb.toString();
				ArrayList<String> matches = dictionary.match(currentWord);
				
				if(matches.size() > 0)
				{
					for (int x = 0; x <= matches.size() - 1;x++)
					{
						tempString = matches.get(x);
						if (!(tempString == null) && !(tempString.compareTo("") == 0) && tempString.charAt(tempString.length() - 1) == '!')
							gameTrie.insert(tempString.substring(0, tempString.length() - 1));
					}
				}
			}
			col--;
			row++;
		}
	}
	
	/**
	* Checks for words to the diagonal up left.
	*/
	public static void checkUpLeft(int row, int col, StringBuilder sb)
	{
		String tempString;
		String currentWord;
		while((col >= 0) && row >= 0)
		{
			sb.append(boardArray[row][col]);
			if(sb.length() >= 4 && sb.length() <= N)
			{
				currentWord = sb.toString();
				ArrayList<String> matches = dictionary.match(currentWord);
				
				if(matches.size() > 0)
				{
					for (int x = 0; x <= matches.size() - 1;x++)
					{
						tempString = matches.get(x);
						if (!(tempString == null) && !(tempString.compareTo("") == 0) && tempString.charAt(tempString.length() - 1) == '!')
							gameTrie.insert(tempString.substring(0, tempString.length() - 1));
					}
				}
			}
			col--;
			row--;
		}
	}
	
	/**
	* Converts the board in ArrayList from into a 2D Array.
	* @return char 2D Array containing the board.
	*/
	public static char[][] convertTo2DArray(ArrayList<Character> theCharAL)
	{
		char[][] returnArray = new char[N][N];
		int position = 0;
		
		for (int x = 0; x < N;x++)
		{
			for (int y = 0; y < N;y++)
			{
				returnArray[x][y] = theCharAL.get(position);
				position++;
			}
			
		}
		
		return returnArray;
	}
	
	/**
	* Reads file data into an Arraylist.
	* @return Character ArrayList containing the board.
	*/
	public static ArrayList<Character> readBoard(String fileName) throws FileNotFoundException,IOException
	{
		FileInputStream FIn;
		char z;
		ArrayList<Character> data = new ArrayList<Character>();
		int counter = 0;
		try
		{
			FIn = new FileInputStream(fileName);
			StringBuffer temporary = new StringBuffer("");
			try
			{
				while(FIn.available()>0)
				{
					z = (char)FIn.read();
					if ((int)z > 47 && (int)z < 58)
						temporary.append(z);
					if((int)z > 64 && (int)z < 91) // Upper case letter.
					{
						counter++;
						data.add(z);
					}
					else if((int)z > 96 && (int)z < 123) // Lower case letter.
					{
						counter++;
						data.add((char)((int)z - 32));
					}
					else if((int)z == 42) // Wild card.
					{
						counter++;
						data.add(z);
					}
				}
				
				N = Integer.parseInt(temporary.toString());
			}
			catch (IOException i){
			i.printStackTrace();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	* StopWatch private class for use with the wordSearch program.
	*/
	private static class StopWatch
	{
		protected boolean running;  // is the clock on?
		protected long strt;		// starting millisecond count
		protected long accum;	   // total milliseconds

		public StopWatch()
		{
			running = false;
			strt = 0;
			accum = 0;
		}

		public void start()
		{
			running = true;
			strt = System.currentTimeMillis();
		}

		public void stop()
		{
			running = false;
			accum += (System.currentTimeMillis() - strt);
		}

		public double read()
		{
			return (double)accum/(double)1000.0;
		}

		public double readMS()
		{
			return (double)accum;
		}
		
		public void reset()
		{
			running = false;
			accum = 0;
		}

		public String toString()
		{
			return "<Clock: "+ read() +" seconds>";
		}
	}
}