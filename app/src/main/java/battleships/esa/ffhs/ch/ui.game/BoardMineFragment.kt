package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import battleships.esa.ffhs.ch.MainActivity
import battleships.esa.ffhs.ch.MainViewModel
import battleships.esa.ffhs.ch.R
import kotlinx.android.synthetic.main.board_mine_fragment.*


class BoardMineFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.board_mine_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //populateGrid()
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    // somehow add cells to board
    private fun populateGrid() {
        val gridParent = myBoardGrid
        val mLocationsGrid = myBoardGrid
        val nColumns = 14
        val nCards: Int = 14
        val fragmentManager: FragmentManager = (activity as MainActivity).getSupportFragmentManager()
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        /*for (i in 0 until nCards) {
            fragmentTransaction.add(
                i,
                (activity as MainActivity).findViewById(R.id.cell)
            )
        }*/
        fragmentTransaction.commit()
    }


}