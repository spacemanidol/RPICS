Wumpus world

How to use

To compile use

g++ -o hw2 main.cpp
then ./

the map for the wumpus world must be in the format as attached which is

4
X,X,X,P,
W,G,P,X,
X,X,X,X,
X,X,P,X

this will be a 4 by 4 csv file. Sorry if this is inconviniet this is what I understood we had to do.

Then the game will play as it is supposed to. the user can do four things, turn left, turn right, move forward and shoot. They have one arrow. They win if they get the gold and lose if they get eaten or fall into a pit. The knowledge base is ouputed in two ways. i made a N by N matrix with values associated with the table at the bottom of the page to show what percepts are experienced at each point. Because we were also suposed to just have a statement into a knowledge base it also just output  something that follows the same number scheme but is as separate percepts. I would use the array for making it autonomous but the other one for class purposes.

example of kb.dat//this isnt an array
10At X: 1,Y: 1
10At X: 1,Y: 1
0 At X: 1,Y: 3
10At X: 1,Y: 1
10At X: 1,Y: 1
3At X: 2,Y: 1
3At X: 2,Y: 1
5At X: 2,Y: 2
2At X: 2,Y: 3


example of kbarray.dat
10,10,10,10
10,10,10,10
10,5,10,10
10,3,10,10

in this case all areas i havent visited have a percept of empty or X and will have a value of ten

hope this helps. 



format for what the percepts answers mean that are stored in the kb
	Number  Description    Symbol
	---------------------------------
	0		Wumpus          W
	1		Cave 			C
	2		Gold 			G
	3 		Breeze			B
	4		Stench			S
	5		Glitter			G
	6		Breeze Glitter	BG
	7		Stench Glitter	SG
	8		Breeze STENCH   BS
	9		Breeze Stench Glitter BSG
	10      Empty			X
    11      BUMP            B