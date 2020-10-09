import sys
import json
# from redis import StrictRedis
# from redis_cache import RedisCache

# client = StrictRedis(host="redis", decode_responses=True)
# cache = RedisCache(redis_client=client)

# @cache.cache()
def populate_dict(inputfile, word_dict):
    with open(inputfile, 'r') as f:
        while True:
            buf = f.read(10240)
            if not buf:
                break

            # make sure we end on a space (word boundary)
            while not str.isspace(buf[-1]):
                ch = f.read(1)
                if not ch:
                    break
                buf += ch

            words = buf.split()
            for word in words:
                # print(word)
                word_dict[word] = getWeight(word)
                # yield word
        # yield '' #handle the scene that the file is empty
    return word_dict


def getWeight(word):
    score = {"a": 1 , "b": 3 , "c": 3 , "d": 2 ,
         "e": 1 , "f": 4 , "g": 2 , "h": 4 ,
         "i": 1 , "j": 8 , "k": 5 , "l": 1 ,
         "m": 3 , "n": 1 , "o": 1 , "p": 3 ,
         "q": 10, "r": 1 , "s": 1 , "t": 1 ,
         "u": 1 , "v": 4 , "w": 4 , "x": 8 ,
         "y": 4 , "z": 10}
    wt = 0
    for i in word:
        i = i.lower()
        wt = wt + score[i]
    return wt


def powerSet(string , index , curr, wdlst): 
    if index == len(string): 
        wdlst.append(curr)
        return wdlst

    powerSet(string, index + 1, curr + string[index], wdlst) 
    powerSet(string, index + 1, curr, wdlst)  


def list_combos(iwd):
    # Algorithm to permute all combinations of letters
    combolst = []
    powerSet(iwd, 0, "", combolst)
    return combolst


if __name__ == "__main__":
    ipword = sys.argv[1]
    ipword = ipword.upper()
    inputfile = r"./wordlist.txt"
    word_dict = {}
    ret_jarray = []

    if len(word_dict)==0:
        word_dict = populate_dict(inputfile, word_dict)

    combos = list_combos(ipword)
        
    for word in combos:
        if word in word_dict:
            ret_jarray.append({'word': word, 'score': word_dict[word]})

    app_json = json.dumps(ret_jarray)
    print(app_json)