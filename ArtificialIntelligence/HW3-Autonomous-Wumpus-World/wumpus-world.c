#include <stdio.h>
#include <stdlib.h>
#include <time.h>		
#include <math.h>
#define NOFBITS	   	12
#define WUMPUS      2048
#define PIT       1024
#define GLITTER        512
#define WUMPUSSUS   256
#define PITSUS     128
#define GLITTERSUS     64
#define BREEZE      32
#define STENCH      16
#define GLITTERING     8
#define VISITED     4
#define SAFE 		2
#define CURRENT     1
struct coord{int x;int y;};
//TOUCH THIS sWhen loading a new txt file change this
#define NOROWS 		5		// Number Of rows
#define NOCOLS 		5		// Number Of columns
//STOP TOUCH
//By defining it like this it allows me not to worry about the order i defined my operations in.
int realWorld[NOCOLS][NOROWS];int score;int arrow;
int calcDirrection(int agentWorld[NOCOLS][NOROWS], struct coord sourceLocation, struct coord destinationLocation);
int followGold(int agentWorld[NOCOLS][NOROWS], struct coord field);
int beSafe(int agentWorld[NOCOLS][NOROWS], struct coord field);
int gambleTime(int agentWorld[NOCOLS][NOROWS], struct coord field);
int killYourself(int agentWorld[NOCOLS][NOROWS], struct coord field);
int testFlag(int world[NOCOLS][NOROWS], struct coord field, int action);
int shootWumpus(int realWorld[NOCOLS][NOROWS] ,int agentWorld[NOCOLS][NOROWS],struct coord location);
int randInt(int min, int max, int *except, int noExcept);
double calcDistance(int agentWorld[NOCOLS][NOROWS], struct coord sourceLocation, struct coord destinationLocation);
void removePercepts(int World[NOCOLS][NOROWS],int action);
void displayWorld(int world[NOROWS][NOCOLS]);
void neighborFieldsCoords(struct coord field, struct coord results[4]);
void setFlag(int world[NOCOLS][NOROWS], struct coord field, int action);
void generateRealWorld(int realWorld[NOCOLS][NOROWS]);
void evaluateNeighbors(int agentWorld[NOCOLS][NOROWS],int realWorld[NOCOLS][NOROWS], struct coord field);
void moveOneField(int agentWorld[NOCOLS][NOROWS], int direction, struct coord sourceLocation );
void removeFlag(int world[NOCOLS][NOROWS], struct coord field, int action);
void copyFlags(int fromWorld[NOCOLS][NOROWS], int toWorld[NOCOLS][NOROWS], struct coord field);
void myPause (int posx,int posy);
struct coord getagent(int world[NOCOLS][NOROWS]);
int main(int argc, char *argv[]) 
{
	srand((unsigned int)time(0));//funtion for when we are gambling
	int realWorld[NOCOLS][NOROWS];	// Array of ints holding real world map
	int agentWorld[NOCOLS][NOROWS];	// Array of ints holding agents world map
	int x, y;						// Counters
	struct coord agent={0,0};		// Current position coordinates
	arrow=1;
	for(y=0; y<NOROWS; y++)
	{
		for(x=0; x<NOCOLS; x++)
		{
			realWorld[x][y] = 0;//set both worlds to empty
			agentWorld[x][y] = 0;
		}
	}
	printf("RPI Artificial Intelligence Fall 2013\nRCS:CAMPOD2\nRIN:660996361");
	printf("Welcome to Daniel Campos Wumpus world autonomous agent solver.\nAs is, the program will run and get to its end state(Win or dead)\nThen it will output score, final agent board and initial board\nTo see each step remove commented out function calls\n");
	printf("World loaded looks like\n");
	generateRealWorld(realWorld);//Load file wumpus.txt and make the real world from it including percepts
	printf("Begin Program\n---------------\n");
	setFlag(agentWorld, agent, CURRENT);//adds the flags to the agent world to its current possition and that jazz
	score=0;
	while(1)
	{
		agent = getagent(agentWorld);//after the agent has moved we update its possition
		if(testFlag(agentWorld, agent, VISITED) == 0)//only go into this if statement if we have not been here before
		{
			copyFlags(realWorld,agentWorld,agent);//gives the agent an updated knowledge of what is around them
			if(testFlag(realWorld, agent, PIT))//agent is now in a pitt
			{
				printf("The Computer fell into a pit, died and thus lost!\n");
				score-=1000;
				break;
			} 
			else if(testFlag(realWorld,agent,WUMPUS))//Agent is same place as wumpus so eaten
			{
				printf("The Computer Was eaten by the Wumpus and thus lost!\n");
				score-=1000;
				break;
			} 
			else if(testFlag(realWorld,agent,GLITTER))//Agent has found the gold
			{
				removePercepts(agentWorld,VISITED);
				printf("The Computer found gold and thus won the game!\n");
				score+=1000;
				break;
			} 
			else{//no end condition so keeps on going
				if(testFlag(agentWorld,agent,GLITTERING))
				{
					printf("There is a GLITTER in here!\n");
				}
				if(testFlag(agentWorld,agent,BREEZE))
				{
					printf("There is a BREEZE in here!\n");
				}
				if(testFlag(agentWorld,agent,STENCH))
				{
					printf("There is a STENCH in here!\n");
				}
				setFlag(agentWorld, agent, VISITED);//set to visited for future state
				evaluateNeighbors(agentWorld,realWorld,agent);//check surrounding area to figure out where we want to go
			}
		}
		//printf("Agent world.\n");
		//displayWorld(agentWorld);
		//printf("RealWorld.\n");
		//displayWorld(realWorld);
		if(followGold(agentWorld, agent))//1 if we move towards gold
		{
			//first we try to go for gold if that percept isnt near
		} 
		else if(beSafe(agentWorld, agent))//1 if we can go foward
		{
			//we try to go for the safe route
		} 
		else if(gambleTime(agentWorld, agent))//1 if we can gamble
		{
			//we have no safe routes so we go random and hope for the best
		} 
		else if(killYourself(agentWorld, agent))//we have lost and have to die
		{
			//we are in a horrible situation and cant win
		}
		myPause(agent.x, agent.y);
		score--;
	}
	printf("\nGAME OVER\nSCORE:%d\n",score);
	printf("This is what the Agent thought the world looked like.\nBesides the pit, wumpus, etc location, it also has suspeected, visited and safe.\nSee documentation for key\n");
	displayWorld(agentWorld);
	printf("This is what the world really looked like\n");
	displayWorld(realWorld);
	for(y=0; y<NOROWS; y++)
	{
		for(x=0; x<NOCOLS; x++)
		{
			realWorld[x][y] = 0;//set both worlds to empty
			agentWorld[x][y] = 0;
		}
	}
	return EXIT_SUCCESS;
}
/*
   input:both worlds, a location
   output: an 1 or 0 represeting if wumpus was hit or not
   This function will shoot at the wumpus if the computer is sure where it is. it is very cautios so doesnt use arrow much
 */
