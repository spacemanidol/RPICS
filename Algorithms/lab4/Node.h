#ifndef H_Node
#define H_Node
#include <iostream>
#include <fstream>
using namespace std;

template <class Type>
struct nodeType
{
    Type info;
    nodeType<Type> *link;
};

#endif