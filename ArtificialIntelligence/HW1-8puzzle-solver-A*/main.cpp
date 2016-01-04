#include<iostream>
#include<algorithm>
#include<stdio.h>
#include<vector>
#include "Board.h"
using namespace std;

//Global Variables
#define DEG 3	//Number of rows and columns
const bool SUCCESS=true;//Jusft for how i write things
vector<Boardstate> output;// A vector that we will write the correct answer into
int correct_solution[3][3] = {{1,2,3},{4,5,6},{7,8,0}};


/*
 input: Two different boards
 output:whichever board has a smaller F value
 simple boolean which will tell which board has a smaller f and therefore should be used
 */
bool lowerF(Boardstate a,Boardstate b) {
	return a.f < b.f;
}

/*
 input:A board, a vector of board states
 output:True or false
 Given a set of boardstates and a board it returns if that move is in the set and possible
 */
bool isInSet(Boardstate a,vector<Boardstate> b) {
	for(int i=0;i<b.size();i++)
		if(a == b[i])
			return true;
    
	return false;
}

/*
 input:A board, two sets of possition coordinates
 output:None
 this function takes in a board, the location of the point that the point wants to be in and the position it is in. It swaps the two points
 */
void swap(Boardstate &a,int i,int j,int i2,int j2) {
	int temp;
	temp = a.board[i][j];
	a.board[i][j] = a.board[i2][j2];
	a.board[i2][j2] = temp;
}

/*
 input:Current board state, the Goal board state. The initial i and j postion and the goal i and j. a vector of the open set and of the closed set
 output:None
 this function takes in a board, the location of the point that the point wants to be in and the position it is in. It swaps the two points
 */
void addNeighbor(Boardstate current,Boardstate goal, int newI, int newJ, int posi, int posj,vector<Boardstate> &openset,vector<Boardstate> closedset)
{
    
	Boardstate newstate = current;
	swap(newstate,newI,newJ,posi,posj);
    if(!isInSet(newstate,closedset))
    {
        int tentative_g_score = current.g + 1;
        if( !isInSet(newstate,openset) || tentative_g_score < newstate.g )
        {
            newstate.g = tentative_g_score;
            newstate.f = newstate.g + Boardstate::heuristic(newstate,goal);
            Boardstate *temp = new Boardstate();
            *temp = current;
            newstate.came_from = temp;
            openset.push_back(newstate);
        }
    }
}

/*
 input:Current board, Goal Board, vector of open set and closed set.
 output:None
 board locates where the zero is and based on its location adds the legal moves
 */
void neighbors(Boardstate current,Boardstate goal, vector<Boardstate> &openset,vector<Boardstate> closedset) {
	int i=0;
    int j=0;
    int posi=0;
    int posj=0;
	
	//loop to find the location of the 0 in the board
	for(i=0;i<DEG;i++)
    {
		for(j=0;j<DEG;j++)
        {
            if(current.board[i][j] == 0)
			{
				posi=i;
				posj=j;
			}
        }
    }
	i=posi;
    j=posj;
    // This next section adds the  moves that are possible using the i and j location to see what location they would be on map, at most 4 neighbors at least 2
	
    //Adds any those that arent the top row so can go upward with the 0
    if((i-1)>=0)
    {
		addNeighbor(current,goal,(i-1),j,posi,posj,openset,closedset);
	}
	//Adds any those that arent on the bottom row so can go downard with the 0
	if((i+1)<DEG)
    {
		addNeighbor(current,goal,(i+1),j,posi,posj,openset,closedset);
	}
    
    //Adds any those that are not in the leftmost column so the 0 can go to the left
	if((j-1)>=0)
    {
		addNeighbor(current,goal,i,(j-1),posi,posj,openset,closedset);
	}
	
    //Adds any those that are not in the rightmost column so the 0 can go to the right
	if((j+1)<DEG)
    {
		addNeighbor(current,goal,i,(j+1),posi,posj,openset,closedset);
	}
}

/*
 input:Current board, vector of boardstates it came from
 output:None
 This function takes the final node and recreates the path from the begining so we can latter print it out
 */
void reconstruct_path(Boardstate final, vector<Boardstate> &came_from) {
    Boardstate *temp = &final;
    while(temp != NULL) {
        came_from.push_back(*temp);
        temp = temp->came_from;
    }
}

/*
 input:Board Start Position, End position
 output:True of false if the A star could solve the 8 puzzle true is success false is falure
 An a star search done using a priority queue. If the item isnt the path it is popped
 */
bool astar(Boardstate start, Boardstate goal) {
	//Configure everything and set the G and F functions to the heuristics and g to zero
    vector<Boardstate> closedset;
	vector<Boardstate> openset;
	Boardstate current;
	start.g = 0;
	start.f = start.g + Boardstate::heuristic(start,goal);
    
	openset.push_back(start);//add our initial item to the priority queue
	
	while(!openset.empty())// we keep on going until either a solution is found or the open set is empty which in that case means we do not have a solvable puzzle
    {
		sort(openset.begin(),openset.end(),lowerF);//we sort this so the begining of the vector has the lowest heuristic
		current = openset[0]; //select the state having lowest fscore value.
		//If goal then we win
        if(current == goal)
        {
			reconstruct_path(current,output);
			return SUCCESS;
		}
        
		openset.erase(openset.begin());//Delete
		closedset.push_back(current);//add it to current
		neighbors(current,goal,openset,closedset);//add all of the neighbor possiblemoves to the queue
	}
	return !SUCCESS;//No solution so we failed and puzzle can be solved
}

/*
 input:the number, its row and its
 output:the manhattan distance a singular number is away from its goal state
 it tells how many moves are required to get a number to its goal state
 */
