//
//  main.cpp
//  lab 8
//
//  Created by Daniel Campos on 10/30/12.
//  Copyright (c) 2012 Daniel Campos. All rights reserved.
//

using namespace std;
#include <vector>
#include <iostream>
vector <int> longest_contiguous_subesquence(vector <int> a_vector)
{
    int bestmax=a_vector[0];
    int runningmax=0;
    vector <int> saved_best_max_values;
    vector <int> saved_runnign_max;
    saved_best_max_values.push_back(a_vector[0]);
    int i=0;
    for(int i=0; i<a_vector.size();i++)
    {
        runningmax=0;
        saved_runnign_max.clear();
        for(int j=i;j<a_vector.size();j++)
        {
            bool past_zero=false;
            if(runningmax<0)
            { past_zero=true;
                break;}
            runningmax=runningmax+a_vector[j];
            saved_runnign_max.push_back(a_vector[j]);
            if(runningmax>bestmax)
            {
                bestmax=runningmax;
                saved_best_max_values=saved_runnign_max;
                if(past_zero==true){
                    saved_runnign_max.clear();}
            }
        }
    }
    saved_best_max_values.push_back(bestmax);
    return saved_best_max_values;
}
int length_longest_nondecreasing(vector<int>a_vector)
{
    int bestlength=0;
    int length=0;
    for(int i=1;i<a_vector.size();i++)
    {
        int length=1;
        for(int j=i;j<a_vector.size()-1;j++)
        {
            if(a_vector[j-1]>a_vector[j])
            {
                length++;
            }
        }
        if(length>bestlength)
        {
            bestlength=length;
        }
    }
    return bestlength;
}
int distance_correct(int mile1,int mile2, int k)
{
    cout<<"mile 1 is "<<mile1<<" mile 2 "<<mile2<<" k is"<<k<<endl;
    if((mile1-mile2)<k)
    {return 0;}
    else{return 1;}
    

}int alp (int *m, int i, int j, int k)
{
	if (m[i]-m[j]<k)
		return 0;
	else
		return 1;
}

void expected_profit (int n, int * p, int *m, int k)
{
    int temp = 0;
	int profit[4] = {0,0,0,0};
	for (int i=1; i<n; i++)
	{
		for (int j=1; j<i-1; j++)
		{
			temp = profit[j] + alp(m, i, j,k)*p[i];
            
			if (temp>profit[i])
				temp = profit[i];
            
			if (profit[i]<p[i])
				profit[i]=p[i];
		}
	}
	cout << "The maximum expected profits at the locations are:\n ";
}

int main(int argc, const char * argv[])
{
    vector <int> t_vector;
    t_vector.push_back(5);
    t_vector.push_back(15);
    t_vector.push_back(-30);
    t_vector.push_back(10);
    t_vector.push_back(-5);
    int max_profit=10;
    t_vector.push_back(40);
    t_vector.push_back(10);
    int length=length_longest_nondecreasing(t_vector);
    cout<<length<<endl;
    vector <int> res_vector= longest_contiguous_subesquence(t_vector);
    for(int i=0;i<res_vector.size();i++)
    {
        cout<<res_vector[i]<<endl;
    }
  
    vector<int> profits;
    profits.push_back(5);
    profits.push_back(6);
    profits.push_back(5);
    profits.push_back(1);
    vector<int> miles;
    miles.push_back(6);
    miles.push_back(7);
    miles.push_back(12);
        miles.push_back(14);

    int m[4] = {6, 7, 12, 14};
	int p[4] = {5, 6, 5, 1};
	int n = 4;
	int k =6;
	expected_profit(n,p, m, k);
    cout<<"max profit"<<max_profit<<endl;
    
}

