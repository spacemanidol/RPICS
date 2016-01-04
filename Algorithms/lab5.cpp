#include "Graph.h"


int main()
{
	Graph<int> A(5);
	A.createGraph();
	A.depthFirstTraversal();
	A.is_tree();
	A.topologicalSort();
    
	return 0;
}