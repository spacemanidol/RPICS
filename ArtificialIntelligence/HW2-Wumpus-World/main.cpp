#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <string>
#include <algorithm>
using namespace std;
/*
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
*/

/*
 input: number representing direction
 output: The string of the cardinal direction represented by the direction
 Prints whatever is the correct cardinal direction
 */
string directionPrint(int number)
{
 string north="NORTH";string east="EAST";string south="SOUTH";string west="WEST";
 if(number==0)
 {
  return north;
 }
 else if(number==1)
 {
  return east;
 }
 else if(number==2)
 {
  return south;
 }
 else if(number==3)
 {
  return west;
 }
 else{
  cout<<"criticall error\n Closing everything. Sorry\n";//stuff would have gone wrong
  exit(0);
 }
}
class boardGame {
 public:
  int moves;
  int score;
  int arrows;
  int direction;
  int posx;
  int posy;
  int size;
  /*
   input: a number representing the N*N dimension
   output:none
   sets all the values for operation
 */
  void startGame(int n)
  {
   moves=0;
   score=0;
   arrows=1;
   direction=1;
   posx=1;
   posy=1;
   size=n;
  }
  /*
   input:None
   output:None
   Prints out the information for the player to use
 */
   void shoot()
   {
    arrows=0;
   }
  void printState()
  {
   cout<<"You are in room ["<<posx<<","<<posy<<"]of the cave. Facing "<<directionPrint(direction);
   cout<<".\nWhat would you like to do? Please enter command [R,L,F,S]:\n";
  }
  /*
  input: none
  output:none
  rotates the direction 90 degrees to the right
 */

  void turnRight()
  {
   if(direction==3)
   {
    direction=0;
   }
   else{
    direction++;
   }
  }
/*
  input: none
  output:none
  rotates the direction 90 degrees to the left
 */
  void turnLeft()
  {
   if(direction==0)
   {
    direction=3;
   }
   else{
    direction--;
   }
  }
  /*
  input:the current position in the array to modify
  output:none
  This function checks to see if the direction that is trying to be moved in is a wall. if not it moves the directions and the corresponding array coordinates
 */
  bool moveForward(int  & arrayPosx,int  & arrayPosY)
  {
   if(direction==3&&posx!=1)//Moving West
   {
    posx--;
    score--;
    arrayPosx--;
    return true;
   }
   else if(direction==2&&posy!=1)// Moving South
   {
    posy--;
    score--;
    arrayPosY++;
    return true;
   }
   else if(direction==1&&posx!=size)//Moving East
   {
    posx++;
    score--;
    arrayPosx++;
    return true;
   }
   else if(direction==0&&posy!=size)//Moving North
   {
    posy++;
    score--;
    arrayPosY--;
    return true;
   }
   else{
    cout<<"BUMP!!! You hit a wall!\n";
    return false;
   }
  }
};
/*
 input: The wumps board game
 output:a vector of all the inputs that later is turned into the n by n array
 This function opens the file, reads the size, initializes my class and then reads all the values into a vector and return it
 */
vector<string> readInput(boardGame &Wumpus)
{
 ifstream wumpus;
 wumpus.open("wumpus.txt");
 int size=0;
 wumpus>>size;
 string value;
 vector<string>inputs;
 int max=size*size;
 for(int i=0;i<max;i++)
 {
  getline( wumpus, value, ',' );
  value.erase(std::remove(value.begin(), value.end(), '\n'), value.end());//removes the newline from the end of a line
  inputs.push_back(value);
 }
 Wumpus.startGame(size);
 return inputs;
}
/*
 input: a vector of the surrounding values on the board
 output:true or false if the wumpus is surrounding the character or not
 this function takes in all the surronding values in the board and if the wumpus is near it informs the character
 */
bool wumpusClose(vector<string>surrounding)
{
 for(int i=0;i<surrounding.size();i++)
 {
  if(surrounding[i]=="W")
  {
   cout<<"There is a STENCH in here!\n";
   return true;
  }
 }
 return false;
}
/*
 input: a vector of the surrounding values on the board
 output:true or false if the gold is surrounding the character or not
 this function takes in all the surronding values in the board and if the gold is near it informs the character
 */
