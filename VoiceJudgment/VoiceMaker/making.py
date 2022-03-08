import shutil
import glob
import ffmpeg
import pydub
from pydub import AudioSegment

def making(kindOfData,dataNumber):
    #音声データの種類
    sampleFolder = "C:\\Users\\koyanagi\\Desktop\\VoiceMaker\\donedone"#input("absolute path of sample folder : ")

    #noiseがあるフォルダの場所(変更しなくてよい)
    noiseFolder = "C:\\Users\\koyanagi\\Desktop\\noises"#input("absolute path of noise folder : ")
    noiseFolder = noiseFolder+"/*"

    noiseList = glob.glob(noiseFolder)

    print("saccececce!!!!!!\n\n")
    
    for noise in noiseList:
        #voluはノイズの音量
        for volu in range(3):
            fileName = "{0}\{1}_{2}_{3}.wav".format(sampleFolder,dataNumber,noiseList.index(noise),volu)
            shutil.copy(kindOfData,fileName)

            #noise付与
            file = AudioSegment.from_file(fileName)
            start_time_ms = 2000
            nois = AudioSegment.from_file(noise)  # noise読み
            nois = nois+volu*2
            result_sound = file.overlay(nois, start_time_ms)
            result_sound.export(fileName,format="wav")
            print(fileName,"is done")