int shootWumpus(int realWorld[NOCOLS][NOROWS] ,int agentWorld[NOCOLS][NOROWS],struct coord location)
{
	int i;//we only will shoot when perfectly certain. arrows are expensive
	for( i=0;i<NOROWS;i++)//shoot vertically
	{
		struct coord shootCoord={i, location.y};
		if(testFlag(realWorld, shootCoord, WUMPUS)==1)
		{
			printf("SCREAM!You have killed the wumpus!\n");
			removeFlag(realWorld,shootCoord, WUMPUS);
			removeFlag(agentWorld,shootCoord, WUMPUS);
			score+=500;
			return 1;
		}
	}
	for( i=0;i<NOCOLS;i++)//shoot laterally
	{
		struct coord shootCoord={location.x, i};
		if(testFlag(realWorld, shootCoord, WUMPUS)==1)
		{
			printf("SCREAM!You have killed the wumpus!\n");
			removeFlag(realWorld,shootCoord, WUMPUS);
			removeFlag(agentWorld,shootCoord, WUMPUS);
			score+=500;
			return 1;
		}
	}
	return 0;
}
/*
   input:agent world, the source location, destination location
   output:and integer representing which direction to go to reach the safe field
   This function takes in the agent world, its source location and its destination and returns how to get there
 */
