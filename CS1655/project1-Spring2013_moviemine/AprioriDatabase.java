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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.HashSet;

public class AprioriDatabase {
	
	private Map<Integer, HashSet<Integer>> positiveTable = new HashMap<Integer, HashSet<Integer>>();
	private Map<Integer, HashSet<Integer>> negativeTable = new HashMap<Integer, HashSet<Integer>>();
	private Map<Integer, HashSet<Integer>> combinationTable = new HashMap<Integer, HashSet<Integer>>();
	private ArrayList<Map<Integer, HashSet<Integer>>> tableArray = new ArrayList<Map<Integer, HashSet<Integer>>>();
	
	// The user chooses which table to use
	private int tableChoice;
	private double minConf;
	private double minSup;
	private int minSupInt;
	private int maxItems;
	
	// Holds the count for the candidate itemsets
	Map<HashSet<Integer>, Integer> frequentItemsetsCount = new HashMap<HashSet<Integer>, Integer>();
	// Holds the single counts until the end
	Map<HashSet<Integer>, Integer> frequentSinglesCount = new HashMap<HashSet<Integer>, Integer>();
	// Hold the count for the candidate itemsets before they are checked against minSup
	Map<HashSet<Integer>, Integer> newFrequentItemsetsCount = new HashMap<HashSet<Integer>, Integer>();
	// Hold the candidate sets
	private HashSet<HashSet<Integer>> candidateSet = new HashSet<HashSet<Integer>>();
	// Temporarily hold candidate sets
	private HashSet<HashSet<Integer>> tempCandidateSet = new HashSet<HashSet<Integer>>();
	// All rules list
	ArrayList<AprioriRule> allRules = new ArrayList<AprioriRule>();
	// Finished rules
	ArrayList<AprioriRule> finishedRules = new ArrayList<AprioriRule>();
	
	// Database constructor 
	public AprioriDatabase (String filename, double sup, double conf, int max, int choice) throws FileNotFoundException {
		tableChoice = choice;
		minSup = sup;
		minConf = conf;
		maxItems = max;
		File datafile = new File(filename);
		Scanner scanner = new Scanner(datafile);
		
		int tempUser;
		int tempTitle;
		int tempRating;
		
		while (scanner.hasNextLine()) {
			
			// Grab the integer values
            try {
            	tempUser = scanner.nextInt();
            }
            catch (Exception e) {
            	System.out.println("Blank Last Line");
            	break;
            }
            
            tempTitle = scanner.nextInt();
            tempRating = scanner.nextInt();
            // Skip to the next line
            // Double check this later
            scanner.nextLine();
            
            // Develop the combinationTable at the same time.
            // To do this, just have the negativeTable values stored as negative values. Easiest way! 
            if (tempRating > 3) {
            	// If the List doesn't exist yet, create it first
            	if (!positiveTable.containsKey(tempUser)) {
            		positiveTable.put(tempUser, new HashSet<Integer>());
            	}
            	if (!combinationTable.containsKey(tempUser)) {
            		combinationTable.put(tempUser, new HashSet<Integer>());
            	}
            	// Then add the new movie title
            	positiveTable.get(tempUser).add(tempTitle);
            	combinationTable.get(tempUser).add(tempTitle);
            }
            else if (tempRating < 3) {
            	// If the List doesn't exist yet, create it first
            	if (!negativeTable.containsKey(tempUser)) {
            		negativeTable.put(tempUser, new HashSet<Integer>());
            		
            	}
            	if (!combinationTable.containsKey(tempUser)) {
            		combinationTable.put(tempUser, new HashSet<Integer>());
            	}
            	// Then add the new movie title
            	negativeTable.get(tempUser).add(0 - tempTitle);
            	combinationTable.get(tempUser).add(0 - tempTitle);
            }
        }
		
		// Add the three tables to the table ArrayList
		tableArray.add(positiveTable);
		tableArray.add(negativeTable);
		tableArray.add(combinationTable);
		
		// Close the scanner at the end
		scanner.close();
	}
	
	/**
	 * Run the database.
	 */
	public void run() {
		doFrequency();
		doRules();
	}
	
