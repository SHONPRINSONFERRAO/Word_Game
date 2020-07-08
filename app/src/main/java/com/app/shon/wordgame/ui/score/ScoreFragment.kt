package com.app.shon.wordgame.ui.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.shon.wordgame.R
import kotlinx.android.synthetic.main.fragment_score.view.*


class ScoreFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_score, container, false)
        val notes = ScoreFragmentArgs.fromBundle(requireArguments()).score
        view.scoreText.text = notes.toString()

        view.quit.setOnClickListener { requireActivity().finish() }

        view.restart.setOnClickListener { findNavController().navigate(ScoreFragmentDirections.actionScoreToWelcome()) }

        return view
    }


}