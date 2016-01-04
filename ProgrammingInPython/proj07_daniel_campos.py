#-------------------------------------------------------------------------------
# Name:Recommendation System
# Purpose:Give user information about movie preference recomend new movies
#
# Author: Daniel Campos
#
# Created: Monday Nov 17th, 2014
#-------------------------------------------------------------------------------
import math
import operator
class User:
    def __init__(self, username, movie_dictionary):
        self.username = username
        self.movie_dictionary = movie_dictionary
    def __str__(self):
        return str(self.username) + str(self.movie_dictionary)
    @property
    def name(self):
        return self.username
    def VectorLength(self):
        """Function to computer VectorLength"""
        sum_of_square = 0
        for i in self.movie_dictionary:
            x = self.movie_dictionary[i] * self.movie_dictionary[i]
            sum_of_square += x
        return math.sqrt(sum_of_square)
    def DotProduct(self, user2):
        """Compute dot product of two users"""
        dot = 0
        for i in self.movie_dictionary:
            x = self.movie_dictionary[i] * user2.movie_dictionary[i]
            dot += x
        return dot
    def CosineSimilarity(self, other):
        """Compute Cosine distance of two users"""
        return self.DotProduct(other)/(self.VectorLength()*other.VectorLength())
    def Recommendations(self, other):
        """Return movies that a second user recommends and the first hasnt seen"""
        unseen_movies = []
        for i in self.movie_dictionary:
            if self.movie_dictionary[i] == 0 and other.movie_dictionary[i] > 2:
                unseen_movies.append((other.movie_dictionary[i],i))
        return unseen_movies
def ReadMovieRatings(filename):
    """Read from a csv file and returns a map of usernames to maps of movies to ratings"""
    user_to_rating = {}
    usernames = []
    filmscores = []
    with open(filename, 'r') as f:
        first_line = f.readline().upper().rstrip().split(',')[1:]
        for i in first_line:
            usernames.append(i)
            filmscores.append({})
        for line in f:
            line = line.upper().rstrip().split(',')
            counter = 1
            for i in range(0,len(filmscores)):
                if line[counter] == '':
                    filmscores[i][line[0]]=0
                else:
                    filmscores[i][line[0]]=int(line[counter])
                counter+=1
    for i in range(0,len(usernames)):
        user_to_rating[usernames[i]]=filmscores[i]
    return user_to_rating
def FindRecommendations(users):
    """Find the most similair user and their recommendation"""
    for i in users:
        most_common = None
        closest_cosine_distance = 0
        for j in users:
            if i.CosineSimilarity(j) > closest_cosine_distance and i.name !=j.name:
                most_common = j
                closest_cosine_distance = i.CosineSimilarity(j)
        recommendations  = i.Recommendations(most_common)
        recommendations.sort(reverse=True)
        print(i.name + " <-> "  + most_common.name + " " + str("%.3G" % i.CosineSimilarity(most_common)) + " recommends: " + str(recommendations[:3]))
def Main():
    user_to_rating = ReadMovieRatings('Movie_Ratings.csv')
    users = []
    for i in user_to_rating:
        users.append(User(i,user_to_rating[i]))
    users.sort(key=lambda x: x.name)
    FindRecommendations(users)
if __name__ == '__main__':
    Main()