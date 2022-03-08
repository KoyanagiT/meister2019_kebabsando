package com.websarva.wings.andrroid.sudotest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.*
import androidx.appcompat.app.AppCompatActivity
import android.os.AsyncTask
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.*



class MainActivity : AppCompatActivity() {

    private var flag = false
    private var record: Record? = null

    //波
    private var visualizer: VisualizerSurfaceView? = null

    //private var mp0 : MediaPlayer?=null

    ///////////効果音のため/////////
    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
        .build()
    private val sound : SoundPool = SoundPool.Builder()
        .setAudioAttributes(audioAttributes)
        .setMaxStreams(1)
        .build()

    //効果音たち
    // 「不快です」という声
    private var seWarnigSpeling = 0
    // 高い音
    private var se1 = 0
    //武器を装備したような音
    private var se2 = 0
    //ピコ太郎のような音
    private var se3 = 0
    //「太閤検地」という声
    private var seAppName = 0
    ////////////////////////////////

    override fun onPause() {
        super.onPause()
        stopRecord()
    }

    ////////////////////ここからが処理内容///////////////////////////
    /////////////コメントアウトされた処理は無視/////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //スプラッシュ画面
        setContentView(R.layout.activity_main)

        //op
        //sound.play(seAppName, 1.0f, 1.0f, 0, 0, 1.0f)

        //不快なことを知らせるmusic

        //効果音たち
        // 「不快です」という声
        seWarnigSpeling = sound.load(this, R.raw.fukaidesu, 1)
        // 高い音
        se1 = sound.load(this, R.raw.se_maoudamashii_onepoint11, 1)
        //武器を装備したような音
        se2 = sound.load(this, R.raw.se_maoudamashii_se_syber07, 1)
        //ピコ太郎のような音
        se3 = sound.load(this, R.raw.se_maoudamashii_system13, 1)
        //「太閤検地」という声
        seAppName = sound.load(this, R.raw.taikoukenti, 1)
        ////////////////////////////////

        //波
        val surface = findViewById<SurfaceView>(R.id.surfaceView)
        visualizer = VisualizerSurfaceView(this, surface)

