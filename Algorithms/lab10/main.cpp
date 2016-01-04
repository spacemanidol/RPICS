/***********************************************/
/*                                             */
/* Author Hilmi Yildirim, Graduate Student RPI */
/*                                             */
/***********************************************/
/*#include <cstdio>
#include <iostream>
#define inputfilename "graphIn3.txt"
#define outputfilename "graphOut.txt"
#define MAXB 10

int B;
int graph[MAXB*MAXB][MAXB*MAXB];
int digitList[3][MAXB*MAXB][MAXB*MAXB+1];
int counter = 0;

void put(int no, int px, int py)
{
    graph[px][py] = no;
    digitList[0][px][no] = 1;
    digitList[1][py][no] = 1;
    digitList[2][(px/3)*3+ py/3][no] = 1;
}

void delete2(int no, int px, int py)
{
    graph[px][py] = 0;
    digitList[0][px][no] = 0;
    digitList[1][py][no] = 0;
    digitList[2][(px/3)*3+ py/3][no] = 0;
    counter++;
}

int suitable(int no, int px, int py)
{
    return !digitList[0][px][no] && !digitList[1][py][no] && !digitList[2][(px/3)*3+ py/3][no];
}

void printGraph()
{
    int i,j;
    //FILE *f = fopen(outputfilename,"w");
    for(i=0; i<B*B; i++,printf("\n"))
        for(j=0; j<B*B; j++)
            printf("%d ",graph[i][j]);
    //fclose(f);
}

void readInitialGraph()
{
    int i,j;
    FILE *f = fopen(inputfilename, "r");
    std::cout << "reading" << std::endl;
    fscanf(f,"%d",&B);
    for(i=0; i<B*B; i++)
        for(j=0; j<B*B; j++){
            fscanf(f," %d",&graph[i][j]);
            if(graph[i][j])
                put(graph[i][j],i,j);
        }
    fclose(f);
    printGraph();
    printf("************************\n");
}


void find_sol(int si,int sj)
{
    int i;
    int x = si, y = sj;
    while(x<B*B){
        while(y<B*B){
            if(!graph[x][y])
            {
                for(i = 1;i<=B*B; i++)
                    if(suitable(i,x,y)){
                        put(i,x,y);
                        find_sol(x,y);
                        delete2(i,x,y);
                    }
                return;
            }
            y++;
        }
        x++;
        y=0;
    }
    if(x == B*B){
        printGraph();
        std::cout << std::endl;
        return;
    }
}
int main(){
    readInitialGraph();
    find_sol(0,0);
    std::cout << "number of back tracks "  << counter << "\n";    
}
*/
 #include <iostream>
 #include <algorithm>
 #include <vector>
 using namespace std;
 bool MONEY (int s ,int e ,int n ,int d ,int m ,int o ,int r ,int y ){
 int carry = 0;
 int temp = 0;
 int mod = 0;
 if( s == 0 or m == 0){
 return false;
 }
 //first y
 temp = d + e;
 mod = temp % 10;
 if(mod != y){
 return false;
 }
 while(temp > 9){
 temp -= 10;
 carry += 1;
 }
 //second e
 temp = n + r + carry;
 carry = 0;
 mod = temp % 10;
 if(mod != e){
 return false;
 }
 while(temp > 9){
 temp -= 10;
 carry += 1;
 }
 //third n
 temp = e + o + carry;
 carry = 0;
 mod = temp % 10;
 if(mod != n){
 return false;
 }
 while(temp > 9){
 temp -= 10;
 carry += 1;
 }
 //forth o
 temp = s + m + carry;
 carry = 0;
 mod = temp % 10;
 if(mod != o){
 return false;
 }
 while(temp > 9){
 temp -= 10;
 carry += 1;
 }
 //last m
 temp = carry;
 carry = 0;
 mod = temp % 10;
 if(mod != m){
 return false;
 }
 cout << "money" << endl;
 cout << s << e << n << d << endl;
 cout << m << o << r << e << endl;
 cout << m << o << n << e << y << endl;
 return true;
 }
 
 bool NOPE (int i ,int s ,int p ,int n ,int o ,int e ){
 int carry = 0;
 int temp = 0;
 int mod = 0;
 if( i == 0){
 return false;
 }
 //first e
 temp = p + p;
 mod = temp % 10;
 if(mod != e){
 return false;
 }
 while(temp > 9){
 temp -= 10;
 carry += 1;
 }
 //second p
 temp = s + n + carry;
 carry = 0;
 mod = temp % 10;
 if(mod != p){
 return false;
 }
 while(temp > 9){
 temp -= 10;
 carry += 1;
 }
 //third o
 temp = i + s + carry;
 carry = 0;
 mod = temp % 10;
 if(mod != o){
 return false;
 }
 while(temp > 9){
 temp -= 10;
 carry += 1;
 }
 //forth n
 temp = i + carry;
 carry = 0;
 mod = temp % 10;
 if(mod != n){
 return false;
 }
 while(temp > 9){
 temp -= 10;
 carry += 1;
 }
 
 cout << "nope" << endl;
 cout << i << s << p << endl;
 cout << i << s << n << p << endl;
 cout << n << o << p << e << endl;
 return true;
 }
 int main () {
 
 int myints[] = {0,1,2,3,4,5,6,7,8,9};
 cout << "The 10! possible permutations with 8 elements:\n";
 sort (myints,myints+10);
 do {
 MONEY(myints[0], myints[1], myints[2], myints[3], myints[4], myints[5], myints[6], myints[7]);
 int s=myints[0];
 int e=myints[1];
 int n=myints[2];
 int d=myints[3];
 int m=myints[4];
 int o=myints[5];
 int r=myints[6];
 int y=myints[7];
 int send=(s*1000)+(e*100)+(n*10)+d;
 int more=(m*1000)+(o*100)+(r*10)+e;
 int money=(m*10000)+(o*1000)+(n*100)+(e*10)+y;
 if((send+more==money&&s!=0&&m==1))
 {
 cout<<"ANSWER******"<<endl;
 cout<<"send="<<send<<endl;
 cout<<"more="<<more<<endl;
 cout<<"money="<<money<<endl;
 break;
 }
 } while ( next_permutation (myints,myints+10) );
 cout << "The 10! possible permutations with  elements:\n";
 sort (myints,myints+10);
 do {
 NOPE(myints[0], myints[1], myints[2], myints[3], myints[4], myints[5]);
 int i=myints[0];
 int s=myints[1];
 int p=myints[2];
 int n=myints[3];
 int o=myints[4];
 int e=myints[5];
 int isp=(i*100)+(s*10)+p;
 int isnp=(i*1000)+(s*100)+(n*10)+p;
 int nope=(n*1000)+(o*100)+(p*10)+e;
 //cout<<"s"<<s<<"e"<<e<<"n"<<n<<"d"<<"     "<<send<<endl;
 if((isp+isnp==nope&&i!=0&&n!=1))
 {
 cout<<"ANSWER******"<<endl;
 cout<<"isp="<<isp<<endl;
 cout<<"isnp="<<isnp<<endl;
 cout<<"nope"<<nope<<endl;
 break;
 }
 } while ( next_permutation (myints,myints+10) );
 return 0;
 
 
 }