	/**
	 * Print out the contents of a table. For debug purposes.
	 * @param whichTable Which of the three tables. 0 - positive, 1 - negative, 2 - combination.
	 * @return String of the table.
	 */
	public String printTable(int whichTable) {
		StringBuilder builder = new StringBuilder();
		
		Iterator<Entry<Integer, HashSet<Integer>>> iter = tableArray.get(whichTable).entrySet().iterator();

		while(iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
			builder.append((Integer)entry.getKey() + ": " + entry.getValue().toString() + "\n");
		}
		
		return builder.toString();
	}
	
	/**
	 * Do all of the frequency/minSup related steps.
	 */
	public void doFrequency() {
		
		if (minSup < 1) {
			minSupInt = (int) (minSup * tableArray.get(tableChoice).size());
		}
		else {
			minSupInt = (int) minSup;
		}
		
		boolean noMoreCandidates = false;
		
		generateFirstCandidates(minSupInt);
				
		int i = 2;
		while ((!noMoreCandidates) && (i <= maxItems)) {
			generateCandidates(i);
			noMoreCandidates = generateFrequentSets(minSupInt);
			i++;
		}
		
		frequentItemsetsCount.putAll(frequentSinglesCount);
	}
	
	/**
	 * Do all of the rule related steps.
	 */
	public void doRules() {
		generateAllRules();
		finishRules();
		System.out.println("Finished rules:\n" + finishedRules.toString() + "Size: " + finishedRules.size());
	}
	
	/**
	 * Calculate support and confidence for each rule. Also checks to make sure the rule meets minConf
	 */
	public void finishRules() {
		HashSet<Integer> leftSide = new HashSet<Integer>();
		HashSet<Integer> rightSide = new HashSet<Integer>();
		HashSet<Integer> total = new HashSet<Integer>();
		int tempSupport;
		double tempConfidence;
		
		// Remove those sets that have a count less than the minSup
		for (AprioriRule tempRule : allRules) {
			leftSide = tempRule.getLeftSide();
			rightSide = tempRule.getRightSide();
			// This is to get the support of the union. Theoretically this could be done when generating the rules.
			// TODO: Do this earlier..maybe.
			total = new HashSet<Integer>(leftSide);
			total.addAll(rightSide);
			
			tempSupport = frequentItemsetsCount.get(total);
			
			tempConfidence = (double)tempSupport / (double)frequentItemsetsCount.get(leftSide);
			
			if (tempConfidence >= minConf) {
				tempRule.setConfidence(tempConfidence);
				tempRule.setSupport(tempSupport);
				tempRule.setSupportDouble((double)tempSupport / tableArray.get(tableChoice).size());
				if (tempRule.getSupportDouble() >= minSup) {
					// To make it match the TA's output.
					// I rounded earlier than he did so the patch is necessary.
					finishedRules.add(tempRule); 
				}
			}
		}
	}
	
	/**
	 * Generate the first candidate sets. Just makes a list of all single item itemsets. Counting is included in this step to remove redundancy.
	 * @param minSup The minimum support.
	 */
	public void generateFirstCandidates (final int minSup) {
		System.out.println("Generating first candidate itemsets with minSup of: " + minSup);
		HashSet<Integer> tempItemset;
		
		for (HashSet<Integer> tempSet : tableArray.get(tableChoice).values()) {
			for (Integer tempInt : tempSet) {
				// Create an itemset
				tempItemset = new HashSet<Integer>();
				// Add the title to the itemset
				// Note: using HashSets with only 1 integer. This is to reduce redundant code later on.
				tempItemset.add(tempInt);
				
				// If this title is not yet in the count map
				if (!newFrequentItemsetsCount.containsKey(tempItemset)) {
					newFrequentItemsetsCount.put(tempItemset, 1);
				}
				else {
					// If it is, just increment
					// Theoretically, this invalidates the need for the if/else, but this makes it explicit
					newFrequentItemsetsCount.put(tempItemset, newFrequentItemsetsCount.get(tempItemset) + 1);
				}
			}
		}
		
		// Call the removeBelowSup method to remove those itemset with counts below the minSup
		removeBelowSup(minSup);
	}
	
