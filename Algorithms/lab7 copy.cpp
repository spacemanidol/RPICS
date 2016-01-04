#include <iostream>
#include <string>
#include <vector>
#include <queue>

/*
 *  HuffNode.h
 *  Huffman
 *
 *  Created by David Rappaport on 22/02/07.
 *  Copied from Mark Nelson's Priority queues and the STL article at URL
 * http://www.dogma.net/markn/articles/pq_stl/priority.htm
 *
 * Simplified further by Moorthy 10/18/09
 *
 * Edited by Dan Ibanez 10/25/10
 *
 * Input: std::cin
 * Format: character, space, frequency(uint)
 *         on each line
 *         (use another character if the character is a space)
 *
 */

class HuffNode {
public:
    HuffNode(unsigned w):
    weight(w) {}
    virtual ~HuffNode() {}
    virtual void traverse(std::string& code, unsigned& totalCost) {std::cout << "Base traverse\n";}
    unsigned GetWeight() const {return weight;}
private:
    unsigned weight;
};

class HuffLeaf : public HuffNode {
public:
    HuffLeaf(char n_c, unsigned n_f):
    c(n_c),
    HuffNode(n_f) {}
    virtual ~HuffLeaf() {}
    void traverse(std::string& code, unsigned& totalCost)
    {
        std::cout << c << ": " << code << std::endl;
        totalCost += GetWeight()*code.size();
    }
private:
    char c;
};

class HuffInternal : public HuffNode {
public:
    HuffInternal(HuffNode* l, HuffNode* r):
    HuffNode((l->GetWeight()) + (r->GetWeight())),
    left(l), right(r) {}
    virtual ~HuffInternal() {}
    void traverse(std::string& code, unsigned& totalCost)
    {
        code.push_back('0');
        left->traverse(code, totalCost);
        code.resize(code.size()-1);
        code.push_back('1');
        right->traverse(code, totalCost);
        code.resize(code.size()-1);
    }
private:
    HuffNode *left, *right;
};

//
// binary predicate for HuffNode pointers
//
// The comparison operator used to order the
// priority queue.
//

class greater_weight
{
public:
    bool operator()(const HuffNode* a, const HuffNode* b) const {
        return (a->GetWeight() > b->GetWeight());
    }
};

//
// The traverse member function is used to
// print out the code for a given node.  It
// is designed to print the entire tree if
// called for the root node.
//
// This function also keeps track of the total
// cost of the tree using the reference variable
// totalCost
//


int main (int argc, char * const argv[]) {
    
    std::priority_queue< HuffNode*, std::vector<HuffNode*>, greater_weight > q;
    std::vector<HuffNode*> tree;
    unsigned numSym = 0;
    unsigned freq = 0;
    char ch = '\0';
    
    // Initialize Huffman tree.
    // Step 1. Make HuffNodes from std::cin, put them into the tree and priority queue
    
    while(std::cin >> ch >> freq)
    {
        tree.push_back(new HuffLeaf(ch, freq));
        q.push(tree.back());
        numSym += freq;
    }
    
    //
    // This loop removes the two smallest nodes from the
    // queue.  It creates a new internal node that has
    // those two nodes as children. The new internal node
    // is then inserted into the priority queue.  When there
    // is only one node in the priority queue, the tree
    // is complete.
    //
    
    while ( q.size() > 1 ) {
        HuffNode *left = q.top();
        q.pop();
        HuffNode *right = q.top();
        q.pop();
        tree.push_back(new HuffInternal( left, right ) );
        q.push(tree.back());
    }
    //
    // Now print the results
    //
    std::cout << "Char  Symbol   Code" << std::endl;
    HuffNode *hNode = q.top();
    unsigned totalCost = 0;
    std::string code;
    hNode->traverse(code, totalCost);
    // Average number of bits per character
    std::cout << "Total Cost " << totalCost << ".  Average bits per symbol " << (float(totalCost)/float(numSym)) << std::endl;
    
    //And delete allocated heap memory
    std::vector<HuffNode*>::iterator it = tree.begin();
    std::vector<HuffNode*>::iterator end = tree.end();
    for (; it != end; ++it) {delete *it;}
    
    return 0;
}
