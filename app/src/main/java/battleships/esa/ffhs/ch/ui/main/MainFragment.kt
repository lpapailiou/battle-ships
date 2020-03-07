package battleships.esa.ffhs.ch.ui.main

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import battleships.esa.ffhs.ch.MainActivity
import battleships.esa.ffhs.ch.R
import kotlinx.android.synthetic.main.board_fragment.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

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
        println ("main fragment start")
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setNavigationIcon(null)    // not working, burger icon keeps coming back
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goToBoard.setOnClickListener {
            //Navigation.createNavigateOnClickListener(R.id.goToBoard, null)
            it.findNavController().navigate(R.id.boardFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    //fun goToBoard(view : View) {
        //val navController = Navigation.findNavController(((MainActivity)getActivity()), R.id.nav_host_fragment)
        //NavigationUI.setupActionBarWithNavController(this, navController, container)

    //}



}