int calcDirrection(int agentWorld[NOCOLS][NOROWS], struct coord sourceLocation, struct coord destinationLocation){
	int z, i, shortestGoodNeighbor;
	double shortestDist;
	struct coord neighbors[4];
	int goodNeighbors_i=0;
	int goodNeighbors_size=0;
	int *goodNeighbors = NULL;
	int *goodNeighbors_temp = NULL;
	double *distances = NULL;
	double *distances_temp = NULL;
	neighborFieldsCoords(sourceLocation, neighbors);
	for(z=0; z<4; z++)//size checks first
	{
		if((((neighbors[z].y < NOROWS) && (z == 0))||((neighbors[z].x < NOCOLS) && (z == 1))||((neighbors[z].y >= 0) && (z == 2))||((neighbors[z].x >= 0) && (z == 3)))&&(testFlag(agentWorld, neighbors[z], VISITED)))
		{
			if(goodNeighbors_i == 0)//we have to allocate memmore cause C can be horrible
			{
				goodNeighbors_size++;
				if ((goodNeighbors = (int*)calloc(goodNeighbors_size, sizeof(int))) == NULL) 
				{
					fprintf(stderr, "Error allocating memory.\n");
					exit(EXIT_FAILURE);
				}
				if ((distances = (double*)calloc(goodNeighbors_size, sizeof(double))) == NULL) 
				{
					fprintf(stderr, "Error allocating memory.\n");
					exit(EXIT_FAILURE);
				}
			}
			else if(goodNeighbors_i == goodNeighbors_size)//once again resize stuff cause of C
			{
				goodNeighbors_size++;
				if ((goodNeighbors_temp = (int*)realloc(goodNeighbors, goodNeighbors_size * sizeof(int))) == NULL) 
				{
					fprintf(stderr, "Ran out of memory while expanding except array.\n");
					free(goodNeighbors);
					exit(EXIT_FAILURE);
				}
				if ((distances_temp = (double*)realloc(distances, goodNeighbors_size * sizeof(double))) == NULL) 
				{
					fprintf(stderr, "Ran out of memory while expanding except array.\n");
					free(distances);
					exit(EXIT_FAILURE);
				}
				goodNeighbors = goodNeighbors_temp;
				distances = distances_temp;
			}
			goodNeighbors[goodNeighbors_i] = z;
			distances[goodNeighbors_i] = calcDistance(agentWorld,neighbors[z], destinationLocation);
			goodNeighbors_i++;
		}
	}
	if(goodNeighbors != 0)//as long as we have some good neighbor
	{
		shortestGoodNeighbor = goodNeighbors[0];
		shortestDist = distances[0];
		for(i=1;i<goodNeighbors_i;i++)
		{
			if(distances[i] < shortestDist)//keep track of the shortest distance to safe neighbor
			{
				shortestGoodNeighbor = goodNeighbors[i];
				shortestDist = distances[i];
			}
		}
	}
	free(goodNeighbors);//memory man
	free(distances);
	return shortestGoodNeighbor;
}
/*
   input:agent world, a location
   output:1 or 0 if we were able to move towards the gold
   This function looks around for glitter and tries to use it to find the gold
 */
int followGold(int agentWorld[NOCOLS][NOROWS], struct coord field){
	int z;
	struct coord neighbors[4];
	neighborFieldsCoords(field, neighbors);
	for(z=0; z<4; z++)//As long as the way it wants to move isnt a boundry place
	{
		if((((neighbors[z].y < NOROWS) && (z == 0))||((neighbors[z].x < NOCOLS) && (z == 1))||((neighbors[z].y >= 0) && (z == 2))||((neighbors[z].x >= 0) && (z == 3)))&&(testFlag(agentWorld, neighbors[z], GLITTER)))
		{
				moveOneField(agentWorld, z, field);
				return(1);
		}
	}
	return(0);
}
/*
   input:agent world, a location
   output:1 or 0 if we could move to safe place or not
   This function looks around for the safest place because it cant find the glitter
 */
