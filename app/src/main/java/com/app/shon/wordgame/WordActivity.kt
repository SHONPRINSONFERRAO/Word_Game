package com.app.shon.wordgame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.shon.wordgame.ui.main.WordFragment

class WordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WordFragment.newInstance())
                .commitNow()
        }
    }
}