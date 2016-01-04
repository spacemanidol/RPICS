#ifndef H_Graph
#define H_Graph
#include "UnorderedLinkedList.h"

template <class Type>
class Graph {
public:
    bool isEmpty() const {return (gSize == 0);}
    void createGraph()
    {
        int index, vertex, adj_v;
        int count = 0;
        
        if (gSize != 0)
            clearGraph();
        
        cout << "Enter size\n";
        cin >> gSize;
        
        for (index=0; index<gSize; index++)
        {
            cout << "Enter vertex\n";
            cin >> vertex;
            if (graph[index].isEmptyList())
                graph[index].insertFirst(vertex);
            cout << "Enter adjacent vertex\n";
            cin >> adj_v;
            while ( adj_v != 0 )
            {
                graph[vertex].insertLast(adj_v);
                cin >> adj_v;
            }
            cout << index << "th vertex.\n" << '\n';
        }
    }
    void clearGraph()
    {
        int index;
        
        for (index=0; index<gSize; index++)
            graph[index].destroyList();
        
        gSize = 0;
    }
    void printGraph() const
    {
        int index;
        
        for (index=0; index<gSize; index++)
        {
            graph[index].print();
            cout << '\n';
        }
        cout << '\n';
    }
    void depthFirstTraversal()
    {
        for(int j=0; j<gSize; j++)
        {
            graph[j].pre = 0;
            graph[j].post = 0;
        }
        
        int counter = 1;
        bool *visited;
        visited = new bool[gSize];
        
        for(int i=0; i<gSize; i++)
            visited[i] = false;
        
        for(int j=0; j<gSize; j++)
        {
            if (!visited[j])
            {
                dft(j,visited, counter);
            }
        }
        delete[] visited;
        
        for(int k=0; k<gSize; k++)
            cout << "Vertex: " << graph[k].front() << "\t pre is: " << graph[k].pre << "\t post is: " << graph[k].post << '\n';
    }
    Graph(int size = 0)
    {
        maxSize = size;
        gSize = 0;
        graph = new unorderedLinkedList<Type>[size];
    }
    ~Graph() {clearGraph();}
protected:
    int maxSize;
    int gSize;
    unorderedLinkedList<int> *graph;
private:
    void dft(int v, bool visited[], int &counter)
    {
        visited[v] = true;
        if(graph[v].pre == 0)
        {
            graph[v].pre = counter;
            //cout << "Vertex: " << graph[v].front() << " pre is: " << graph[v].pre << '\n';
            counter++;
        }
        
        linkedListIterator<int> graphIt;
        for (graphIt = graph[v].begin(); graphIt != graph[v].end(); graphIt++)
        {
            int w = *graphIt;
            
            if(!visited[w])
                dft(w,visited, counter);
            
            if(graph[v].post == 0)
            {
                graph[v].post = counter;
                //cout <<"Vertex: " << graph[v].front() << " post is: " << graph[v].post << '\n';
                counter++;
            }
        }
    }
};



#endif