bool GoldClose(vector<string>surrounding)
{
 for(int i=0;i<surrounding.size();i++)
 {
  if(surrounding[i]=="G")
  {
   cout<<"There is a GLITTER in here!\n";
   return true;
  }
 }
 return false;
}
/*
 input: a vector of the surrounding values on the board
 output:true or false if the pit is surrounding the character or not
 this function takes in all the surronding values in the board and if the pit is near it informs the character
 */
bool pitClose(vector<string>surrounding)
{
 for(int i=0;i<surrounding.size();i++)
 {
  if(surrounding[i]=="P")
  {
   cout<<"There is a BREEZE in here!\n";
   return true;
  }
 }
 return false;
}
/*
 input:a vector of the surrounding values on the board
 output:a number which represents what percepts are in each area
 this function returns what percept is experienced in each area 
 based on what is around it and has a numerical value for it 
 corresponding to the table at the entrance
 */
int precteptsOfArea(vector<string>surrounding)
{
 bool gold=GoldClose(surrounding);
 bool pit=pitClose(surrounding);
 bool wumpus=wumpusClose(surrounding);
 if(wumpus&&pit&&gold)
 {
  return 9;
 }
 if(wumpus&&pit)
 {
  return 8;
 }
 if(wumpus&&gold)
 {
  return 7;
 }
 if(pit&&gold)
 {
  return 6;
 }
 if(gold)
 {
  return 5;
 }
 if(wumpus)
 {
  return 4;
 }
 if(pit)
 {
  return 3;
 }
 else{return 10;}
}
int main(int argc, const char * argv[])
{
 boardGame Wumpus;
 int k=0;
 vector<string>inputs=readInput(Wumpus);
 int size=Wumpus.size;
 string board[size][size];//the wumpus board
 int percepts[size][size];//the board of percepts
 for(int i=0;i<size;i++)
 {
  for(int j=0;j<size;j++)
  {
   board[i][j]=inputs[k];
   percepts[i][j]=10;//sets the percepts to ten, the value of X
   k++;
  }
 }
ofstream mypercepts;
 mypercepts.open("kb.dat");//saves the knowledge base in the form of a array which each value representing the percepts of the area
 string input;
 int arrayPosX=0;//since we have an array our normnal coordinates dont work
 int arrayPosY=size-1;
 while(1)//repeating
 {
  Wumpus.printState();
  vector<string>surrounding;
  if(arrayPosX!=size-1)//if to the right isnt a wall adds whatever is there
  {
   surrounding.push_back(board[arrayPosY][arrayPosX+1]);
  }
  if(arrayPosX!=0)//if to the left there isnt a wall adds whatever is there
  {
   surrounding.push_back(board[arrayPosY][arrayPosX-1]);
  }
  if(arrayPosY!=size-1)//if to the top there isnt a wall then it adds it
  {
   surrounding.push_back(board[arrayPosY+1][arrayPosX]);
  }
  if(arrayPosY!=0)//if to the bottom there isnt a wall then it adds it
  {
   surrounding.push_back(board[arrayPosY-1][arrayPosX]);
  }
  int percept=precteptsOfArea(surrounding);//gets the percept code for arround
  percepts[arrayPosY][arrayPosX]=percept;//saves it to our array
  mypercepts<<percept<<"At X: "<<Wumpus.posx<<",Y: "<<Wumpus.posy<<endl;
  cout<<">";
  cin>>input;
  if(input=="R")//Rotate right
  {
   Wumpus.turnRight();
  }
  else if(input=="L")//Rotate Left
  {
   Wumpus.turnLeft();
  }
  else if(input=="F")//Move Forward
  {
   bool didMove=Wumpus.moveForward(arrayPosX,arrayPosY);
   if(didMove==false)
   {
    mypercepts<<"11 At X: "<<Wumpus.posx<<",Y: "<<Wumpus.posy<<endl;
   }
  }
  else if(input=="S")//shoot an arrow
  {
   int i=0;
   if(Wumpus.arrows==0)//no arrows to shoot
   {
    cout<<"You have no arrows to shoot\n";
   }
   else{
    cout<<"You are shooting and arrow\n";
    Wumpus.shoot();
    Wumpus.score=Wumpus.score-10;
    int direction=Wumpus.direction;
    if(direction==3)//ShootingWest
    {
     int i=0;
     for( i=arrayPosX;i>=0;i--)
     {
      if(board[arrayPosY][i]=="W")//it hits a wumpus
      {
       mypercepts<<"0 At X: "<<i<<",Y: "<<Wumpus.posy<<endl;//saying where the wumpus was
       cout<<"SCREAM!You have killed the wumpus!\n";
       Wumpus.score+=500;
       board[arrayPosY][i]="X";//removes the wumpus from board
       break;
      }
     }
    }
    if(direction==2)//ShootingSouth
    {
     for(i=arrayPosY;i<size;i++)
     {
      if(board[arrayPosY][i]=="W")//hits wumpus
      {
        mypercepts<<"0 At X: "<<Wumpus.posx<<",Y: "<<size-i<<endl;//saying where the wumpus was
        cout<<"SCREAM!You have killed the wumpus!\n";
        Wumpus.score+=500;
        board[i][arrayPosX]="X";//removes the wumpus from board
        break;
      }
     }
    }
    if(direction==1)//ShootingEast
    {
     for(i=arrayPosX;i<size;i++)
     {
      if(board[arrayPosY][i]=="W")//hits wumpus
      {
        mypercepts<<"0 At X: "<<i<<",Y: "<<Wumpus.posy<<endl;//saying where the wumpus was
        cout<<"SCREAM!You have killed the wumpus!\n";
        Wumpus.score+=500;
        board[arrayPosY][i]="X";//removes the wumpus from board
        break;
      }
     }
    }
    if(direction==0)//Shooting North
    {
     for(i=arrayPosY;i>=0;i--)
     {
      if(board[i][arrayPosX]=="W")//hhits wumpus
      {
       mypercepts<<"0 At X: "<<Wumpus.posx<<",Y: "<<size-i<<endl;//saying where the wumpus was
       cout<<"SCREAM!You have killed the wumpus!\n";
       Wumpus.score+=500;
       board[i][arrayPosX]="X";//removes the wumpus from board
       break;
      }
     }
    }
   }
  }
  else{
   cout<<"invalid input\n";
  }
  if(board[arrayPosY][arrayPosX]=="W")//The move landed the player in wumpus so dead
  {
    mypercepts<<0<<"At X: "<<Wumpus.posx<<",Y: "<<Wumpus.posy<<endl;//saying where the wumpus was
    Wumpus.score=Wumpus.score-1000;
    cout<<"You have been eaten by the Wumpus and thus have lost.\n";
    break;
  }
  if(board[arrayPosY][arrayPosX]=="G")//the move landed the player in gold so the won
  {
    mypercepts<<2<<"At X: "<<Wumpus.posx<<",Y: "<<Wumpus.posy<<endl;//saying where the gold was was
    Wumpus.score+=1000;
    cout<<"You have won the game! Congradulations\n";
    break;
  }
  if(board[arrayPosY][arrayPosX]=="P")//The move landed the player in pit so dead
  {
    mypercepts<<1<<"At X: "<<Wumpus.posx<<",Y: "<<Wumpus.posy<<endl;//saying where the pit was
    Wumpus.score=Wumpus.score-1000;
    cout<<"You have fallen into a pit and died and thus lost.\n";
    break;
  }
 }
 ofstream myperceptsarray;
 myperceptsarray.open("kbarray.dat");//saves the knowledge base in the form of a array which each value representing the percepts of the area
 cout<<"The game is over. Your score was "<<Wumpus.score<<endl;
 for(int i=0;i<size;i++)
 {
  for(int j=0;j<size-1;j++)
  {
    myperceptsarray<<percepts[i][j]<<",";
  }
  myperceptsarray<<percepts[i][size-1]<<endl;
 }
 myperceptsarray.close();//closes the document
 mypercepts.close();//saves and closes the document
}

