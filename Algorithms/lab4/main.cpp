#include "Graph.h"


int main() 
{
	Graph<int> A(5);
	A.createGraph();
	A.depthFirstTraversal();
	return 0;
}