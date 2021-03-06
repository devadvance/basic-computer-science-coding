/**
Copyright (c) 2009, Matt Joseph
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
CS7
Assignment 2
*/

/** *******************************
hw2d - Matt Joseph
This program asks the user to select their internet
package and input hours of use, and then computes
the cost. In addition, it presents the savings (if any)
offered by the other packages. This version uses dialog
boxes only.
******************************** */

import javax.swing.*;
import java.text.DecimalFormat;

public class hw2d
{
	public static void main (String[] args)
	{
		Object[] possibles = {"A", "B", "C"};
		Object userpick = JOptionPane.showInputDialog(null, "Please pick your internet package:", "Input",
			JOptionPane.INFORMATION_MESSAGE, null,possibles, possibles[0]);

		double hours = Double.parseDouble(JOptionPane.showInputDialog("Please enter the number of hours of internet use: ", 0));

		double costA;
		double costB;
		double costC = 19.95;
		DecimalFormat formatter = new DecimalFormat(".00");

		if (hours > 10)
			costA = ((hours - 10) * 2.00) + 9.95;
		else costA = 9.95;

		if (hours > 20)
			costB = ((hours - 10) * 1.00) + 13.95;
		else costB = 13.95;

		//If package was A
		if ((userpick == "A")) {
			JOptionPane.showMessageDialog(null, "You picked package A and used " + hours + " hours this month.\nThe cost of this usage is $" + formatter.format(costA));
			if (costA <= costB) {
				JOptionPane.showMessageDialog(null, "Sorry, you would not save any money with packages B or C.");
			}
			else if (costA <= costC) {
				JOptionPane.showMessageDialog(null, "You would save $" + formatter.format(costA - costB) + " with package B, but would not save anything with package C.");
			}
			else
				JOptionPane.showMessageDialog(null, "You would save $" + formatter.format(costA - costB) + " with package B, and would save $" + formatter.format(costA - costC) + " with package C.");
		}

		//If package was B
		if ((userpick == "B")) {
			JOptionPane.showMessageDialog(null, "You picked package B and used " + hours + " hours this month.\nThe cost of this usage is $" + formatter.format(costB));
			if (costB <= costC) {
				JOptionPane.showMessageDialog(null, "Sorry, you would not save any money with package C.");
			}
			else
				JOptionPane.showMessageDialog(null, "You would save $" + formatter.format(costB - costC) + " with package C.");
		}

		//If package was C
		if ((userpick == "C")) {
			JOptionPane.showMessageDialog(null, "You picked package C and used " + hours + " hours this month.\nThe cost of this usage is $" + formatter.format(costC));	
		}
	}
}

