
 //You have a set of n integers each in the range 0 ... K. Partition these integers into two subsets such
 //that you minimize |S1 - S2|, where S1 and S2 denote the sums of the elements in each of the two subsets.
 #include <iostream>
 #include <vector>
 using namespace std;
 
 int maxsub(int a, int b)
 {
 if (a==1 || b==1)
 return 1;
 else
 return 0;
 }
 
 int main()
 {
 int n=7;
 int set[7]={2,4,6,8,12,15,4};
 int t=25;
 bool table[8][26];
 int s1 = 0;
 bool A[8][26];
 for(int i=0; i<=7; i++)
 for(int j=0; j<=25; j++)
 {
 A[i][j]=0;
 table[i][j]=0;
 }
 
 for(int s = 0; s<=t; s++)
 table[0][s]=false;
 for(int i = 0; i<=n; i++)
 table[i][0]=true;
 
 for(int i=2; i<=n; i++)
 for(int j=1; j<=t; j++)
 {
 if(table[i-1][j-set[i-1]] >= table[i-1][j])
 A[i][j] = 1;
 else
 A[i][j] = 0;
 
 if(j<set[i-1])
 table[i][j]=table[i-1][j];
 else
 table[i][j]=maxsub(table[i-1][j],table[i-1][j-set[i-1]]);
 }
 
 vector<int> selected;
 vector<int> S2;
 int ss=t;
 int ii=n-1;
 while (ss > 0 && ii > 0)
 {
 if (A[ii][ss]==1 && (ss-set[ii])>=0)
 {
 selected.push_back(set[ii]);
 ss=ss-set[ii];
 ii=ii-1;
 }
 else
 {
 ii=ii-1;
 }
 }
 
 cout << "Subset 1:\n";
 cout << "{";
 int sum1=0;
 for(int g=selected.size()-1; g>=0; g--)
 {
 sum1+=selected[g];
 if(g==0)
 cout << selected[g] << "}";
 else
 cout << selected[g] << ",";
 }
 cout << '\n' << '\n';
 
 vector<int> S2_temp = selected;
 for(int d=0; d<n; d++)
 {
 int match = false;
 for(int abc=0; abc<selected.size(); abc++)
 {
 if(S2_temp[abc] == set[d])
 {
 S2_temp[abc]=0;
 set[d]=0;
 match = true;
 }
 }
 
 if(match==false)
 {
 if(set[d]!=0)
 S2.push_back(set[d]);
 }
 }
 cout << "subset 2: \n";
 cout << "{";
 int sum2=0;
 for(int gh=0; gh<S2.size(); gh++)
 {
 sum2+=S2[gh];
 if(gh==S2.size()-1)
 cout << S2[gh] << "}";
 else
 cout << S2[gh] << ",";
 }
 cout << '\n'<< '\n';
 cout << "Absolute difference between S1 and S2 is ";
 if(sum1>sum2)
 cout << sum1-sum2;
 else if(sum2>sum1)
 cout << sum2-sum1;
 else
 return 0;
 cout << '\n';
 }
