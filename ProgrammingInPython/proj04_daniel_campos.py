#-------------------------------------------------------------------------------
# Name:Baseball Statistics
# Purpose:Game to parse CSV and output useful baseball stats
#
# Author: Daniel Campos
#
# Created: Monday Oct 6th, 2014
#-------------------------------------------------------------------------------
def LoadPlayers(filename):
	playerstats = []
	with open(filename, 'r') as f:
		for line in f:
			line = line.upper().rstrip().split(',')
			hits = float(line[6])
			atbat = float(line[4])
			doubles = float(line[7])
			triples = float(line[8])
			homeruns = float(line[9])
			singles = hits - (doubles + triples + homeruns)
			totalbases = singles + 2 * doubles + 3 * triples + 4 * homeruns
			battingaverage = hits/atbat
			slugging = totalbases/atbat
			x = (line[0],line[1],line[2],hits,battingaverage,slugging)
			playerstats.append(x)
	return playerstats
def PrintHelp():
	print "The Commands you may run are as follows"
	print "QUIT"
	print "HELP"
	print "TEAM <identifier>"
	print "Report HITS"
	print "Report BATTING"
	print "Report SLUGGING"
def PrintTeam(teamname,playerstats):
	flag = 0
	sorted_list = sorted(playerstats, key=lambda playerstats: playerstats[0]) # sort by last name
	for i in sorted_list:
		if i[2]==teamname:
			flag = 1
			print  i[0] + "," + i[1] + "," + str(i[3]) + "," + str(i[4]) + "," + str(i[5])
	if flag == 0:
		print 'Invalid Team Name'
		ReadInput()
def PrintSlugging(playerstats):
	sorted_list = sorted(playerstats, key=lambda playerstats: playerstats[5])
	for i in range (1,11):
		print sorted_list[len(sorted_list)-i][0] + "," +  sorted_list[len(sorted_list)-1-i][1] + "," + str(sorted_list[len(sorted_list)-1-i][5])
def PrintHits(playerstats):
	sorted_list = sorted(playerstats, key=lambda playerstats: playerstats[3])
	for i in range (1,11):
		print sorted_list[len(sorted_list)-i][0] + "," + sorted_list[len(sorted_list)-1-i][1] + "," + str(sorted_list[len(sorted_list)-1-i][3])
def PrintBatting(playerstats):
	sorted_list = sorted(playerstats, key=lambda playerstats: playerstats[4])
	for i in range (1,11):
		print sorted_list[len(sorted_list)-i][0] + "," + sorted_list[len(sorted_list)-1-i][1] + "," + str(sorted_list[len(sorted_list)-1-i][4])
def ReadInput():
	response = raw_input('Please enter a command. Enter HELP to see a list of commands\n')
	response = response.upper().split(' ')
	if response[0] == 'QUIT':
		return (response[0])
	elif response[0] == 'HELP':
		return (response[0])
	else: 
		if len(response) == 2:
			temptuple = (response[0],response[1])
			if response[0] == 'REPORT' and response[1] == 'HITS':
				return(temptuple)
			elif response[0] == 'REPORT' and response[1] == 'BATTING':
				return(temptuple)
			elif response[0] == 'REPORT' and response[1] == 'SLUGGING':
				return(temptuple)
			elif response[0] == 'TEAM' and len(response[1]) == 3:
				return(temptuple) #Supposed to have team identifier
	print 'Invalid Command'
	return ReadInput()
def ProcessInput(response, playerstats):
	if response == 'QUIT':
		return 'Quit'
	elif response == 'HELP':
		PrintHelp()
	elif response == ('REPORT', 'HITS'):
		PrintHits(playerstats)
	elif response == ('REPORT', 'SLUGGING'):
		PrintSlugging(playerstats)
	elif response == ('REPORT', 'BATTING'):
		PrintBatting(playerstats)
	else:
		PrintTeam(response[1],playerstats)
def Main():
	playerstats = LoadPlayers('mlb.2013.csv')
	response = ReadInput()
	while 1:
		if ProcessInput(response,playerstats) == 'Quit':
			break
		response = ReadInput()
if __name__ == '__main__':
	Main()