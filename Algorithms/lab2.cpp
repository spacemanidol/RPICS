//
//  main.cpp
//  algo lab 2
//
//  Created by Daniel Campos on 9/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>
#include <math.h>
using namespace std;
int  modexp( int x, int y,  int n)
{
    if(y==0)
    {
        return 1;
    }
    else
    {
        int z= modexp(x,(y/2),n);
        if((y%2)==0)
        {
            return (z*z)%n;
        }
        else
        {
            return (x*(z*z))%n;
        }
    }
}
bool isPrimeNumber(int num)
{
    if (num <=1)
        return false;
    else if (num == 2)         
        return true;
    else if (num % 2 == 0)
        return false;
    else
    {
        bool prime = true;
        int divisor = 3;
        double num_d = static_cast<double>(num);
        int upperLimit = static_cast<int>(sqrt(num_d) +1);
        
        while (divisor <= upperLimit)
        {
            if (num % divisor == 0)
                prime = false;
            divisor +=2;
        }
        return prime;
    }
}

int main(int argc, const char * argv[])
{

    // insert code here...
    int a= modexp(2, 125, 127);
    std::cout<<a<<std:: endl;
    std::cout << "Hello, World!\n";
    for(int i=1; i<1000; i++)
    {
        bool isprime=isPrimeNumber(i);
        if(isprime==true)
        {
            cout<<i<<",";
        }
    }
    return 0;
}