int beSafe(int agentWorld[NOCOLS][NOROWS], struct coord field){
	int random,z;
	struct coord neighbors[4];
	neighborFieldsCoords(field, neighbors);
	int except_i;				//we will use an array of eceptions
	int except_size;		
	int *exceptSF = NULL;			//Array with exceptions 
	int *exceptSF_temp = NULL;
	except_i = 0;
	except_size = 0;
	for(z=0; z<4; z++)//If location is not safe or within the bounds of the board then it adds them to the array of exceptions to possible moves
	{
		if((((neighbors[z].y > NOROWS-1) && (z == 0))||((neighbors[z].x > NOCOLS-1) && (z == 1))||((neighbors[z].y < 0) && (z == 2))||((neighbors[z].x < 0) && (z == 3)))||(testFlag(agentWorld, neighbors[z], VISITED))||(testFlag(agentWorld, neighbors[z], SAFE) == 0))
		{
			if(except_i == 0)
			{
				except_size++;
				if ((exceptSF = (int*)calloc(except_size, sizeof(int))) == NULL) {
					fprintf(stderr, "Error allocating memory.\n");
					exit(EXIT_FAILURE);
				}
			}
			else if(except_i == except_size)
			{
				except_size++;
				if ((exceptSF_temp = (int*)realloc(exceptSF, except_size * sizeof(int))) == NULL) 
				{
					fprintf(stderr, "Ran out of memory while expanding except array.\n");
					free(exceptSF);
					exit(EXIT_FAILURE);
				}
				exceptSF = exceptSF_temp;
			}
			exceptSF[except_i] = z;
			except_i++;
		}
	}
	if (except_i<4)// as long as one of the moves is possible it will take a random move that is safe and possible
	{
		random = randInt(0,3,exceptSF,except_i);
		moveOneField(agentWorld, random, field);//moves the agent in some direction that is free and safe
		free(exceptSF);
		return(1);
	}
	else//there are no safe zones we can move into so this method wont help us move
	{
		free(exceptSF);
		int x,y,direction;//now we look for the closest neighbor that will likley be safe so that we can try to get there
		struct coord nearestSafeFieldCoord = { -1, -1 };
		for(x=0; x<NOROWS; x++)//search through the agent world to find first safe field and try to get there
		{
			for(y=0; y<NOCOLS; y++)
			{
				struct coord testSafeFieldCoord = { x, y };
				if(testFlag(agentWorld, testSafeFieldCoord, SAFE))// first nearest safe field
				{
					if((nearestSafeFieldCoord.x == -1) && (nearestSafeFieldCoord.y == -1))
					{
						nearestSafeFieldCoord = testSafeFieldCoord;
					}
					else{// It this is not first SAFE field, we check is it nearest one
						if(calcDistance(agentWorld,field,testSafeFieldCoord) < calcDistance(agentWorld, field,nearestSafeFieldCoord))
						{
							nearestSafeFieldCoord = testSafeFieldCoord;
						}
					}
				}
			}
		}
		if((nearestSafeFieldCoord.x == -1) && (nearestSafeFieldCoord.y == -1))		// No safe fields so we cant use the function
		{
			return(0);
		} 
		else{//some close safe field so we try to move toward it and hope for the best
			direction = calcDirrection(agentWorld,field,nearestSafeFieldCoord);
			moveOneField(agentWorld,direction,field);
			return(1);
		}
	}
}
/*
   input:agent world, a location
   output:1 or 0 wether or not we could by gambling to the maybe safe places
   This function is activated when there is no gold nearby, we dont know where anything nearby is safe so we just guess and hope for the best
 */
