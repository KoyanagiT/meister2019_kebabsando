import os
from MFFC import *
import glob

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
            feature = get_feature(fileName,nfft,nceps)#file_name,nfft,nceps)
            if len(test_data) == 0:
                test_data=feature
            else:
                test_data=np.vstack((test_data,feature))
    return test_data

if __name__ == "__main__":
    test_data=[]
    print(testmaking("test.wav",test_data))
