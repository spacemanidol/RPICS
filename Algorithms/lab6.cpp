#include <iostream>
#include <fstream>
#include <limits.h>
#include <map>
#include <vector>
#include <string>
#include <stack>
#include <list>
#include <queue>

using namespace std;

int INFINITY = INT_MAX;

class Vertex {
public:
    string           name;   // Vertex name
    vector<pair<int,Vertex*>> adj;    // Adjacent vertices
    int              dist;   // Cost
    Vertex          *path;   // Previous vertex on shortest path
	
    Vertex( const string & nm ) : name( nm )
    { reset( ); }
    
    void reset( )
    { dist = INFINITY; path = NULL; }
};

typedef map <string,Vertex *> vmap;
typedef pair<string,Vertex *> vpair;
typedef pair<int,Vertex *> wpair;

struct Compare : public std::binary_function<Vertex*, Vertex*, bool>
{
    bool operator()(const Vertex* lhs, const Vertex* rhs) const
    {
        if ( lhs->dist > rhs->dist )
            return true;
        else
            return false;
    }
};

class Graph
{
public:
    Graph( ) { }
    ~Graph( );
    void addEdge( const string & sourceName, const string & destName, const int & weight );
    void printPath( const string & destName ) const;
    void weighted( const string & startName );
    
private:
    Vertex * getVertex( const string & vertexName );
    void printPath( const Vertex & dest ) const;
    void clearAll( );
    
    vmap vertexMap;
    vector<Vertex *> allVertices;
};



void Graph::addEdge( const string & sourceName, const string & destName, const int & weight )
{
    Vertex * v = getVertex( sourceName );
    Vertex * w = getVertex( destName );
	pair <int, Vertex*> temp;
	temp = make_pair(weight, w);
    v->adj.push_back( temp );
}

void Graph::printPath( const string & destName ) const
{
    vmap::const_iterator itr = vertexMap.find( destName );
    
    if( itr == vertexMap.end( ) )
    {
        cout << "Destination vertex not found" << endl;
        return;
    }
    
    const Vertex & w = *(*itr).second;
    if( w.dist == INFINITY )
        cout << destName << " is unreachable";
    else
        printPath( w );
    cout << endl;
}

// If vertexName is not present, add it to vertexMap
// In either case, return the Vertex
Vertex * Graph::getVertex( const string & vertexName )
{
    vmap::iterator itr = vertexMap.find( vertexName );
    
    if( itr == vertexMap.end( ) )
    {
        Vertex *newv = new Vertex( vertexName );
        allVertices.push_back( newv );
        vertexMap.insert( vpair( vertexName, newv ) );
        return newv;
    }
    return (*itr).second;
}

void Graph::printPath( const Vertex & dest ) const
{
    if( dest.path != NULL )
    {
        printPath( *dest.path );
        cout << " to ";
    }
    cout << dest.name;
}

void Graph::clearAll( )
{
    for( int i = 0; i < allVertices.size( ); i++ )
        allVertices[ i ]->reset( );
}

Graph::~Graph( )
{
    for( int i = 0; i < allVertices.size( ); i++ )
        delete allVertices[ i ];
}


void Graph::weighted( const string & startName )
{
    clearAll( );
    
    vmap::iterator itr = vertexMap.find( startName );
    
    if( itr == vertexMap.end( ) )
    {
        cout << startName << " is not a vertex in this graph" << endl;
        return;
    }
    
    Vertex *start = (*itr).second;
    priority_queue<Vertex *, vector<Vertex *>, Compare > q;
    q.push( start ); start->dist = 0;
    
    while( !q.empty( ) )
    {
        Vertex *v = q.top();
		q.pop();
		v->dist = 0;
        
        for( int i = 0; i < v->adj.size( ); i++ )
        {
			Vertex *w = v->adj[ i ].second;
            if( w->dist == INFINITY )
            {
				w->dist = v->dist + v->adj[i].first;
                w->path = v;
                q.push( w );
            }
			else
				if ( w->dist > v->dist + v->adj[i].first )
				{
					w->dist = v->dist + v->adj[i].first;
					w->path = v;
				}
        }
    }
}

/**
 * Process a request; return false if end of file.
 */
bool processRequest( istream & in, Graph & g )
{
    string startName;
    string destName;
    
    cout << "Enter start node: ";
    if( !( in >> startName ) )
        return false;
    cout << "Enter destination node: ";
    if( !( in >> destName ) )
        return false;
    
    g.weighted( startName );
    g.printPath( destName );
    
    return true;
}


/**
 * A simple main that reads the file given by argv[1]
 * and then calls processRequest to compute shortest paths.
 * Skimpy error checking in order to concentrate on the basics.
 */
int main( int argc, char *argv[ ] )
{
    Graph g;
    
    if( argc != 2 )
    {
        cerr << "Usage: " << argv[ 0 ] << " graphfile" << endl;
        return 1;
    }
    
    ifstream inFile( argv[ 1 ] );
    if( !inFile )
    {
        cerr << "Cannot open " << argv[ 1 ] << endl;
        return 1;
    }
    
    string source, dest;
	int weight;
    
    // Read the vertices; add them to vertexMap
    while( inFile >> source >> dest >> weight )
    {
        g.addEdge( source, dest, weight );
    }
    
    cout << "File read" << endl;
    while( processRequest( cin, g ) )
        ;
    
    return 0;
}

 
 #include <cstdio>
 #include <vector>
 #include <algorithm>
 using namespace std;
 #define edge pair< int, int >
 #define MAX 1001
 // ( w (u, v) ) format
 vector< pair< int, edge > > GRAPH, MST;
 int parent[MAX], total, N, E;
 int findset(int x, int *parent)
 {
 if(x != parent[x])
 parent[x] = findset(parent[x], parent);
 return parent[x];
 }
 void kruskal()
 {
 int i, pu, pv;
 sort(GRAPH.begin(), GRAPH.end()); // increasing weight
 for(i=total=0; i<E; i++)
 {
 pu = findset(GRAPH[i].second.first, parent);
 pv = findset(GRAPH[i].second.second, parent);
 if(pu != pv)
 {
 MST.push_back(GRAPH[i]); // add to tree
 total += GRAPH[i].first; // add edge cost
 parent[pu] = parent[pv]; // link
 }
 }
 }
 void reset()
 {
 // reset appropriate variables here
 // like MST.clear(), GRAPH.clear(); etc etc.
 for(int i=1; i<=N; i++) parent[i] = i;
 }
 void print()
 {
 int i, sz;
 // this is just style...
 sz = MST.size();
 for(i=0; i<sz; i++)
 {
 printf("( %d", MST[i].second.first);
 printf(", %d )", MST[i].second.second);
 printf(": %d\n", MST[i].first);
 }
 printf("Minimum cost: %d\n", total);
 }
 int main()
 {
 int i, u, v, w;
 scanf("%d %d", &N, &E);
 reset();
 for(i=0; i<E; i++)
 {
 scanf("%d %d %d", &u, &v, &w);
 GRAPH.push_back(pair< int, edge >(w, edge(u, v)));
 }
 kruskal(); // runs kruskal and construct MST vector
 print(); // prints MST edges and weights
 return 0;
 }