	/**
	 * Generate candidate sets. Works for any with size 2 or greater. Not used for the first round.
	 * @param setSize Desired itemset size.
	 */
	public void generateCandidates(final int setSize) {
		System.out.println("Generating candidate itemsets with set size of: " + setSize);
		
		// Clear the temp set
		tempCandidateSet.clear();
		// Clear the candidate set
		candidateSet.clear();
		
		// This will be the outer loop set, which will have other sets unioned with it
		HashSet<Integer> firstSet;
		// Merger of two previous sets
		HashSet<Integer> newSet;
		
		// Grab a list of current candidate itemsets, uses the candidateCountTable so as not to repeat counting
		// Using newFrequentItemsetsCount so as to only use the previous candidate generation
		ArrayList<HashSet<Integer>> previousCandidateSets = new ArrayList<HashSet<Integer>>(newFrequentItemsetsCount.keySet());
		
		if (setSize < 1) {
			// This method should only be used AFTER the first count. It is inefficient otherwise!
		}
		else {
			// Going to be a combination of sets
			// While the list of current candidate itemsets is not empty
			while (!previousCandidateSets.isEmpty()) {
				// Grab the first set from the list
				firstSet = previousCandidateSets.get(0);
				// Remove that set from the list
				previousCandidateSets.remove(0);
				// Now combine that set with all other sets in the list and add the resulting combination to the tempCandidateSet
				for (HashSet<Integer> secondSet : previousCandidateSets) {
					// Create a new set using the first
					newSet = new HashSet<Integer>(firstSet);
					// Union (essentially) the second set with the new set
					newSet.addAll(secondSet);
					// Add the new set to the tempCandidateSet
					if (newSet.size() == setSize) {
						tempCandidateSet.add(newSet);
					}
				}
			}
		}
		
		// After everything is generated, set the candidateSet to the tempCandidateSet
		// Duplicate the set
		candidateSet = new HashSet<HashSet<Integer>>(tempCandidateSet);
		
		// Clear the frequentItemsetsCount map. This is because we don't care about single sets for association rules!
		// Only do this after generating the two-item candidate itemsets
		// Duplicate the maps though, as they will be needed later when calculating confidence
		if (setSize == 2) {
			frequentSinglesCount.putAll(frequentItemsetsCount);
			frequentItemsetsCount.clear();
		}
	}
	
	/**
	 * Generate the frequentsets from the candidate sets.
	 * @param minSup The minumum support. Not really needed.
	 * @return True means there are no more candidate itemsets.
	 */
	public boolean generateFrequentSets(final int minSup) {
		// Clear the temporary map
		newFrequentItemsetsCount.clear();
		
		// True means there are no more candidate itemsets
		boolean returnValue = true;
		ArrayList<HashSet<Integer>> tempTransactions = new ArrayList<HashSet<Integer>>(tableArray.get(tableChoice).values());
		
		// For each member of the positiveTable (list of transactions)
		for (HashSet<Integer> tempList : tempTransactions) {
			// For each candidate itemset
			for (HashSet<Integer> tempItemset : candidateSet) {
				// If the transaction contains the itemset, then the count will be incremented
				if (tempList.containsAll(tempItemset)) {
					// We found a candidate in the transactions!
					returnValue = false;
					// If this title is not yet in the count map
					if (!newFrequentItemsetsCount.containsKey(tempItemset)) {
						newFrequentItemsetsCount.put(tempItemset, 1);
					}
					else {
						// If it is, just increment
						// Theoretically, this invalidates the need for the if/else, but this makes it explicit
						newFrequentItemsetsCount.put(tempItemset, newFrequentItemsetsCount.get(tempItemset) + 1);
					}
				}
			}
		}
		
		if (returnValue) {
			return returnValue;
		}
		else {
			// Call the removeBelowSup method to remove those itemset with counts below the minSup
			return removeBelowSup(minSup);
		}
	}
	
