#include <iostream>
#include <fstream>
using namespace std;

/*T(n)= T(n-1) +1, T(0)=0
 Q(n) = Q(n-1) + n , Q(0) =0
 R(n) = 2*R(n-1)+1, R(0)=0
 X(n) = 2*X(n/2)+1, X(0) =0
 Y(N) = 2*Y(n/2)+n, Y(0) =0
 Z(n) = Z(sqrt(n)) + 1, Z(1)= 0*/

int T (int n)
{
	if ( n <= 0 )
		return 0;
    
	return 1 + T(n-1);
}

int Q (int n)
{
	if ( n <= 0 )
		return 0;
    
	return n + Q(n-1);
}

int R (int n)
{
	if ( n <= 0 )
		return 0;
    
	return 1 + 2 * R(n-1);
}

int X (int n)
{
	if ( n <= 0 )
		return 0;
    
	return 1 + 2* X(n/2);
}

int Y (int n)
{
	if ( n <= 0 )
		return 0;
    
	return n + 2* Y(n/2);
}

int Z (int n)
{
	int count = 0;
	int i = 1;
	while ( i * i < n )
	{
		count ++;
		i *= 2;
	}
	return count;
}

int main ()
{
    
	ofstream results;
	results.open ("results.txt");
	int n;
    
	for (n = 10; n <=1000; n=n+10)
		results << n<<","<< T(n) <<","<< Q(n) << "," << R(n) << "," << X(n) <<"," << Y(n) <<","<< Z(n) << "\n";
	results.close();
	return 0;
}


