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
import kotlin.collections.ArrayList
import kotlin.random.Random


class WordViewModel(context: Application) : AndroidViewModel(context) {
    // TODO: Implement the ViewModel

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var wordRepo: JSONArray
    private lateinit var wordsJson: String
    private val timer = Timer("Timer")

    private var _word = MutableLiveData<String>()
    private var _wordTranslation = MutableLiveData<String>()
    private var _fameTimer = MutableLiveData<Int>()
    private var _hintList = MutableLiveData<String>()
    private var _hintListData = ArrayList<String>()
    private var _score = MutableLiveData<Int>()
    private var _gameSize = MutableLiveData<Int>()
    private var _gameOver = MutableLiveData<Boolean>()
    private var _answerCorrect = MutableLiveData<Boolean>()

    val word: LiveData<String>
        get() = _word

    val wordTranslation: LiveData<String>
        get() = _wordTranslation

    val fameTimer: LiveData<Int>
        get() = _fameTimer

    val hintList: LiveData<String>
        get() = _hintList

    val score: LiveData<Int>
        get() = _score

    val gameOver: LiveData<Boolean>
        get() = _gameOver

    val answerCorrect: LiveData<Boolean>
        get() = _answerCorrect

    init {
        _score.value = 0
        _gameSize.value = 1
        _gameOver.value = false
        _answerCorrect.value = false
        fetchJson()
    }

    private fun fetchJson() {
        wordsJson = loadJSONFromAsset()
        wordRepo = JSONArray(wordsJson)
        getNewWord()
    }

    fun getNewWord() {
        if (wordsJson.isNotEmpty()) {
            val index = Random.nextInt(wordRepo.length())
            //_fameTimer.postValue(8)
            _word.postValue(wordRepo.getJSONObject(index).getString("text_eng"))
            _wordTranslation.postValue(wordRepo.getJSONObject(index).getString("text_spa"))
            _hintListData.add(wordRepo.getJSONObject(index).getString("text_spa"))
            addMoreHints()
            //timer()
            getNextHint()
        }
    }

    private fun addMoreHints() {
        for (item in 0..3) {
            _hintListData.add(
                wordRepo.getJSONObject(Random.nextInt(wordRepo.length())).getString("text_spa")
            )
            _hintListData.shuffle()
        }
        /*_hintList.postValue(_hintListData[0])
        _hintListData.removeAt(0)*/
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
                //getNewWord()
                if (_hintListData.size > 0) {
                    getNextHint()
                } else {
                    getNewWord()
                }

            }
        }

        val delay = 8000L
        timer.scheduleAtFixedRate(task, 0, delay)
    }

    fun getNextHint() {
        if (_hintListData.size > 0) {
            _hintList.postValue(_hintListData[0])
            _hintListData.removeAt(0)
            timerUpdater()
        } else {
            /*if (_gameSize.value == 5) {
                _gameOver.value = true
            } else {
                getNewWord()
            }*/
            _gameOver.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    private fun timerUpdater() {
        Handler(Looper.getMainLooper()).post {
            countDownTimer = object : CountDownTimer(8000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _fameTimer.postValue(Math.toIntExact(millisUntilFinished / 1000))
                }

                override fun onFinish() {
                    // _fameTimer.postValue(0)
                    getNextHint()
                }
            }.start()
        }

    }

    fun updateScore() {
        _score.value = _score.value?.plus(1)
    }

    fun cancelTimer() {
        countDownTimer.cancel()
    }

    fun updateGame(answer: Boolean) {
        _answerCorrect.value = answer
        _gameOver.value = true
    }

    fun resetParams() {
        //_score.value = 0
        _gameOver.value = false
        _answerCorrect.value = false
        _hintListData.clear()
        cancelTimer()
    }

}