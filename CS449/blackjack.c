/*
Copyright (c) 2011, encryptstream (mgj7@pitt.edu)
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
CS449
Project 1a
Modified: 2011-09-24 @ 19:30

Note: Tested successfully by compiling with the following argument:
gcc -O2 -o blackjack blackjack.c

Note about the logic behind the aces:
You can only have ONE ace worth 11. This is true regardless of the hand. Therefore, only the FIRST ace in either the player's or dealer's hand will be given a value of 11 to start.
For example, if you have an ace (11) and (5) and then get another ace, the value is automatically one (1). If you continue to hit, and go over 21, the first ace's value will be reduced from 11 to 1.

*/

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

// Function prototypes
int value_of_card(int input_card, int ace_position);

// This function serves to correct the value to the ACTUAL value. Ex: 12 is actually a queen, which has a value of 10. 14 is an ace, with a value of 11 or 1, depending on what's been played already.
int value_of_card(int input_card, int ace_position) {
	int real_value;

	if ((input_card > 10) && (input_card < 14))
		real_value = 10;
	else if ((input_card == 14) & (ace_position == -1))
		real_value = 11;
	else if ((input_card == 14) & (ace_position != -1))
		real_value = 1;
	else if (input_card <= 10)
		real_value = input_card;

	return real_value;
}

int main() {
	int dealer_cards[7]; // Dealer's cards. Array is used to keep the cards as independent values.
	int my_cards[7]; // Player's cards. Array is used to keep the cards as independent values.
	
	int number_of_dealer_cards,number_of_my_cards,sum_dealer_cards,sum_my_cards,temp_counter,stand,dealer_ace_position,my_ace_position;

	char input[20];
	char consume;

	srand((unsigned int)time(NULL));
	
	// Deal the first two cards to both the dealer and the player. Create the initial sum values and set the number of cards each to 2.
	dealer_ace_position = -1;
	my_ace_position = -1;
	
	// Deal cads to the player first. If an ace comes up, save that position as the ace position.
	my_cards[0] = value_of_card(rand() % (14 - 2 + 1) + 2,my_ace_position);
	if (my_cards[0] == 11)
		my_ace_position = 0;
	my_cards[1] = value_of_card(rand() % (14 - 2 + 1) + 2,my_ace_position);
	if (my_cards[1] == 11)
		my_ace_position = 1;
	sum_my_cards = my_cards[0] + my_cards[1];
	number_of_my_cards = 2;
	
	// Deal cads to the dealer second. If an ace comes up, save that position as the ace position.
	dealer_cards[0] = value_of_card(rand() % (14 - 2 + 1) + 2,dealer_ace_position);
	if (dealer_cards[0] == 11)
		dealer_ace_position = 0;
	dealer_cards[1] = value_of_card(rand() % (14 - 2 + 1) + 2,dealer_ace_position);
	if (dealer_cards[1] == 11)
		dealer_ace_position = 1;
	sum_dealer_cards = dealer_cards[0] + dealer_cards[1];
	number_of_dealer_cards = 2;

	printf("Welcome to the Blackjack game!\n\nPress Enter to play!\n\n");
	
	// Wait for the player to hit Enter.
	getchar();

	printf("The dealer:\n? + %d\n\n",dealer_cards[1]);
	printf("You:\n%d + %d = %d\n\n",my_cards[0],my_cards[1],sum_my_cards);
	
	stand = 0;

	// This is the player's loop. The loop continues if the player has chosen hit.
	// If the player choses stand (or types anything but "hit") then they have chosen to stand and the loop ends.
	while(stand == 0) {
		printf("Would you like to \"hit\" or \"stand\"? ");
		
		scanf("%19s",&input);
		scanf("%c",&consume);
		
		if (strcmp(input,"stand") == 0) //if (input[0] != 'h')
			stand = 1;
		else if (strcmp(input,"hit") == 0) {
			my_cards[number_of_my_cards] = value_of_card(rand() % (14 - 2 + 1) + 2,my_ace_position); // Add a new card to the array.
			if (my_cards[number_of_my_cards] == 11)
				my_ace_position = number_of_my_cards;

			sum_my_cards += my_cards[number_of_my_cards]; // Add new card to the sum of my cards.

			number_of_my_cards++; // Increase the number of my cards. This is done after adding a new card since the array is zero indexed.

			if (sum_my_cards > 21) {
				if ((my_ace_position < 0)) { // If there is no ace worth 11, or there is but it has already been switched to 1, then the player busts.
					printf("\nBusted!\n");
					stand = 1;
				}
				if (my_ace_position >= 0) { // If there is an ace that is still worth 11, then it changes to 1 if the player would be going over.
					my_cards[my_ace_position] = 1;
					sum_my_cards -= 10;
					my_ace_position = -2;
				}
			}
			
			// Print the current cards and their sum.
			printf("\nYou:\n%d",my_cards[0]);
			for(temp_counter = 1;temp_counter < number_of_my_cards;temp_counter++)
				printf(" + %d",my_cards[temp_counter]);
				
			printf(" = %d\n",sum_my_cards);
			
			if(number_of_my_cards == 7)
				stand = 1;
		}
		else
			printf("You entered something invalid. Try again.\n "); // If the player enters something other than hit or stand.
	}

	stand = 0;
	while (stand == 0) {
		if ((sum_dealer_cards >= 17) || (sum_my_cards > 21))
			stand = 1;
		else {

			dealer_cards[number_of_dealer_cards] = value_of_card(rand() % (14 - 2 + 1) + 2,dealer_ace_position); // Add a new card to the array.
			if (dealer_cards[number_of_dealer_cards] == 11)
				dealer_ace_position = number_of_dealer_cards;

			sum_dealer_cards += dealer_cards[number_of_dealer_cards]; // Add new card to the sum of my cards.

			number_of_dealer_cards++; // Increase the number of my cards. This is done after adding a new card since the array is zero indexed.

			if (sum_dealer_cards > 21) {
				if ((dealer_ace_position < 0)) { // If there is no ace worth 11, or there is but it has already been switched to 1, then the player busts.
					printf("\nDealer busted!\n");
					stand = 1;
				}
				if (dealer_ace_position >= 0) { // If there is an ace that is still worth 11, then it changes to 1 if the player would be going over.
					dealer_cards[my_ace_position] = 1;
					sum_dealer_cards -= 10;
					dealer_ace_position = -2;
				}
			}

			if (sum_dealer_cards >= 17)
				stand = 1;
		}
	}
	
	printf("\nDealer has: %d\n",sum_dealer_cards);

	if (sum_my_cards > 21)
		printf("Dealer wins!\n");
	else if (sum_dealer_cards > 21)
		printf("Player wins!\n");
	else if (sum_dealer_cards > sum_my_cards)
		printf("Dealer wins!\n");
	else if (sum_dealer_cards < sum_my_cards)
		printf("Player wins!\n");
	else if (sum_dealer_cards == sum_my_cards)
		printf("Dealer wins!\n");

	// Wait for enter to be pressed. This way it the result can be seen without opening cmd first on windows.
	getchar();

	return 0;
}
