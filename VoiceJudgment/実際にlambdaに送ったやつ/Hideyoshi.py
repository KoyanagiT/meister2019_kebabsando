#coding:utf-8
from sklearn import svm
from sklearn.metrics import classification_report, accuracy_score
from testMaking import *
from learning import *
import csv
import numpy as np

def Hideyoshi(TEST_FILE):

    with open("feature_data/train_list.csv") as f:
        reader = csv.reader(f)
        train_data_NameAndNumber = [row for row in reader]
        #train_data_NameAndNumber
        #[n][0]=Name
        #[n][1]=label(intじゃない)
        #[n][2]=sample個数(intじゃない)

    #ファイル名だけ取得する
    #S3上のファイルをダウンロード
    receData = TEST_FILE
    test_data = np.empty((0,12),float)#テストデータ
    #test_dataに20通りほどノイズを与える
    #ファイルに保存せず，featureのlistをそのまま渡す
    test_data = testmaking(receData, test_data)
    
    #学習させる&出力
    #多分重い
    clf = svm.SVC(gamma="scale")

    evaluate(train_data_NameAndNumber,clf)

    test_label= clf.predict(test_data)

    #最頻値
    count = np.bincount(test_label)
    mode = np.argmax(count)

    if(max(count)/20>0.8):
        message = train_data_NameAndNumber[mode][0]
    else:
        message = "unknown data"
    
    return message

if __name__ == "__main__":
    file_path = input("file : ")
    res = Hideyoshi(file_path)
    print(res)