        //ボタンであるButtonオブジェクトを取得
        val recButton = findViewById<Button>(R.id.button3)
        //発狂オブジェクトを取得
        val help = findViewById<ImageButton>(R.id.imageButton)
        //リスナクラスのインスタンスを生成
        val listener = ClickListener()
        //ボタンにリスナを設定
        recButton.setOnClickListener(listener)
        help.setOnClickListener(listener)
    }

    //ボタンをクリックしたときのリスナクラス
    private inner class ClickListener : View.OnClickListener {
        override fun onClick(view: View) {

            val output = findViewById<Button>(R.id.button3)

            val am = this@MainActivity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val ringVol = am.getStreamVolume(AudioManager.STREAM_RING).toFloat()


            ////////////////画面遷移////////////////////
            ///////////////もし，イメージボタンが押されたら///////////
            if(view.id==R.id.imageButton) {
                /////////もし，録音してる途中だったら/////////
                if (flag) {
                    //音//
                    sound.play(se3, ringVol, ringVol, 0, 0, 1.0f)
                    //警告//
                    val text = getString(R.string.deng)
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(this@MainActivity, text, duration)
                    toast.show()
                } else {
                    ////////録音してなかったら//////////
                    //音//
                    sound.play(se1, ringVol, ringVol, 0, 0, 1.0f)
                    /////////画面遷移/////////
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
            //////////////////違うボタンだったら///////////////
            else {
                /////////////////flagの中身が，////////////////
                when (flag) {
                    ///falseの時//
                    //つまり，録音中の時////
                    false -> {
                        sound.play(se2, ringVol, ringVol, 0, 0, 1.0f)
                        /////flagの中身をtrueにする///
                        flag = true

                        output.text = getText(R.string.stop)

                        //録音開始//
                        doRecord()
                    }

                    ///trueの時//
                    //つまり，録音が停止していた時//
                    true -> {
                        sound.play(se2, ringVol, ringVol, 0, 0, 1.0f)
                        /////flagの中身をfalseにする///
                        flag = false

                        val level = findViewById<TextView>(R.id.textView)
                        level.text = "0"
                        val feeling = findViewById<TextView>(R.id.textView5)
                        feeling.text = ""
                        output.text = getText(R.string.start)

                        ///録音停止//
                        stopRecord()
                    }
                }
            }
        }
    }

    //録音停止//
    fun stopRecord(){
        record?.cancel(true)
    }

    //録音スタート//
    fun doRecord(){
        // AsyncTaskは使い捨て１回こっきりなので毎回作ります
        record = Record()
        record?.execute()
    }

    //録音するためのクラス(関数をひとまとめにした存在)//
    inner class Record : AsyncTask<Void, String, Void>() {

        private var message : String? = null

        // サンプリングレート。1秒あたりのサンプル数
        // （8000, 11025, 22050, 44100, エミュでは8kbじゃないとだめ？）
        private val sampleRate = 44100
        private val sec = 1
        private val buffer = ShortArray(sampleRate * (16 / 8) * 1 * sec)
        private var readSize = 0

        private var chFlag = 0


        /////////////不快ですmusicスレッド
        //音楽流すとそれを収音しちゃう→うるさい判定される
        //private var sendData: Thread? = null
        //private var running = AtomicBoolean(false)
        //private var treadChekcker = false

        //sendData
        //private var sendRecord : MediaRecorder?=null
        //sendData file name
        //private val = "appl"

        ////////////////////////////////////////
        /*fun recoding_send_data() {
            running.set(true)
            sendRecord = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(filename)
            }
            sendData = Thread {
                while (running.get()) {
                    try {

                    } catch (ex: InterruptedException) {

                        Thread.currentThread().interrupt()
                    }

                }
            }
            sendData?.start()
        }*/


        /////mainではない別のスレッド(上のUIに表示するonCreateとはべつのところでやってる)////
        ////スレッドの概念はよくわからない////
        override fun doInBackground(vararg params: Void): Void? {

            val baseValue = 12.0
            val borderValue = 65

            // 最低限のバッファサイズ
            val minBufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ) * 2

            // バッファサイズが取得できない。サンプリングレート等の設定を端末がサポートしていない可能性がある。
            if (minBufferSize < 0) {
                return null
            }

            //インスタンス
            val audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC, //音声ｓｏｕｒｃｅ　マクロフォン
                sampleRate, //上にある
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize
            )


            //録音開始//
            audioRecord.startRecording()

            ///エラーが起きるまで行う処理////
            try {
                while (flag) {
                    readSize = audioRecord.read(buffer, 0, minBufferSize)

                    if (readSize < 0) {
                        break
                    }
                    if (readSize == 0) {
                        continue
                    }

                    var maxValue : Short = 0
                    for (i in 0 until readSize) {
                        if(maxValue < buffer[i]){
                            maxValue = buffer[i]
                        }
                    }

                    val db = 20.0 * log10(maxValue / baseValue)

                    if(db > borderValue){
                        chFlag=1
                    }
                    else{
                        chFlag=0
                    }

                    message="%.1f".format(db)

                    //割り込み//
                    publishProgress(message)
                }

                //////エラーが起きたら，最後に行う処理////
            } finally {
                ///録音停止///
                audioRecord.stop()
                audioRecord.release()
            }

            return null
        }

        //割り込み内容//
        override fun onProgressUpdate(vararg values: String?){
            //Level view
            val level = findViewById<TextView>(R.id.textView)
            val feeling = findViewById<TextView>(R.id.textView5)

            level.text = message

            if(chFlag==1) {
                feeling.text = getText(R.string.bad)
            }
            else{
                feeling.text = getText(R.string.good)
            }

            ///波の表示//
            visualizer?.update(buffer, readSize)
        }
    }
}
