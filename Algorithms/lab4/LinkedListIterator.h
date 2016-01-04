#ifndef H_LinkedListIterator
#define H_LinkedListIterator
#include "Node.h"

template <class Type>
class linkedListIterator
{
public:
    linkedListIterator() {current = nullptr;}
    linkedListIterator(nodeType<Type> *ptr) {current = ptr;}
    Type operator *() {return current->info;}
    linkedListIterator<Type> operator++()
    {
        current = current->link;
        return *this;
    }
    bool operator==(const linkedListIterator<Type>& right ) const {return (current == right.current);}
    bool operator!=(const linkedListIterator<Type>& right ) const {return (current != right.current);}
private:
    nodeType<Type> *current;
};

#endif