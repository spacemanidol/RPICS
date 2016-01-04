Project By:
	Zoe Konrad
	Daniel Campos

	Everything in the Assignment works according to the test cases provided. None of the extracredit was implemented. We would have submited it early but the test cases changed values and as they were posted late sunday were delayed by that. 
Part 1 
	1. For our deck representation we decided to represent the deck as 52 numbers in a list. Since Suit does not matter for BlackJack,nor our assignment, Aces would be represented by 1s, Jacks Represented by 10s, Queens Represented by 10s, and Kings Represented by 10s since they all have the same value in the eyes of blackjack. All other cards were represented by their value (2-10). This produced a list with: 4 1s(aces),4 2s, 4 3s, 4 4s, 4 5s, 4 6s, 4 7s, 4 8s, 4 9s, 16 10s(4*(10 King Queen Jack)).
	declare Deck = [1 2 3 4 5 6 7 8 9 10 10 10 10
		1 2 3 4 5 6 7 8 9 10 10 10 10
		1 2 3 4 5 6 7 8 9 10 10 10 10
		1 2 3 4 5 6 7 8 9 10 10 10 10]

	2. The Shuffle function works perfectly as per matching Yuris output and test cases perfectly 
	3. We wrote 2 functions to Compute Hand Value 1 Hard(Ace as 11) the other Soft(Ace as 1)
Part 2
	The program plays BlackJack when given the input {BlackJack S N M B R}} within reason
	It works with all the test cases Yuri provided, and Matches his output. You must run them 1 by 1, if not, the program will crash.