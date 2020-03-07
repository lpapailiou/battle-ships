package battleships.esa.ffhs.ch.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import battleships.esa.ffhs.ch.MainActivity
import battleships.esa.ffhs.ch.R
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*

// https://material.io/resources/icons/?icon=directions_boat&style=baseline

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    val rootView = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // add clicklistener for go-to-board-placeholder
        goToBoard.setOnClickListener {
            it.findNavController().navigate(R.id.boardFragment)
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
