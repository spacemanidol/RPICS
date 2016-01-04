#-------------------------------------------------------------------------------
# Name:Currency Conversion
# Purpose:A program that accesses google finance to do curency calculation
#
# Author: Daniel Campos
#
# Created: Monday Nov 3rd, 2014
#-------------------------------------------------------------------------------
import urllib.request
import re
class Currency:
    def __init__(self, currency_code, amount = 0.00):
        self.currency_code = currency_code
        self.amount = amount
    def __str__(self):
        return str(self.amount) + "," + self.currency_code
    def __add__(self,x):
        t = Currency(self.currency_code,self.amount)
        if t.currency_code == x.currency_code:
            t.amount += x.amount
        else:
            t.amount += x.convert(t.currency_code).amount
        return t
    def __gt__(self,x):
        if self.currency_code == x.currency_code:
            return self.amount > x.amount
        else:
            return self.amount > x.convert(self.currency_code)
    def convert(self,currency_code):
        try:
            query = 'https://www.google.com/finance/converter?a=' + str(self.amount) + '&from=' + self.currency_code + '&to=' + currency_code
            web_obj = urllib.request.urlopen(query)
            result = re.findall(r'<span class=bld>(.*?)</span>', str(web_obj.read()), re.DOTALL)
            result = float(result[0].split(" ")[0])
            web_obj.close()
            return Currency(currency_code, result)
        except:
            print("something went wrong, adding standard 0 USD")
            web_obj.close()
            return Currency('USD', 0)
def validNumber(string):
    if float(string) and float(string) > 0:
        return True
    else: 
        print("Invalid amount")
        return False
def validCurrencyCode(string):
    if string == 'USD':
        return True
    elif string == 'EUR':
        return True
    elif string == 'SEK':
        return True
    elif string == 'CAD':
        return True
    elif string == 'CNY':
        return True
    elif string == 'GBP':
        return True
    else:
        print("Invalid Currency Code")
        return False
def Main():
    total = Currency('USD')
    test = Currency('USD',1000)
    while test > total:
        print(total)
        answer = input("Please input an amount to add and its currency (E.G. 100 USD)").upper().split()
        if len(answer) == 2:
            if validCurrencyCode(answer[1]) and validNumber(answer[0]):
                C1 = Currency(answer[1], float(answer[0]))
                total += C1
    print(total)
    print("We have passed 1000 USDS")
if __name__ == '__main__':
    Main()