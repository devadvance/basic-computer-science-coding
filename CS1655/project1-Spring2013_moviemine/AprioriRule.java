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

import java.util.HashSet;

public class AprioriRule {
	private HashSet<Integer> leftSide;
	private HashSet<Integer> rightSide;
	private int support;
	private double supportDouble;
	private double confidence;
	
	public AprioriRule (HashSet<Integer> left, HashSet<Integer> right) {
		leftSide = left;
		rightSide = right;
	}
	
	public void setSupport (int sup) {
		support = sup;
	}
	
	public void setConfidence (double conf) {
		confidence = conf;
	}

	public void setSupportDouble(double support) {
		supportDouble = support;
	}

	public double getSupportDouble() {
		return supportDouble;
	}
	
	public int getSupport() {
		return support;
	}
	
	public double getConfidence () {
		return confidence;
	}

	public HashSet<Integer> getLeftSide() {
		return leftSide;
	}
	
	public HashSet<Integer> getRightSide() {
		return rightSide;
	}

	public void setLeftSide(HashSet<Integer> left) {
		leftSide = left;
	}

	public void setRightSide(HashSet<Integer> right) {
		rightSide = right;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(leftSide.toString());
		builder.append(" => ");
		builder.append(rightSide.toString());
		builder.append(" with support: " + support + " (" + String.format("%.2f", 100.0 * supportDouble) + "%)" 
		+ ", confidence: " + String.format("%.2f", 100.0 * confidence) + "%\n");
		
		return builder.toString();
	}
}
