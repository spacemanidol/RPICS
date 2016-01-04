Wumpus World in C Played in full by agent
=====================

Wumpus world is an old board game. There is a player whose goal is to go into a cave, find the gold and get out without getting killed by the wumpus or falling in a pit. They can shoot one arrow and they know the percepts of when either gold is near wumpus is near or pit is near. I made an Articifial intelligence version of this that based on what it knows about what surrounds it, the program will make the best descicion it can. This will result in hopefully mostly winning and occasional death.

Requirements
===============

* A standard C Library

Instructions
--------------
From github
git clone https://github.com/dfcf93/autonomous-wumpus.git 
cd wumpus-world-agent
gcc -o  wumpus-world wumpus-world.c
./wumpus-world


For class 
Make any modifications to the wumpus.txt file you want
modify the #define NOROWS && #define NOCOLS so the programs runs the best. They are highlighted and are in the section with touch above it
gcc -o  wumpus-world wumpus-world.c
./wumpus-world

any modifications to what type of map to read should be done for the wumpus.txt which is a n*m(where n can = m). If you do modify the input you must change the NOCOLS to whatever you M is and the NOROWS to what your N is. This is highligted in the wumpus-world.c document and is preset to be 5 and 5.  When you load a new file please change the M and N parameters. Thank You. Also when changing the files of wumpus.txt feel free to use the ones in wingame. Those are some games I used for testing. sometimes the win sometimes they lose and those were my best scores.
/*
 ============================================================================
I store the actual world inside a matrix. This actual world comes fromt the
document that I read in at the begining of the program I store the world 
that the agent percieves in another ont. Each element in the matrix represents
what is percieved at each point. Initially all points are set to zero and as 
the game plays and more is know it is also expanded. The terms are all values 
of 2^n and thus can be represented by binary and by adding them we can never 
have a false that we would get by adding two percepts together. EG if Wumpus=4
and glitter=1 and breeze=3 then we would get that if the state had both it is a 
wumpus so wrong
	Dec	 -  Description	    - Symbol
	-----------------------------------------
	2048 -  Wumpus					- W
	1024 -  Cave					- C
	512  -  Gold					- G
	256  -  WUMPUSSUS 				- w
	128  -  CAVESUS					- s
	64   -  GOLDSUS		 			 -g
	32   -  Breeze					- B
	16   -  Stench					- T
	8    -  Glitter					- L
	4    -  Visited					- V
	2    -  Safe					- S
	1    -  Current					- A
 ============================================================================
 */
