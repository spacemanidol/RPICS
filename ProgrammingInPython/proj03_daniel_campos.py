#-------------------------------------------------------------------------------
# Name: Anagrams
# Purpose: given a list of words produce counts of anamgram and print out the longest anagram
#
# Author: Daniel Campos
#
# Created: Sep 22nd, 2014
#-------------------------------------------------------------------------------
import collections
def LoadDictionary(filename):
	anagram = {}
	with open(filename, 'r') as f:
		for line in f:
			line = line.strip()
			letters = str(sorted(list(line)))
			if letters in anagram.keys():
				anagram[letters] = str(line + "," + anagram[letters])
			else:
				anagram[letters] = line
	result = Anagrams(anagram)
	print "anagram count: " + result.amount
	print "longest anagram list: " + str(result.words)
def Anagrams(dictionary):
	anagramcount = 0
	longestanagram = 0
	for key in dictionary:
		current = dictionary[key].count(',') #amount of commas indicates amount of anagrams-1
		if current > 0:
			anagramcount += 1
			if current > longestanagram:
				longestanagram = current
				longestkey = key
	result = collections.namedtuple('result',['amount','words'])
	p = result(amount=str(anagramcount),words=dictionary[longestkey].split(','))
	return p
if __name__ == '__main__':
	LoadDictionary('word.txt')