int gambleTime(int agentWorld[NOCOLS][NOROWS], struct coord field)
{
	int random,f;
	int tempi=0;
	int tempsize=0;
	int *exceptRND = NULL;
	int *exceptRND_temp = NULL;
	struct coord neighbors[4];//places we can go;
	neighborFieldsCoords(field,neighbors);
	for(f=0;f<4;f++)//we  remove the places that we cant go because they are walls or out of bands and stuff
	{
		if((((neighbors[f].y > NOROWS-1) && (f == 0)) ||((neighbors[f].x > NOCOLS-1) && (f == 1))|| ((neighbors[f].y < 0) && (f == 2))||((neighbors[f].x < 0) && (f == 3)))||(testFlag(agentWorld, neighbors[f], VISITED)))
		{
			if(tempi == 0)//i regret choosing C, I do not want to manage memory, luckily there were methods online
			{
				tempsize++;
				if((exceptRND = (int*)calloc(tempsize, sizeof(int))) == NULL) 
				{
					fprintf(stderr, "Error allocating memory.\n");
					exit(EXIT_FAILURE);
				}
			}
			else if(tempi == tempsize)
			{
				tempsize++;
				if ((exceptRND_temp = (int*)realloc(exceptRND, tempsize * sizeof(int))) == NULL) {
					fprintf(stderr, "Ran out of memory while expanding except array.\n");
					free(exceptRND);
					exit(EXIT_FAILURE);
				}
				exceptRND = exceptRND_temp;
			}
			exceptRND[tempi] = f;
			tempi++;
		}
	}
	if (tempi<4)//hope we dont die
	{
		random = randInt(0,3,exceptRND,tempi);
		moveOneField(agentWorld, random, field);
		free(exceptRND);
		return(1);
	} 
	else//well i guess we have to kill ourselves
	{
		free(exceptRND);
		return(0);
	}
}
/*
   input:agent world, a location
   output:1 or 0 if we were able to kill ourselves or not
   we have no hope in this world. Everywhere we want to go will kill us and thus we must die to get out of the game
 */
int killYourself(int agentWorld[NOROWS][NOCOLS], struct coord field)
{
	int random,f;
	int tempi=0;
	int tempsize=0;
	int *exceptRND = NULL;
	int *exceptRND_temp = NULL;
	struct coord neighbors[4];//places we can go;
	neighborFieldsCoords(field,neighbors);
	for(f=0;f<4;f++)//check for each of our neighbors
	{
		if((((neighbors[f].y > NOROWS-1) && (f == 0)) ||((neighbors[f].x > NOCOLS-1) && (f == 1))|| ((neighbors[f].y < 0) && (f == 2))||((neighbors[f].x < 0) && (f == 3)))||(testFlag(agentWorld, neighbors[f], VISITED)))
		{
			if(tempi == 0)
			{
				tempsize++;
				if((exceptRND = (int*)calloc(tempsize, sizeof(int))) == NULL) 
				{
					fprintf(stderr, "Error allocating memory.\n");
					exit(EXIT_FAILURE);
				}
			}
			else if(tempi == tempsize)
			{
				tempsize++;
				if ((exceptRND_temp = (int*)realloc(exceptRND, tempsize * sizeof(int))) == NULL) {
					fprintf(stderr, "Ran out of memory while expanding except array.\n");
					free(exceptRND);
					exit(EXIT_FAILURE);
				}
				exceptRND = exceptRND_temp;
			}
			exceptRND[tempi] = f;
			tempi++;
		}
	}
	if(tempi<4)//kill youself man
	{
		random = randInt(0,3,exceptRND,tempi);
		moveOneField(agentWorld, random, field);
		free(exceptRND);
		return(1);
	} 
	else//if we cant kill ourselves something is really wrong and life is screwed
	{
		free(exceptRND);
		return(0);
	}
}
/*
   input:a world, a location, and what we are looking for
   output:1 or 0 wether or not the action or state exists at this point, e.g. is there a wumpus, a glitter, etc. see readme for table codes
   this function checks if the location we are at has the flag we are looking for or not
 */
int testFlag(int world[NOCOLS][NOROWS], struct coord field, int action)
{
	if(((world[field.x][field.y] & action) == action))
	{return 1;} 
	else 
	{return 0;}
}
/*
   input:min, max, the amount of exceptions, and amount of no exceptions
   output:the random choice we will take to gamble
   we use this function so it tells us randomly which way to go
 */
