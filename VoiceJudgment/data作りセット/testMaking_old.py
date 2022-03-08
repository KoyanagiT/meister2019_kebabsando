import os
from MFFC import *
import shutil
import glob
import pydub
from pydub import AudioSegment

def testmaking(fileName,test_data):
    nfft = 2048  # FFTのサンプル数
    nceps = 12   # MFCCの次元数
    
    #noiseがあるフォルダの場所(変更しなくてよい)
    noiseFolder = "./VoiceMaker/noises"#input("absolute path of noise folder : ")
    noiseFolder = noiseFolder+"/*"

    noiseList = glob.glob(noiseFolder)
    
    for noise in noiseList:
        #voluはノイズの音量
        for volu in range(2):
            shutil.copy(fileName,"sample.wav")

            #noise付与
            result_sound = AudioSegment.from_file("sample.wav")
            time = result_sound.duration_seconds
            start_time_ms = (time/3)*1000
            nois = AudioSegment.from_file(noise)  # noise読み
            nois = nois-volu*25
            if volu!=0:
                result_sound = result_sound.overlay(nois, start_time_ms)
            result_sound.export("sample.wav",format="wav")
            #データ抽出
            file_name = "sample.wav"
            feature = get_feature(fileName,nfft,nceps)#file_name,nfft,nceps)
            if len(test_data) == 0:
                test_data=feature
            else:
                test_data=np.vstack((test_data,feature))
            #sample削除
            os.remove('sample.wav')
            #print("sample.wav with <{0}>is done".format(noise))
    return test_data

if __name__ == "__main__":
    test_data=[]
    print(testmaking("test.wav",test_data))
