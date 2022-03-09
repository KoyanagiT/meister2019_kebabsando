# meister2019_kebabsando

meister2019で，kebabsandoチームで作った作品

不快な音や大きい音を検知して知らせるアプリ，
「TAIKOUKENCHI」．

予定では，収音した音声データをサーバに送り，不快かどうかを判定するはずだったが，音声データをサーバに送ることができなかった．だが，アプリ側とサーバ側のプログラムはそれぞれ動かすことができる．

VoiceJudgment内のものと，アプリのUI以外を担当．

Sklearnは調べるとサンプルプログラムが出てくる．

# DEMO

googleDriveに動画を上げています．

# Requirement

* scikit-learn
* numpy
* csv
* ffmpeg

# Usage

* アプリ

アプリを起動し，スタートボタンを押す．
* Pythonの方

data作りセット内のlearning.pyを実行し，その後，Hideyoshi.pyを実行するとテストできる．
このプロジェクトは欠陥が多く，その一つに，ある音声が必ず何かしらの不快な音として判定されてしまう点がある．
これは，不快ではない音というラベルを設けなかったためである．
