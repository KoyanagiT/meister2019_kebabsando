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
    noiseFolder = noiseFolder+"/*"

    noiseList = glob.glob(noiseFolder)
    
    for noise in noiseList:
        #voluはノイズの音量
        for volu in range(2):
            shutil.copy(fileName,"sample.wav")

            #noise付与
            result_sound = AudioSegment.from_file("sample.wav")
            start_time_ms = 200
            nois = AudioSegment.from_file(noise)  # noise読み
            nois = nois-volu*5
            if volu!=0:
                result_sound = result_sound.overlay(nois, start_time_ms)
            result_sound.export("sample.wav",format="wav")
            #データ抽出
            file_name = "sample.wav"
            feature = get_feature(file_name,nfft,nceps)
            if len(test_data) == 0:
                test_data=feature
            else:
                test_data=np.vstack((test_data,feature))
            #sample削除
            os.remove('sample.wav')
            #print("sample.wav with <{0}>is done".format(noise))
    return test_data
