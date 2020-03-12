package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import battleships.esa.ffhs.ch.R


class BoardMineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.board_mine_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    // somehow add cells to board
    private fun populateGrid() {
        val nColumns = 14
        val nCards: Int = 14
        val fragmentManager: FragmentManager =
            (activity as MainActivity).getSupportFragmentManager()
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
