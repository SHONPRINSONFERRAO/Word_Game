package com.app.shon.wordgame.ui.quickresults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.app.shon.wordgame.R
import kotlinx.android.synthetic.main.fragment_fail_quiz.view.*

class FailQuizFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fail_quiz, container, false)

        view.endGameBtn.setOnClickListener { sendResults(true) }
        view.continueBtn.setOnClickListener { sendResults(false) }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    setFragmentResult(
                        "event", bundleOf(
                            "backPress" to true
                        )
                    )
                    findNavController().popBackStack()
                }
            })


        return view
    }

    private fun sendResults(status: Boolean) {
        setFragmentResult(
            "result", bundleOf(
                "endGame" to status
            )
        )
        findNavController().popBackStack()
    }


}