package com.app.shon.wordgame.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.shon.wordgame.R
import kotlinx.android.synthetic.main.main_fragment.view.*
import org.json.JSONArray
import java.io.IOException
import java.nio.charset.StandardCharsets
import kotlin.random.Random


class WordFragment : Fragment() {

    companion object {
        fun newInstance() = WordFragment()
    }

    private lateinit var animBounce: Animation
    private lateinit var animMove: Animation
    private lateinit var viewModel: WordViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        val viewModelFactory = WordViewModelFactory(application)

        viewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(WordViewModel::class.java)

        val animIn: Animation = AlphaAnimation(0.0f, 1.0f)
        animIn.duration = 3000

        val animOut: Animation = AlphaAnimation(1.0f, 0.0f)
        animOut.duration = 3000

        val anim = AnimationSet(true)
        anim.addAnimation(animOut)
        animIn.startOffset = 3000
        anim.addAnimation(animIn)

        animMove = AnimationUtils.loadAnimation(requireContext(),
            R.anim.annim)

        animBounce = AnimationUtils.loadAnimation(requireContext(),
            R.anim.bounce)

        view.wordText.setOnClickListener { viewModel.getNewWord() }

        viewModel.fameTimer.observe(viewLifecycleOwner, Observer {
            view.timerTxtView.text = it.toString()
            view.timerTxtView.startAnimation(animBounce)
        })

        viewModel.word.observe(viewLifecycleOwner, Observer {
            view.wordText.text = it.toString()
            view.wordHintText.startAnimation(animMove)
        })


        return view
    }


}