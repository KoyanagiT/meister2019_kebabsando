#coding:utf-8
from sklearn.metrics import classification_report, accuracy_score
import sys
from MFFC import *
import glob
import csv
import random
import numpy as np

def evaluate(train_data_NameAndNumber,clf):
    train_label = []
    with open("feature_data/_data_sets.csv") as f,open("feature_data/_data_sets_label.csv") as d:
        reader = csv.reader(f)
        train_data = [row for row in reader]
        reader = csv.reader(d)
        train_label = [int(row[0]) for row in reader]
    with open("feature_data/train_list.csv") as f:
        reader = csv.reader(f)
        train_data_NameAndNumber = [row for row in reader]
    
    #識別機学習
    clf.fit(train_data,train_label)


def makeDataSets(sampleName, sampleKindNumber,samLabel):
    train_label = []
    train_data = np.empty((0,12),float)#トレーニングデータ

    noise_nums = [1,2,3,4,5,6,7,8,9,10]#ノイズ番号
    level_nums = [0,1,2]#ノイズの音量
    random.shuffle(noise_nums)

    nfft = 2048  # FFTのサンプル数
    nceps = 12   # MFCCの次元数


    #学修
    for noise_num in noise_nums[0:10]:
        random.shuffle(level_nums)
        #学習用データを作成
        for level_num in level_nums[0:2]:
            files_name = glob.glob("voice_data/%s/%d_%d_%d.wav" % (sampleName,sampleKindNumber,noise_num,level_num))
            for file_name in files_name:
                feature = get_feature(file_name,nfft,nceps)
                if len(train_data) == 0:
                    train_data=feature
                else:
                    train_data=np.vstack((train_data,feature))
                train_label.append(samLabel)

    #特徴データをテキストに出力
    #feature_train_data=np.hstack((samLabel.reshape(len(train_label),1),train_data))
    #feature_test_data=np.hstack((test_label.reshape(len(test_label),1),test_data))
    with open("feature_data/{0}.csv".format(sampleName),'a+') as f, open("feature_data/_data_sets.csv".format(dataframe[0]),"a+") as d:
        writer=csv.writer(f,lineterminator='\n')
        writerDataSets=csv.writer(d,lineterminator='\n')
        writer.writerows(train_data)
        writerDataSets.writerows(train_data)
    with open("feature_data/_data_sets_label.csv".format(dataframe[0]),"a+") as l:
        writerlabel=csv.writer(l,lineterminator='\n')
        writerlabel.writerows(train_label)

    print(sampleName,"done")


if __name__=="__main__":
    #データセットを全部作る(本番用では消す)
    #本番用は元からデータセットは作っておく
    #train_data(学修用データ)の一覧の取得
    #sample番号sample名が参照できるようなcsvを用意する(用意してある)
    #追加機能をandroidで実装してから追記できるようにする
    with open("voice_data/train_list.csv") as f:
        reader = csv.reader(f)
        train_data_NameAndNumber = [row for row in reader]
        #train_data_NameAndNumber
        #[n][0]=Name
        #[n][1]=label(intじゃない)
        #[n][2]=sample個数(intじゃない)

    #data sets
    for dataframe in train_data_NameAndNumber:
        for oneData in range(int(dataframe[2])-1):#sample個数回
            #makeDataSets(sampleName, sampleNumber, labelNumber)
            makeDataSets(dataframe[0], oneData, dataframe[1])
            print(dataframe[0],"-", oneData,"is done")
    