	/**
	 * Remove itemsets that do not meet the minSup.
	 * @param minSup The minimum support.
	 * @return True if there were no more itemsets that met the minSup, false if there were any.
	 */
	private boolean removeBelowSup (final int minSup) {
		// True means there are no more frequent itemsets that meet the required minSup
		boolean returnValue = true;
		
		// Get an iterator to the frequentItemsetsCount HashMap
		Iterator<Entry<HashSet<Integer>, Integer>> frequentIter = newFrequentItemsetsCount.entrySet().iterator();
		Map.Entry entry;
		
		// Remove those sets that have a count less than the minSup
		while(frequentIter.hasNext()) {
			entry = (Map.Entry)frequentIter.next();
			if ((Integer)entry.getValue() < minSup) {
				frequentIter.remove();
			}
			else {
				returnValue = false;
			}
		}
		
		// Put all of the new counts into the existing map
		frequentItemsetsCount.putAll(newFrequentItemsetsCount);
		
		return returnValue;
	}
	
	/**
	 * Generate rules for all of the frequent itemsets.
	 */
	private void generateAllRules() {
		// Get an iterator to the frequentItemsetsCount HashMap
		Iterator<Entry<HashSet<Integer>, Integer>> frequentIter = frequentItemsetsCount.entrySet().iterator();
		Map.Entry entry;
		ArrayList<AprioriRule> tempList;
		
		while(frequentIter.hasNext()) {
			entry = (Map.Entry)frequentIter.next();
			// Generate rules for a specific itemset
			tempList = generateRule((HashSet<Integer>)entry.getKey());
			// Add this list to the total list
			allRules.addAll(tempList);
		}
	}
	
	/**
	 * Generates a list of rules for a given set.
	 * @param set The itemset to generate the rules for.
	 * @return The list of rules.
	 */
	private ArrayList<AprioriRule> generateRule(HashSet<Integer> set) {
		int leftSize;
		ArrayList<HashSet<Integer>> subsetList = new ArrayList<HashSet<Integer>>();
		ArrayList<Integer> tempList = new ArrayList<Integer>(set);
		ArrayList<AprioriRule> returnList = new ArrayList<AprioriRule>();

		HashSet<Integer> leftSideSingle = new HashSet<Integer>();
		HashSet<Integer> rightSide = new HashSet<Integer>();
		
		if (set.size() < 2) {
			// Well there aren't any rules if the itemsets only have one item...don't generate any.
		}
		else if (set.size() == 2) {
			// Generate two pairs
			leftSideSingle.add(tempList.get(0));
			rightSide.add(tempList.get(1));
			returnList.add(new AprioriRule(leftSideSingle, rightSide));
			returnList.add(new AprioriRule(rightSide, leftSideSingle));
		}
		else {
			for (leftSize = 1;leftSize < set.size(); leftSize++) {
				// Generate the subsets for the left side
				subsetList = generateSubset(tempList, leftSize);
				// Loop through the resulting left side subsets and determine the right side
				for (HashSet<Integer> leftSide : subsetList) {
					// Create the right side from all of the elements
					rightSide = new HashSet<Integer>(tempList);
					// Remove all those that occur in the left side
					rightSide.removeAll(leftSide);
					// Add a rule of the left side and right side
					returnList.add(new AprioriRule(leftSide, rightSide));
				}
			}
		}
		
		return returnList;
	}
	
	public ArrayList<HashSet<Integer>> generateSubset(List<Integer> originalSet, int comboSize) {
		ArrayList<HashSet<Integer>> resultList = new ArrayList<HashSet<Integer>>();
		getSubset(originalSet, comboSize, 0, new HashSet<Integer>(), resultList);
		return resultList;
	}
	
	private static void getSubset(List<Integer> originalSet, int comboSize, int pivot, HashSet<Integer> current,List<HashSet<Integer>> resultSet) {
		// If this is the base case
		if (current.size() == comboSize) {
			resultSet.add(new HashSet<Integer>(current));
			return;
		}
		
		// If the pivot has somehow reached the size of the originalSet (not good..)
		if (pivot == originalSet.size()) {
			return;
		}
		
		int x = originalSet.get(pivot);
		current.add(x);
		
		// Recurse another level
		getSubset(originalSet, comboSize, pivot + 1, current, resultSet);
		// Remove the just added value from the current set
		current.remove(x);
		// Recurse another level without the just removed value
		getSubset(originalSet, comboSize, pivot + 1, current, resultSet);
	}
}