int randInt(int min, int max, int *except, int noExcept){
	int result, i;
	result = rand() % (max - min + 1) + min;
	if(except != NULL)
	{
		for(i=0;i<noExcept;i++)
		{
			if(result == except[i])
			{
				result = randInt(min,max,except,noExcept);
				break;
			}
		}
	}
	return result;
}
/*
   input:agent world, a start location, a end location
   output:distance between points
  gives us distance between two points, used to figure out if the move we want is possible.
 */
double calcDistance(int agentWorld[NOCOLS][NOROWS], struct coord sourceLocation, struct coord destinationLocation){
	double distance;//standard distance thing
	distance = sqrt((double)pow((double)destinationLocation.x-sourceLocation.x,2)+(double)pow((double)destinationLocation.y-sourceLocation.y,2));
	return distance;
}
/*
   input:a world
   output:the location where our player currently is living/ making camp
   This function searches the board and when it finds the player returns their location
 */
struct coord getagent(int world[NOCOLS][NOROWS]){
	int x,y;
	struct coord agent = { -1, -1 };
	for(x=0; x<NOROWS; x++)//search
	{
		for(y=0; y<NOCOLS; y++)
		{
			struct coord testCoord = { x, y };
			if(testFlag(world, testCoord, CURRENT))//we found em
			{
				return testCoord;
			}
		}
	}
	return agent;//no find
}
/*
   input:a world
   output:nothing
   prints the world in its decimal form
 */
void displayWorld(int world[NOROWS][NOCOLS]){
	int x, y;
	for(y=NOROWS-1; y>=0; y--){
		for(x=0; x<NOCOLS; x++){
		  printf("%d",world[x][y]);
		  printf("\t");
		}
		printf("\n");
	}
	fflush(stdout);
}
/*
   input:a location, surrounding locations
   output:modified results array with what the surrounding locations are
   Function makes the array for the location of the surrounding places
 */
void neighborFieldsCoords(struct coord field, struct coord results[4]){
	results[0].x = field.x;//Up
	results[0].y = field.y + 1;//Up
	results[1].x = field.x + 1;//Right
	results[1].y = field.y;//Right
	results[2].x = field.x;//Down
	results[2].y = field.y - 1;//Down
	results[3].x = field.x - 1;//left
	results[3].y = field.y;//left
}
/*
   input:a world, a location, an action
   output:none
   function removes flags from the location such as visited or wumpus when its shot
 */
void removeFlag(int world[NOCOLS][NOROWS], struct coord field, int action){
	world[field.x][field.y] = world[field.x][field.y] & ~action;
}
/*
   input:a world, a world,a location
   output:none
   coppies the flags in one world to new world
 */
void copyFlags(int fromWorld[NOCOLS][NOROWS], int toWorld[NOCOLS][NOROWS], struct coord field)
{
	toWorld[field.x][field.y] |= fromWorld[field.x][field.y];
}
/*
   input:a world, a location, an action
   output:none
   remove the flags of something once it has been visited becasue visited means for sure safe so we dont need to know
 */
void setFlag(int world[NOCOLS][NOROWS], struct coord field, int action){
	if(action == VISITED)
	{
		removeFlag(world, field, SAFE);
		removeFlag(world, field, PITSUS);
		removeFlag(world, field, GLITTERSUS);
		removeFlag(world, field, WUMPUSSUS);
	}
	world[field.x][field.y] = world[field.x][field.y] | action;
}
/*
   input:a world
   output:none
   function loads the wumpus world file from text, makes the world, and based on location of things adds percepts
 */
