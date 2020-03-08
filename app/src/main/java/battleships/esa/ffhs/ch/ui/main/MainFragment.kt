package battleships.esa.ffhs.ch.ui.main

import android.graphics.Matrix
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.navigation.findNavController
import battleships.esa.ffhs.ch.MainActivity
import battleships.esa.ffhs.ch.MainViewModel
import battleships.esa.ffhs.ch.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*

// https://material.io/resources/icons/?icon=directions_boat&style=baseline

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // animate intro image with rotation, source: https://blog.kotlin-academy.com/android-animation-using-kotlin-ef385b5b5dea
        /*val animation = AnimationUtils.loadAnimation((activity as MainActivity), R.anim.rotate)
        sonarImg.startAnimation(animation)*/

        // add clicklistener for go-to-board-placeholder
        train.setOnClickListener {
            it.findNavController().navigate(R.id.boardFragment)
        }

        online.setOnClickListener {
            it.findNavController().navigate(R.id.boardFragment)
        }

        score.setOnClickListener {
            it.findNavController().navigate(R.id.scoreFragment)
        }

        // remove toolbar menu when returning to main fragment
        val toolbar = (activity as MainActivity).toolbar
        toolbar?.setNavigationIcon(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
