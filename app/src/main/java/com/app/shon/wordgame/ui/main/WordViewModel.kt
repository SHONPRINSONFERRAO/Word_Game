package com.app.shon.wordgame.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONArray
import java.io.IOException
import java.nio.charset.StandardCharsets
import kotlin.random.Random

class WordViewModel(context: Application) : AndroidViewModel(context) {
    // TODO: Implement the ViewModel

    private lateinit var wordRepo: JSONArray
    private lateinit var wordsJson: String
    private var _word = MutableLiveData<String>()
    private var _wordTranslation = MutableLiveData<String>()

    val word: LiveData<String>
        get() = _word

    val wordTranslation: LiveData<String>
        get() = _wordTranslation

    init {
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
            _word.value = wordRepo.getJSONObject(index).getString("text_eng")
            _wordTranslation.value = wordRepo.getJSONObject(index).getString("text_spa")
            //view.wordText.text = word.getString("text_eng").toString()
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

}