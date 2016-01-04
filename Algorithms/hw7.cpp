#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <string>
using namespace std;

//given in the x and y coordinates of two separate points it will return the distance
float distance(float x1,float y1, float x2, float y2)
{
	return abs((y1-y2)/(x1-x2));//must be absolute value since if not it will return negative distance sometimes
}
int main()
{
    struct city//a struct that represents the cities before they are put into a cluster.
    {
		float x_data;
		float y_data;
		string city_name;
     };
	struct clustered_city//same as previous but it has which cluster its in
	{ 
		float x_data;
		float y_data;
		string city_name;
		int cluster_number;
	};
	vector<city> cities;//a vector that stores all the cities before they are sorted
	vector<clustered_city> clustered_cities;//a vector that has all the cities with what cluster they are in
	vector<city> clusters;//a vector of all the central points of clusters
	float x_data_temp;//where the data will read in to
	float y_data_temp;
	string city_name_temp;
	ifstream input;
	input.open("input1.txt");//opens the file 
	while(input.good())//as long as input is possible
	{
		input>>x_data_temp;//saves the x data in my var
		input>>y_data_temp;//saves y value in my var
		input>>city_name_temp;//saves city name in my var
		city temp={};//temp struct to instert into the vector
		temp.x_data=x_data_temp;
		temp.y_data=y_data_temp;
		temp.city_name=city_name_temp;
		cities.push_back(temp);//adds to vector
	}
	int location_in_vector=0;
	input.close();
	for(int i=0; i<50;i++)//runs until 50 k_clusters are found
	{
		float greatest_distance=0;
		int location_of_greatest=0;
		float x_loc_curr=cities[location_in_vector].x_data;
		float y_loc_curr=cities[location_in_vector].y_data;
		string curr_city_name=cities[location_in_vector].city_name;
		cities.erase(cities.begin()+location_in_vector);//removes it so it isnt found to be the farthest again	
		for(int j=0;j<cities.size();j++)//loops through the vector and finds the plae with the furthest away and makes not of the location
		{
			float temp_distance=distance(x_loc_curr,y_loc_curr,cities[j].x_data,cities[j].y_data);
			if(temp_distance>greatest_distance)
			{
				location_of_greatest=j;
				greatest_distance=temp_distance;
			}
		}
		location_in_vector=location_of_greatest;//sets the new item to the furthest point away
		city temp={};//adds the previous point to the vector of cluster start points.
		temp.x_data=x_loc_curr;
		temp.y_data=y_loc_curr;
		temp.city_name=curr_city_name;
		clusters.push_back(temp);
	}
	int counter=0;
	while(cities.size()!=counter+1)//as long as counter is not at end of vector
	{
		int least_distance=10000000000000000;//arbitrarily large number.
		int location_of_least;
		for(int i=0;i<clusters.size();i++)//finds the point closest to it and saves the index
		{
			float temp_distance=distance(clusters[i].x_data,clusters[i].y_data,cities[counter].x_data,cities[counter].y_data);
			if(temp_distance<least_distance)
			{
				location_of_least=i;
				least_distance=temp_distance;
			}
		}
		clustered_city temp={};
		temp.x_data=cities[counter].x_data;
		temp.y_data=cities[counter].y_data;
		temp.city_name=cities[counter].city_name;
		temp.cluster_number=location_of_least+1;
		clustered_cities.push_back(temp);//adds the informtion of the city plus which k-cluster it is in and adds it to the clustered city vector
		counter++;
	}
	ofstream output;
	output.open("output.txt");
	for(int i=0; i<clusters.size();i++)//output in one way
	{
		output<<"K-Cluster City central point: "<<clusters[i].city_name<<" K-cluster number: "<<i+1<<endl;//outputs the central point
		output<<"Cities Present in this cluster: "<<endl;//then will output all the cities in said cluster
		for(int j=0;j<clustered_cities.size();j++)
		{
			if((i+1)== clustered_cities[j].cluster_number)//if the cluster and the city share a number then they are in the same cluster
			{
				output<<"City Name: "<<  clustered_cities[j].city_name<<endl;
			}
		}
		output<<endl;
	}
	output.close();
}