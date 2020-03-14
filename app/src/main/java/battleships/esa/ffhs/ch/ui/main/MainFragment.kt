package battleships.esa.ffhs.ch.ui.main

import MainViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import battleships.esa.ffhs.ch.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*


// https://material.io/resources/icons/?icon=directions_boat&style=baseline

class MainFragment : Fragment() {


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

        online_button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_boardFragment)
        )

        offline_button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_boardFragment)
        )

        score_button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_scoreFragment)
        )

        // remove toolbar menu when returning to main fragment
        val toolbar = (activity as MainActivity).toolbar
        toolbar?.setNavigationIcon(null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
}
