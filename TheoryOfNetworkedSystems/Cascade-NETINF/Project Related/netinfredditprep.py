from functools import cmp_to_key
import string
class Post:
    def __init__(self, image_id, unixtime, rawtime, reddit_id, subreddit, username):
        self.unixtime = unixtime
        self.rawtime = rawtime
        self.image_id = image_id
        self.reddit_id = reddit_id
        self.subreddit = subreddit
        self.username = username
    def __str__(self):
        t = "unixtime:" + self.unixtime + " rawtime:" + self.rawtime + " image_id:" + self.image_id + " reddit_id:" 
        return  t + self.reddit_id + " subreddit:" + self.subreddit + " username:" + self.username
def StarndardizeTime(time):
    if '+' in time:
        time = time.split('+')
        timezone = time[1]
        time = time[0]
        if timezone == '00:00':
            return time
        timezone= timezone.split(":")
        time = time.split(":")
        hour = time[0]
        minute = time[1]
        second = time[2]
    elif '-' in time:
        time = time.spit('-')
        timezone = time[1]
        time = time[0]
        if timezone == '00:00':
            return time
        timezone= timezone.split(":")
        time = time.split(":")
        hour = time[0]
        minute = time[1]
        second = time[2]
def SortViaTime(time1,time2):
    return false
def SortDate(date1, date2):
    date1 = date1.split('-')
    date2 = date2.split('-')
    year1 = int(date1[0])
    month1 = int(date1[1])
    day1 = int(date1[2])
    year2 = int(date2[0])
    month2 = int(date2[1])
    day2 = int(date2[2])
    if year2 == year1:
        if month2 == month1:
            return day1 < day2
        return month1 < month2
    return year1 < year2
def SortViaRawTime(time1, time2):
    date1 = time1.split('T')[0]
    date2 = time2.split('T')[0]
    time1 = time1.split('T')[1]
    time2 = time2.split('T')[1]
    if date1 == date2:
        return SortViaTime(time1,time2)
    else:
        return SortDate(date1,date2)
def LoadFile(filename):
    posts = []
    counter = 0
    with open(filename, 'r') as f:
        for line in f:
            line = line.split(',')
            if len(line) >= 13:
                if line[12].strip():
                    counter+=1
                    posts.append(Post(line[0], line[1], line[2], line[5], line[7], line[12]))
    print("Posts: " + str(counter))
    return posts
def CreateCascades(posts):
    cascades = {}
    for j in range(0, len(posts)):
        i = posts[j]
        t = []
        if i.image_id in cascades.keys():
            t = cascades[i.image_id]
        t.append(i) 
        cascades[i.image_id] = t
    return cascades
def StarndardizeTime(time):
    if '+' in time:
        time = time.split('+')
        timezone = time[1]
        time = time[0]
        if timezone == '00:00':
            return time
        timezone= timezone.split(":")
        time = time.split(":")
        hours = time[0]
        minutes = time[1]
        seconds = time[2]
        hoursplus = timezone[0]
        minutesplus = timezone[1]
        hours = int(hours) + int(hoursplus)
        minutes = int(minutes) + int(minutesplus)
    elif '-' in time:
        time = time.split('-')
        timezone = time[1]
        time = time[0]
        if timezone == '00:00':
            return time
        timezone= timezone.split(":")
        time = time.split(":")
        hours = time[0]
        minutes = time[1]
        seconds = time[2]
        hoursplus = timezone[0]
        minutesplus = timezone[1]
        hours = int(hours) - int(hoursplus)
        minutes = int(minutes) - int(minutesplus)
    return str(hours) + ":" + str(minutes) + ":" + str(seconds)
def SortViaTime(time1,time2):
    time1 = time1.split(":")
    time2 = time2.split(":")
    if time1[0] == time2[0]:
        if time1[1] == time2[1]:
            if time1[2] == time2[2]:
                return 0
            return time1[2] < time2[2]
        elif time1[1] < time2[1]:
            return -1
        elif time1[1] > time2[1]:
            return 1
    elif time1[0] < time2[0]:
        return -1
    elif time1[0] > time2[0]:
        return 1
def SortDate(date1, date2):
    date1 = date1.split('-')
    date2 = date2.split('-')
    year1 = int(date1[0])
    month1 = int(date1[1])
    day1 = int(date1[2])
    year2 = int(date2[0])
    month2 = int(date2[1])
    day2 = int(date2[2])
    if year2 == year1:
        if month2 == month1:
            if day1 > day2:
                return 1
            elif day1 < day2:
                return -1
            else:
                return 0
        elif month1 < month2:
            return -1
        elif month1 > month2:
            return 1
    elif year1 < year2:
        return -1
    elif year1 > year2:
        return 1
def SortViaRawTime(ts,ts1):
    time1 = ts.rawtime
    time2 = ts1.rawtime
    date1 = time1.split('T')[0]
    date2 = time2.split('T')[0]
    time1 = time1.split('T')[1]
    time2 = time2.split('T')[1]
    if date1 == date2:
        time1 = StarndardizeTime(time1)
        time2 = StarndardizeTime(time2)
        return SortViaTime(time1,time2)
    else:
        return SortDate(date1,date2)
def SortCascades(cascades):
    newcascades = {}
    for key in cascades:
        chain = cascades[key]
        newchain = sorted(chain, key=cmp_to_key(SortViaRawTime))
        newcascades[key] = newchain
    return newcascades
def PrintCascades(cascades, filename):
    for j in range(0,20):
        count = 0 
        total = 0
        newcascades = {}
        username_to_id = {}
        for key in cascades:
            chain = cascades[key]
            if len(chain) > j:
                newcascades[key] = chain
        for key in newcascades:
            chain = newcascades[key]
            for i in chain:
                username_to_id[i.username.rstrip('\n')] = count
                count += 1
        count = 0 
        with open(str(j)+filename, 'w') as f:
            for key in username_to_id:
                f.write(str(username_to_id[key]) + "," + str(username_to_id[key])+ "\n")
            f.write("\n")
            for key in newcascades:
                chain = newcascades[key]
                s = ""
                total += len(chain)
                count +=1
                for i in chain:
                    s = str(username_to_id[i.username.rstrip('\n')]) + "," + str(i.unixtime) + "," + s
                f.write(s[:-1] + "\n")
        print(str(total/count))
if __name__ == '__main__':
    posts = LoadFile('reddit.csv')
    cascades = CreateCascades(posts)
    sorted_cascades = SortCascades(cascades)
    PrintCascades(sorted_cascades, 'cascades.txt')
