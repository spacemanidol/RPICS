#ifndef H_UnorderedLinkedList
#define H_UnorderedLinkedList
#include "LinkedListType.h"

template <class Type>
class unorderedLinkedList: public linkedListType<Type>
{
public:
    bool search(const Type& searchItem) const
    {
        nodeType<Type> *current;
        bool found = false;
        current = first;
        
        while (current != nullptr && !found)
        {
            if (current->info == searchItem)
                found = true;
            else
                current = current->link;
        }
        return found;
    }
    void insertFirst(const Type& newItem)
    {
        nodeType<Type> *newNode;
        newNode = new nodeType<Type>;
        
        newNode->info = newItem;
        newNode->link = first;
        first = newNode;
        
        count ++;
        if ( last == nullptr )
            last = newNode;
    }
    void insertLast(const Type& newItem)
    {
        nodeType<Type> *newNode;
        
        newNode = new nodeType<Type>;
        newNode->info = newItem;
        newNode->link = nullptr;
        
        if (first == nullptr)
        {
            first = last = newNode;
            count++;
        }
        else
        {
            last->link = newNode;
            last = newNode;
            
            count++;
        }
    }
    void deleteNode(const Type& deleteItem)
    {
        nodeType<Type> *current;
        nodeType<Type> *prev;
        bool found;
        
        if (first == nullptr)
            cout << "Cannot delete from empty list\n";
        
        else
        {
            if (first->info == deleteItem)
            {
                current = first;
                first = first->link;
                count--;
                
                if (first == nullptr)
                    last = nullptr;
                
                delete current;
            }
            else
            {
                found = false;
                prev = first;
                
                current = first->link;
                
                while (current!= nullptr && !found)
                {
                    if (current->info != deleteItem)
                    {
                        prev = current;
                        current = current->link;
                    }
                    else
                        found = true;
                }
                if (found)
                {
                    prev->link = current->link;
                    count--;
                    if ( last == current)
                        last = prev;
                    
                    delete current;
                }
                else
                    cout<< "The item to be delete is not in the list.\n";
            }
        }
    }
    int pre;
    int post;
    
};

#endif