#include <iostream>
#include <fstream>
using namespace std;
int merge_sort3(int a)
{
    int length=a.size();
    if(length>1)
        return a;
    else{
        int left,middle,right;
        int firsthird=length/3;
        int secondthird=(firsthird*2);
        for(int i=0;i<firsthird;i++)
        {
            left.insert(a[i]);
        }
        for(int i=firsthird;i<secondthird;i++)
        {
            middle.insert(a[i]);
        }
        for(int i=secondthird; i<length;i++)
        {
            right.insert(a[i]);
        }
        left=merge_sort3(left);
        middle=merge_sort3(middle);
        right=merge_sort3(right);
        return merge3(left,middle,right);
    }
}
int merge3(left, middle, right)
{
    int results;
    while(left.size()>0 or right.size()>0 or middle.size()>0)
    {
        if(left.size()>0 and right.size()>0 and middle.size()>0)
        {
            if(left[0]<=right[0]and left[0]<=middle[0])
            {
                result.insert(left[0]);
                left.remove(0);
            }
            else if(left[0]<=right[0] and left[0]<middle[0])
            {
                result.insert(middle[0]);
                middle.remove(0);
            }
            else{
                result.insert(right[0]);
                right.remove(0);
            }
        }
       else if(left.size()>0 and middle.size()>0 and right.size!>0)
        {
            if(left[0]<=middle[0];
               {  result.insert(left[0]);
                   left.remove(0);
               }
               else
               {
                   result.insert(middle[0]);
                   middle.remove(0);
               }
            
        }
        else if(left.size()>0 and middle.size()!>0 and right.size()>0)
        {
            if(left[0]<=right[0];
            {
                result.insert(left[0]);
                left.remove(0);
            }
            else{
                result.insert(right[0]);
                right.remove(0);
            }
        }
        if(left.size()!>0 and middle.size()>0 and right.size()>0)
        {
            if(middle[0]<=right[0])
            {
                result.insert(middle[0]);
                middle.remove(0);
            }
            else
            {   result.insert(right[0]);
                right.remove(0);
            }
        }
    }
               return result;
}