void generateRealWorld(int realWorld[NOCOLS][NOROWS])
{
	int i,j, c;
	char buffer[200];
	char token[50];
	char *mode= "r";
	struct coord xy;
	struct coord neighbors[4];
	FILE * inputToBeRead;
 	inputToBeRead =fopen("wumpus.txt",mode);
	if(inputToBeRead==NULL)//fail to open and things
	{
	 fprintf(stderr,"Cant find or open file wumpus.txt\n Please fix it\n");
	 exit(1);
	}
	else
	{
	    for(i=NOROWS-1;i>=0;i--)//pulls from file and also prints our cave
	    {
	    	for(j=0;j<NOROWS;j++)
	    	{
	    		struct coord curr={j,i};
	    		c = fgetc(inputToBeRead);
				while(c==44||c==10||c==32)//comma or newline
				{c = fgetc(inputToBeRead);}
				if(c==87)//Wumpus
				{
					realWorld[j][i]=2048;//wumpus added
					printf(" W");
				}
				else if(c==80)//pit
				{
					realWorld[j][i]=1024;//pit added
					printf(" P");
				}
				else if(c==71)//gold
				{
					realWorld[j][i]=512;//gold added
					printf(" G");
				}
				else //any other input will be a X
				{
					printf(" X");//other dont add 0 cause its already there
				}
	    	}
	    	printf("\n");
	    }
	}
	//displayWorld(realWorld);
	for(xy.x=0; xy.x<NOROWS; xy.x++)//loops through the real world and looks at each point looks 
	{
		for(xy.y=0; xy.y<NOCOLS; xy.y++)
		{
			if(testFlag(realWorld, xy, PIT)==1)//pit at location adding breeze percepts
			{
				neighborFieldsCoords(xy, neighbors);//we only add to the neighbors that arent out of bounds hence the loop
				for(j=0; j<4; j++)
				{
					if(((neighbors[j].y < NOROWS) && (j== 0))||((neighbors[j].x < NOCOLS) && (j == 1))||((neighbors[j].y>= 0) && (j == 2))||((neighbors[j].x >= 0) && (j == 3)))
					{
						setFlag(realWorld, neighbors[j], BREEZE);	
					}
				}
			}
			else if(testFlag(realWorld, xy, WUMPUS)==1)//wumpus at location, adding stench to board
			{
				neighborFieldsCoords(xy, neighbors);//we only add to the neighbors that arent out of bounds hence the loop
				for(j=0; j<4; j++)
				{
					if(((neighbors[j].y < NOROWS) && (j== 0))||((neighbors[j].x < NOCOLS) && (j == 1))||((neighbors[j].y>= 0) && (j == 2))||((neighbors[j].x >= 0) && (j == 3)))
					{
						setFlag(realWorld, neighbors[j], STENCH);
					}
				}
			}
			if(testFlag(realWorld, xy, GLITTER)==1)//gold at location adding glitter to board
			{
				neighborFieldsCoords(xy, neighbors);//we only add to the neighbors that arent out of bounds hence the loop
				for(j=0; j<4; j++)
				{
					if(((neighbors[j].y < NOROWS) && (j== 0))||((neighbors[j].x < NOCOLS) && (j == 1))||((neighbors[j].y>= 0) && (j == 2))||((neighbors[j].x >= 0) && (j == 3)))
					{
						setFlag(realWorld, neighbors[j], GLITTERING);
					}
				}
			}
		}
	}
}
/*
   input:a world, location
   output:none
   looks at the neighbor arounds first to see if visited or not and stuff. If not visited figures out the percepts
 */
