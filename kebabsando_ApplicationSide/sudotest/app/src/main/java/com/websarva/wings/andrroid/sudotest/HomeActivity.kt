package com.websarva.wings.andrroid.sudotest

import android.annotation.SuppressLint
import android.media.*
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast




class HomeActivity : AppCompatActivity() {

    private var flag = false
    private var flagOfLearn = false
    private var recorderAI: RecorderAI? = null

    ///////////効果音のため/////////*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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

        //ボタンであるButtonオブジェクトを取得
        val recButton = findViewById<Button>(R.id.bts)
        //リスナクラスのインスタンスを生成
        val listener = ClickListener2()
        //ボタンにリスナを設定
        recButton.setOnClickListener(listener)
    }

    //ボタンをクリックしたときのリスナクラス
    private inner class ClickListener2 : View.OnClickListener {
        override fun onClick(view: View) {

            val output = findViewById<Button>(R.id.bts)
            val viewing = findViewById<TextView>(R.id.answerView)

            when (flag) {
                //メッセージの表示
                false -> {
                    sound.play(se3, 1.0f, 1.0f, 0, 0, 1.0f)
                    flag = true
                    output.text = getText(R.string.stop)
                    viewing.text = getString(R.string.learning)
                    doRecord()
                }

                true -> {
                    sound.play(se3, 1.0f, 1.0f, 0, 0, 1.0f)
                    flag = false
                    output.text = getText(R.string.start)
                    viewing.text = getText(R.string.startMessage)
                    stopRecord()
                }
            }
        }
    }

    fun stopRecord(){
        recorderAI?.cancel(true)
        if(!flagOfLearn) {
            val text = getString(R.string.learnFail)
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(this@HomeActivity, text, duration)
            toast.show()
        }
    }

    fun doRecord(){
        // AsyncTaskは使い捨て１回こっきりなので毎回作ります
        recorderAI = RecorderAI()
        recorderAI?.execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class RecorderAI : AsyncTask<Void, Void, Void>() {

        // サンプリングレート。1秒あたりのサンプル数
        // （8000, 11025, 22050, 44100, エミュでは8kbじゃないとだめ？）
        private val sampleRate = 44100
        private val sec = 1
        private val buffer = ShortArray(sampleRate * (16 / 8) * 1 * sec)
        private var readSize = 0

        override fun doInBackground(vararg params: Void): Void? {

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

            audioRecord.startRecording()

            try {
                //スタートのフラグと学修完了のフラグ
                while (flag&&(!flagOfLearn)) {
                    readSize = audioRecord.read(buffer, 0, minBufferSize)

                    if (readSize < 0) {
                        break
                    }
                    if (readSize == 0) {
                        continue
                    }

                    //学修
                    //学修完了でflagOfLearn =ｔｒｕｅ
                    flagOfLearn=true

                }
            } finally {
                publishProgress()
                //ファイル書き込み
                //if(flagOfLearn) -> ファイルに書き込み
                audioRecord.stop()
                audioRecord.release()
            }

            return null
        }

        override fun onProgressUpdate(vararg values: Void?){
            if(flagOfLearn) {
                flag = false
                val viewing = findViewById<TextView>(R.id.answerView)
                val output = findViewById<Button>(R.id.bts)
                viewing.text = getString(R.string.learnEnd)
                output.text = getText(R.string.start)
            }
        }
    }
}
