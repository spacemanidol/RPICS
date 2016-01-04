#include <iostream>
#include <vector>
#include <fstream>
#include <string>
#include <map>
using namespace std;
map<string,vector<string> > allWords;
map<string,vector<string> >::iterator it;
/*
    inputs:vector of strings representing the words
    outputs:none
    Actions:takes words in and creates a map with the key being the word and the value a vector of words that come next
*/
void insertWordsToMap(vector<string>words)
{
    for(int i=0;i<words.size();i++)
    {
        if(i+1!=words.size())//so you dont try to add the next element when there is none
        {
           	it = allWords.find(words[i]);
            if(it!=allWords.end())//word exists so we add the agacent words to out vector
            {
                it->second.push_back(words[i+1]);
            }
            else{
                vector<string>temp;
                temp.push_back(words[i+1]);
                allWords.insert(pair<string,vector<string> >(words[i],temp));//add to the map
            }
        }
    }
}
/*
    inputs:a string
    outputs:a all lower case string
    Actions:takes in a string in whatever way and makes it lower case
*/
string regularizeWord(string word)
{
    locale loc;
    for (std::string::size_type i=0; i<word.length(); ++i)
    { 
       word[i]=tolower(word[i],loc);
    }
    return word;
}
/*
    inputs:an input
    outputs:true or false
    Actions:if there is a space to signify two or more words it will reject
*/
bool isValid(string word)
{
    for(int i=0; i<word.size(); i++)
    { 
       if(word[i]==32)
        {
            return false;
        }
    }
    return true;
}
/*
    inputs:a vector of the words that come after the word, the word
    outputs:none
    Actions:creates a map to save the current words and then saves to this map with the key being string and value is amount of times
*/
void printProbability(vector<string> toGetProbability,string first)
{
    map<string,int>currWords;
    map<string,int>::iterator it2;
    float counter=0;
    float temp;
    for(int i=0;i<toGetProbability.size();i++)
    {
        counter++;
        it2=currWords.find(toGetProbability[i]);//check if it iexists
        if(it2!=currWords.end())//exists so increase counter
        {
            temp=it2->second;
            temp++;
            it2->second=temp;
        }
        else{//inserts the currently nonexisting pair into the map
            currWords.insert(pair<string,int>(toGetProbability[i],1));
        }
    }
    cout<<"\nAll word pairs with probability > 0:\n";
    for(map<string, int >::const_iterator it = currWords.begin();it != currWords.end(); ++it)
    {
        temp=it->second;
        cout <<first<<" "<< it->first <<" (probability: " << temp/counter<<")\n";
    }
}
int main(int argc, const char * argv[])
{
    string input;
    vector<string>words;
    ifstream myfile;
    myfile.open ("file.txt");
    vector<string>toGetProbability;
    if(!myfile) 
    { // file couldn't be opened
        cerr << "Error: file could not be opened" << endl;
        exit(1);
    }
    while(myfile>>input)//we regularize each word and we add it to a vector
    {
        words.push_back(regularizeWord(input));
    }
    insertWordsToMap(words);//taking the words make them intoa map
    while(1)
    {
        cout<<"\nPlease input a keyword:\n";
        getline(cin,input);
        while(!isValid(input))//if there is a space again an again
        {
            cout<<"Invalid input!\nPlease input a single keyword:\n";
            getline(cin,input);
        }
        input=regularizeWord(input);//regularize the letters
        it = allWords.find(input);
        if(it==allWords.end())//Doesnt exist in mexico
        {
            cout<<"Fail to find \""<<input<<"\" in text   (probability=0)\n";
        }
        else{
            toGetProbability=it->second;
            printProbability(toGetProbability,input);//all else is good so we print probabilities
        }
    }
    myfile.close();
    return 0;
}