void evaluateNeighbors(int agentWorld[NOCOLS][NOROWS],int realWorld[NOCOLS][NOROWS], struct coord field){
	int z;
	struct coord neighbors[4];		// Neighbors coordinates
	neighborFieldsCoords(field, neighbors);// Get neighbors coordinates
	for(z=0; z<4; z++)
	{
		if((((neighbors[z].y < NOROWS) && (z == 0))||((neighbors[z].x < NOCOLS) && (z == 1))||((neighbors[z].y >= 0) && (z == 2))||((neighbors[z].x >= 0) && (z == 3)))&&(testFlag(agentWorld, neighbors[z], SAFE) == 0)&&(testFlag(agentWorld, neighbors[z], VISITED) == 0))
		{	//cant be out of bounds or already marked as visited or safe
			if(testFlag(agentWorld, field, BREEZE))//feel a brezze
			{
				if(testFlag(agentWorld, neighbors[z], PITSUS))//already has suspect pit and there is a breeze so should be pit
				{
					setFlag(agentWorld, neighbors[z], PIT);
				}
				else
				{
					setFlag(agentWorld, neighbors[z], PITSUS);//mark with suspect
				}
			}
			if(testFlag(agentWorld, field, STENCH))//smell bad stuff
			{
				if(testFlag(agentWorld, neighbors[z], WUMPUSSUS))//prev thought there was wumpus there, plus our percep so for sure
				{
					setFlag(agentWorld, neighbors[z], WUMPUS);
					if(arrow!=0)//we shoot to kill
					{
						shootWumpus(realWorld,agentWorld,field);
						removePercepts(realWorld,STENCH);
						removePercepts(agentWorld,STENCH);
						removePercepts(agentWorld,WUMPUSSUS);
						arrow=0;
						setFlag(agentWorld,neighbors[z], SAFE);//We killed the wumpus
					}
				}
				else
				{
					setFlag(agentWorld, neighbors[z], WUMPUSSUS);//mark as suspect
				}
			}
			if(testFlag(agentWorld, field, GLITTERING))//I like gold
			{
				if(testFlag(agentWorld, neighbors[z], GLITTERSUS))//prev thought there was gold there, plus our percep so for sure
				{
					setFlag(agentWorld, neighbors[z], GLITTER);
				}
				else
				{
					setFlag(agentWorld, neighbors[z], GLITTERSUS);//mark as suspect
				}
			}
			if((testFlag(agentWorld, field, BREEZE) == 0)&&(testFlag(agentWorld, field, STENCH) == 0))//nothing here
			{
				setFlag(agentWorld, neighbors[z], SAFE);//No percepts so we mark this place as safe and remove all other tags we thought may be here
				removeFlag(agentWorld, neighbors[z], PITSUS);//we were wrong
				removeFlag(agentWorld, neighbors[z], WUMPUSSUS);
				removeFlag(agentWorld, neighbors[z], PIT);
				removeFlag(agentWorld, neighbors[z], WUMPUS);
			}
		}
	}
}
/*
   input:a world,a direction, a location
   output:none
   based on inputs move our computer in the direction it wants to
 */
void moveOneField(int agentWorld[NOCOLS][NOROWS], int direction, struct coord sourceLocation )
{
	struct coord temp;
	agentWorld[sourceLocation.x][sourceLocation.y] = agentWorld[sourceLocation.x][sourceLocation.y] & ~CURRENT;
	if(direction==0)//moves up
	{
		temp.x = sourceLocation.x;
		temp.y = sourceLocation.y+1;
		setFlag(agentWorld, temp, CURRENT);
		printf("The Computer is looking North and wants to move forward\n");
	}
	else if(direction==1)//moves right
	{
		temp.x = sourceLocation.x+1;
		temp.y = sourceLocation.y;
		setFlag(agentWorld, temp, CURRENT);
		printf("The Computer is looking East and wants to move forward\n");
	}
	else if(direction==2)//move down
	{
		temp.x = sourceLocation.x;
		temp.y = sourceLocation.y-1;
		setFlag(agentWorld, temp, CURRENT);
		printf("The Computer is looking South and wants to move forward\n");
	}
	else if(direction==3)//moves left
	{
		temp.x = sourceLocation.x-1;
		temp.y = sourceLocation.y;
		setFlag(agentWorld, temp, CURRENT);
		printf("The Computer is looking West and wants to move forward\n");
	}
}
/*
   input:a world, action
   output:none
   removes all traces of that action from the world
 */
void removePercepts(int World[NOCOLS][NOROWS],int action)
{
	struct coord xy;
	for(xy.x=0; xy.x<NOROWS; xy.x++)//loops through the real world and looks at each point looks 
	{
		for(xy.y=0; xy.y<NOCOLS; xy.y++)
		{
			if(testFlag(realWorld, xy, action))//if percept present then remove
			{
				removeFlag(World,xy, action);
			}
		}
	}
}
/*
   input:x pos, y pos
   output:none
   tells user where they are and waits for an enter to move to the next step
 */
void myPause(int posx,int posy){
	printf("\nYou are in room [%d,%d]of the CAVE.\nThe computer can either shoot, turn or move.\nPress[Enter] To have the computer execute it next Move\n",posx+1,posy+1);
	fflush ( stdout );
  	getchar();	
}