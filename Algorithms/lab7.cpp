//
//  main.cpp
//  lab 7
//
//  Created by Daniel Campos on 10/23/12.
//  Copyright (c) 2012 Daniel Campos. All rights reserved.
//

#include <iostream>
#include <cmath>
using namespace std;
double minimum_num_coings(double n)
{
    int min_coins=0;
    while(n!=0)
    {
        while(n>=50)
        {
            n=n-50;
            min_coins++;
        }
        while(n>=25)
        {
            n=n-25;
            min_coins++;
        }
        while(n>=10)
        {
            n=n-10;
            min_coins++;
        }
        while(n>=5)
        {
            n=n-5;
            min_coins++;
        }
        while(n>=1)
        {
            n=n-1;
            min_coins++;
        }
    }
    return min_coins;
}
int main(int argc, const char * argv[])
{    int ammount_average=0;
    for(int i=1;i<99;i++)
    {
        ammount_average+=minimum_num_coings(i);
    }
    ammount_average=ammount_average/98;
    cout<<ammount_average<<endl;
    int temp=minimum_num_coings(85);
    cout<<temp<<endl;
    return 0;
}

