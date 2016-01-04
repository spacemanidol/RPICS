#-------------------------------------------------------------------------------
# Name: Amaze your friends
# Purpose: This is a simple parlor trick attributed to Albert Einstein, but is probably much older. 
#
# Author: Daniel Campos
# RCS: campod2
# RIN: 660996361
# Created: Sep 8th, 2014
#------------------------------------------------------------------------------
def checknumber(magicnumber):
	if len(magicnumber) == 3:
		if int(magicnumber[0])-1 > int(magicnumber[2]) or  int(magicnumber[0])+1 < int(magicnumber[2]):
			return 1
	return 0
def reverse_int(n):
    return int(str(n)[::-1])
def magicnumber():
	print("I will do magic. Rember the number 1089")
	number = raw_input("Enter a 3 digit number where the first and last digit differ by at least 2\n")
	if number[0] =="-":
		number = number[1:] #remove a negative sign
	if checknumber(number) == 0:
		print("the number you entered doesnt meet the criterea")
		return
	else:
		number = int(number)
		reversenumber = reverse_int(number)
		if number > reversenumber:
			newnumber = number-reversenumber
		else:
			newnumber = reversenumber-number
		newnumberreverse = reverse_int(newnumber)
		sumofreverses = newnumberreverse+newnumber
		print number," and ", reversenumber
		print newnumber
		print newnumberreverse
		print sumofreverses
		print "The sum is 1089 as predicted!OMG"
if __name__ == '__main__':
	magicnumber()