import shutil
import glob
import ffmpeg
import pydub
from pydub import AudioSegment

def making(kindOfData,dataNumber):
    #音声データの種類
    sampleFolder = "./donedone"#input("absolute path of sample folder : ")

    #noiseがあるフォルダの場所(変更しなくてよい)
    noiseFolder = "./noises"#input("absolute path of noise folder : ")
    noiseFolder = noiseFolder+"/*"

    noiseList = glob.glob(noiseFolder)

    print("saccececce!!!!!!\n")
    
    for noise in noiseList:
        #voluはノイズの音量
        for volu in range(3):
            fileName = "{0}\{1}_{2}_{3}.wav".format(sampleFolder,dataNumber,noiseList.index(noise),volu)
            shutil.copy(kindOfData,fileName)

            #noise付与
            result_sound = AudioSegment.from_file(fileName)
            time = result_sound.duration_seconds
            start_time_ms = (time/3)*1000
            nois = AudioSegment.from_file(noise)  # noise読み
            if volu!=0:
                nois = nois-volu*30
                result_sound = result_sound.overlay(nois, start_time_ms)
            result_sound.export(fileName,format="wav")
            print(fileName,"is done")
