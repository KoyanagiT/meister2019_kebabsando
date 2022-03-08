動画ならば，movieChanger
mp3ならば，mp3Changer
に入れる

cuttingRiff
　作りたての音声はすべてカットする．
　5～8秒にカット
 使い方は，まずcuttingRiffフォルダの中に音声データを一つ入れて，cuttingRiff.pyを実行する．
 ファイル名は拡張子を入れる．
 切り取る間隔を秒数で指定する．
すると，outputフォルダに切り取り後のファイルがすべて入る

datamaker.py
　音声データの種類ごとに実行
　learning_sampleにデータを全て入れる．(ここで一気に行うはデータの種類ごと)
　donedoneフォルダにノイズの付与された
ファイルが完成

learning
　voice_data/train_list.csvにsample情報を書く
voiceフォルダにデータを入れていく