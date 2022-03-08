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
                try:
                    feature = get_feature(file_name,nfft,nceps)
                    if len(train_data) == 0:
                        train_data=feature
                    else:
                        train_data=np.vstack((train_data,feature))
                    train_label.append(samLabel)
                except:
                    pass

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


