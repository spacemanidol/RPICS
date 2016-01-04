#define MODULUS 127
#include <iostream>
#include <string>

int rksearch ( std::string pattern, std::string text);
int hash (std::string str);
int hashn(std::string str, int n);
int hash_increment( std::string str, int prevIndex, int prevHash, int keyLength );

int rksearch ( std::string pattern, std::string text)
{
	int pattern_hash, text_hash, pattern_len, num_iterations, i;
    
	if (pattern.empty() || text.empty())
		return -1;
    
	pattern_len = pattern.size();
	num_iterations = text.size() - pattern_len + 1;
	pattern_hash = hash(pattern);
	text_hash = hashn(text, pattern_len);
	
	for ( i = 0; i < num_iterations; i++ )
	{
		if ( pattern_hash == text_hash )
		{
			bool match = true;
			for ( int j = 0; j < pattern.size(); j ++ )
			{
				if ( pattern[j] != text[i+j] )
					match = false;
			}
			if ( match )
				return i;
		}
        
		text_hash = hash_increment(text, i, text_hash, pattern_len);
	}
	return -1;
}

int hash (std::string str)
{
	int sum = 0;
	int k;
	int i = 0;
	k = str.length();
	while ( i < k )
	{
		sum = (sum + (int)(str[i]));
		i++;
	}
	return sum % MODULUS;
}

int hashn(std::string str, int n)
{
	int sum;
	std::string str1;
	str1 = str.substr(0, n);
	sum = hash(str1);
	std::cout << sum << '\n';
	return sum;
}

int hash_increment( std::string str, int prevIndex, int prevHash, int keyLength )
{
	int val = (prevHash - ((int) str[prevIndex])
               + ((int) str[prevIndex + keyLength])) % MODULUS;
    
	if ( val < 0 )
		return val + MODULUS;
	else
		return val;
}

int main()
{
	std::string text;
	std::string pattern;
	std::cout << "Enter Text String." << '\n';
	std::cin >> text;
	std::cout << "Enter Pattern String." << '\n';
	std::cin >> pattern;
	std::cout << "Text String \n" << text << '\n';
	std::cout << "Pattern String \n" << pattern << '\n';
    
	std::cout << "Patter is found in position " << rksearch (pattern, text);
	return 0;
}