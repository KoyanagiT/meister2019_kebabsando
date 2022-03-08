from pydub import AudioSegment

fifi = input("file : ")

start = input("time of start :")
end = input("time of end :")

sound = AudioSegment.from_file("{0}.wav".format(fifi), format="wav")

soundre = sound[:int(end)*1000]

soundre.export("{0}_{1}of_{2}.wav".format(start,end,fifi), format="wav")