int manhattan_Distance_singlular(int number, int row,int column)
{
    int distance = abs(row-number/3)+abs(column-number%3);
    if(number==0)
    {
        return 0;
    }
    else{
        return distance;
    }
    
}
/*
 input: puzzle
 output: the sum of all manhattan distances
 based on http://faculty.kutztown.edu/rieksts/447/projects/26puzz/testManh.html description on how to compute the manhattan distance of a puzzle
 takes in the puzzle and creates the sum of all manhattan distances
 */
int manhattan_Distance_puzzle(int puzzle[3][3])
{
    int distance=0;
    for(int i=0; i<3 ;i++)
    {
        for(int j=0; j<3 ;j++)
        {
            distance=distance+manhattan_Distance_singlular(puzzle[i][j],i,j);//add to total
        }
    }
    return distance;
}
/*
 input:puzzle
 output:none
 given a array of puzzle it is printed
 */
void printPuzzle(int puzzle[3][3])
{
    for(int i=0; i<3 ;i++)
    {
        for(int j=0; j<2 ;j++)
        {
            if(puzzle[i][j]==0)
            {
                cout<<"  ";
            }
            else{cout<<puzzle[i][j]<<" ";}
        }
        cout<<puzzle[i][2]<<endl;
    }
    
}
/*
 input:Number of permutations
 outputs:Wether the 8 puzzle is solvable or not
 If odd then not solvable, true otherwise. if it is odd then it informs and exits
 */
bool is_solvable(int permutations)
{
    if (permutations%2==0)
    {
        return true;
        //cout<<"Puzzle is solvable.\n Working toward solving it\n...\n";
        
    }
    else{
       // cout<<"Puzzle is not solvable. Please try again.\n Bye!\n";
        return false;
    }
}

/*
 input:a one dimensional array of the puzzle to make the math easier, counter defining which number is being refered to
 output:amount of permutations of each number
 */
int permutations_number(int permutation_puzzle[9], int counter)
{
    int less_than=0;
    int number=permutation_puzzle[counter];
    if(number==0)
    {
        return 8-counter;//needs to be in last spot
    }
    for(int i=counter;i<9;i++)
    {
        if(permutation_puzzle[i]<number&& permutation_puzzle[i]!=0)//if numbers are smaller then they have permutatins
        {
            less_than++;
        }
    }
    return less_than;
}
/*
 input: a one dimensional array of the puzzle to make the math easier
 output: the amount of permutations possible in the puzzle
 based on http://www.gamedev.net/topic/595060-solving-n-puzzles/ and work we did in class. if even solvable, not if otherwise
 */
int permutations_puzzle(int permutation_puzzle[9])
{
    int permutations=0;
    for(int i=0; i<8 ;i++)
    {
        permutations+=permutations_number(permutation_puzzle, i);
    }
    return permutations;
}
/*
 input: puzzle board, one dimensional puzzle board
 output:puzzle board, one dimensional puzzle board but madeup of user inputed values
 asks users for numbers, if not correct number or repeated number it quits.
 */
void UserInputBoard(int puzzle[3][3],int permutation_puzzle[9])
{
    int temp=0;
    int counter=0;
    for(int i=0; i<3 ;i++)
    {
        for(int j=0; j<3 ;j++)
        {
            cin>>temp;
            if(temp<0||temp>8)
            {
                //cout<<"Please input values from 0 to 8, without repetition\n";
                cin>>temp;
            }
            puzzle[i][j]=temp;
            permutation_puzzle[counter]=temp;
            counter++;
        }
    }
    cout<<"Your Puzzle is bellow\n";
    printPuzzle(puzzle);
    cout<<"Your Puzzles Manhattan distance is:"<<manhattan_Distance_puzzle(puzzle)<<endl;
}
/*
 input:none
 output:none
 Sets up my outputs, prints the proper solution and asks the user to input
 */
void startup()
{
    cout<<"Name: Daniel Campos\nRCS:660996361\n";
    cout<<"Artificial Inteligence HW1 8 Puzzle Manhattan Distance\n";
    cout<<"The goal state will look like the cube bellow. All manhattan distances and and permutation sare based on this goal state\n";
    printPuzzle(correct_solution);
    cout<<"Enter the initial state for the Cube \n";
    cout<<"Please input values from 0 to 8, without repetition, one at a time  where 0 stands for the blank square\n";
}

/*
 input:a one dimensional array of the puzzle to make the math easier, counter defining which number is being refered to
 output:amount of permutations of each number
 */
int main() {
	Boardstate start,goal;
	int puzzle[3][3];
    int permutation_puzzle[9];
    startup();
    UserInputBoard(puzzle, permutation_puzzle);
    
    //sets start board to user input
    for(int i=0;i<DEG;i++)
    {
		for(int j=0;j<DEG;j++)
        {
            start.board[i][j]=puzzle[i][j];
        }
    }
    
    
    //sets goal state to correct solution
    for(int i=0;i<DEG;i++)
    {
		for(int j=0;j<DEG;j++)
        {
            goal.board[i][j]=correct_solution[i][j];
        }
    }
    cout<<"Starting to solve it...\n";
	if(astar(start,goal) == SUCCESS)
    {
        for(int i=output.size()-1;i>=0;i--)//Prints out the order if succesful and the amount of moves
        {
            output[i].print();
            cout<<endl;
        }
        cout<<"\n"<<output.size()-2<<"\n";
        output[output.size()-1].printNo();
        cout<<"\nSUCCESSFUL.\nNumber of moves: "<<output.size()-2<<endl;//-2 to remove the start and end solution so only getting ammount of moves
	}
	else
    {
        cout<<"\nUNSUCCESSFUL.\n Something must have gone wrong or it isnt solvable\n";
    }	
	return 0;
}

