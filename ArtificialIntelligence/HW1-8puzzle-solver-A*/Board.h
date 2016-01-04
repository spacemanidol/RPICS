//
//  Board.h
//  hw1 temp
//
//  Created by Erasmus on 9/29/13.
//  Copyright (c) 2013 Erasmus. All rights reserved.
//

#include<iostream>
#include<algorithm>
#include<stdio.h>
#include<vector>
using namespace std;
#define DEG 3
#ifndef hw1_temp_Board_h
#define hw1_temp_Board_h


class Boardstate {
public:
    int board[DEG][DEG];
    int g,f;
    Boardstate *came_from;
    Boardstate() {
        g=0;
        f=0;
        came_from = NULL;
    }
    static int heuristic(Boardstate from,Boardstate to) {
        int i,j,dist=0;
        for(i=0;i<DEG;i++)
        {
            for(j=0;j<DEG;j++)
            {
                if(from.board[i][j] != to.board[i][j])
                {
                    dist++;
                }
            }
        }
        return dist;
    }
    bool operator ==(Boardstate a) {
        for(int i=0;i<DEG;i++)
        {
            for(int j=0;j<DEG;j++)
            {
                if(this->board[i][j] != a.board[i][j])
                {
                    return false;
                }
            }
            
        }
        return true;
    }
    
	void print() {
		for(int i=0;i<DEG;i++)
        {
			for(int j=0;j<DEG;j++)
            {
                if(j==2)
                {
                    cout<<board[i][j];
                }
                else{
                    cout<<board[i][j];
                }
                
            }
			cout<<endl;
		}
	}
    void printNo(){
        for(int i=0;i<DEG;i++)
        {
            for(int j=0;j<DEG;j++)
            {
                if(board[i][j]==0)
                {
                    cout<<"  ";
                }
                else{
                    if(j==2)
                    {
                        cout<<board[i][j]<<" ";
                    }
                    else{
                        cout<<board[i][j]<<" ";
                    }
                }
                
            }
            cout<<endl;
        }
    }
};

#endif
