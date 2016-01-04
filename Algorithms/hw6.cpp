//
//  main.cpp
//  hw6
//
//  Created by Daniel Campos on 11/7/12.
//  Copyright (c) 2012 Daniel Campos. All rights reserved.
//

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;
int LCS(string X,string Y)
{
    if (Y.length() > X.length())
        swap(X,Y);
    int m = X.length(),n=Y.length();
    vector< vector<int> > c(2, vector<int>(n+1,0));
    int i,j;
    for (i=1;i<=m;i++)
    {
        for (j=1;j<=n;j++)
        {
            if (X[i-1]==Y[j-1])
                c[1][j]=c[0][j-1]+1;
            else
                c[1][j]=max(c[1][j-1],c[0][j]);
        }
        for (j=1;j<=n;j++)
            c[0][j]=c[1][j];
    }
    return (c[1][n]);
}
bool possible_to_make_change_using_v_coins(vector <int> coin_values, int v)
{
    for(int i=0;i<coin_values.size();i++)//loops through array of coins
    {
        v=v%coin_values[i];//since amount of coins is unlimited just done the modulus
    }
    if(v==0)//if the change can be made
    {return true;}
    else{return false;}
}
bool can_make_change_using_coins(vector<int>coins ,int v)
{
    for(int i=0;i<coins.size();i++)//runs through the coins
    {
        if(v>=coins[i])//as long as coin is removable it will
        {
            v=v-coins[i];//removes one coin
        }
    }
    if(v==0)//the amount is returnable
    {
        return true;
    }
    return false;
}
bool can_make_change_using_k_coins(vector<int>coins, int k, int v)
{
    int coins_used=0;//amount of used coins
    for(int i=0;i<coins.size();i++)
    {
        if(v>=coins[i])//as long as the the ammount is larger than the coin size
        {
            int temp=v/coins[i];//will give you amount of coins that can be removed
            v=v%coins[i];//modulus to remove the amount coins from v
            coins_used+=temp;//ups the amount of coins used
        }
    }
    if(coins_used<=k&&v==0)//if the amount of coins used is less than k and v is equal to 0
    {
        return true;
    }
    return false;
}
int main(int argc, const char * argv[])
{
    int out=longest_common_substring("abrcadabra","ababra" );
    cout<<"the longest value is: "<<out<<endl;
    vector <int> coins;
    vector<int>coins2;
    coins2.push_back(10);
    coins2.push_back(5);
    coins.push_back(20);
    coins.push_back(10);
    coins.push_back(5);
    coins.push_back(1);
    cout<<"is it possible to make change using each coins at most once for 15: "<<possible_to_make_change_using_v_coins(coins2,15)<<endl;
    cout<<"is it possible to make change using each coins at most once for 12: "<<possible_to_make_change_using_v_coins(coins2,12)<<endl;
    cout<<"is it possible to make change using at most 6 coins for 55: "<<can_make_change_using_k_coins(coins2, 6, 55)<<endl;
    cout<<"is it possible to make change using at most 6 coins for 65: "<<can_make_change_using_k_coins(coins2, 6, 65)<<endl;
    cout<<"is it possible to make change using each coin once for 16: "<<can_make_change_using_coins(coins, 16)<<endl;
    cout<<"is it possible to make change using each coin once for 31: "<<can_make_change_using_coins(coins, 31)<<endl;
    cout<<"is it possible to make change using each coin once for 40: "<<can_make_change_using_coins(coins, 40)<<endl;
    return 0;
}

