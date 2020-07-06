package com.app.shon.wordgame.ui.main

import android.app.Application
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONArray
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.random.Random


class WordViewModel(context: Application) : AndroidViewModel(context) {
    // TODO: Implement the ViewModel

    private lateinit var wordRepo: JSONArray
    private lateinit var wordsJson: String
    private val timer = Timer("Timer")

    private var _word = MutableLiveData<String>()
    private var _wordTranslation = MutableLiveData<String>()
    private var _fameTimer = MutableLiveData<Int>()

    val word: LiveData<String>
        get() = _word

    val wordTranslation: LiveData<String>
        get() = _wordTranslation

    val fameTimer: LiveData<Int>
        get() = _fameTimer

    init {
        fetchJson()
        timer()
    }

    private fun fetchJson() {
        wordsJson = loadJSONFromAsset()
        wordRepo = JSONArray(wordsJson)
        getNewWord()
    }

    fun getNewWord() {
        if (wordsJson.isNotEmpty()) {
            val index = Random.nextInt(wordRepo.length())
            _fameTimer.postValue(8)
            _word.postValue(wordRepo.getJSONObject(index).getString("text_eng"))
            _wordTranslation.postValue(wordRepo.getJSONObject(index).getString("text_spa"))
            //view.wordText.text = word.getString("text_eng").toString()
            timerUpdater()
        }
    }

    private fun loadJSONFromAsset(): String {
        var json: String? = null
        json = try {
            val `is` = getApplication<Application>().assets.open("words_v2.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }

    private fun timer() {
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                getNewWord()
            }
        }

        val delay = 8000L
        timer.scheduleAtFixedRate(task, 0, delay)
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    private fun timerUpdater() {
        Handler(Looper.getMainLooper()).post {
            object : CountDownTimer(8000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _fameTimer.postValue(Math.toIntExact(millisUntilFinished / 1000))
                    println("Time " + millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    // _fameTimer.postValue(0)
                }
            }.start()
        }
    }

}