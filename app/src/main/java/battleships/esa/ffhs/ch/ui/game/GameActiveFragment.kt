package battleships.esa.ffhs.ch.ui.game

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame
import battleships.esa.ffhs.ch.ui.viewmodel.BoardViewModel


class GameActiveFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.game_active_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGameFragments()
    }

    private fun initGameFragments() {
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_game_active) != null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_active, GameBoardMineFragment(), "mine").commit()
        }
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_game_inactive) != null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_inactive, GameBoardOpponentFragment(), "other")
                .commit()
        }
    }

    // will be accessed as well from child fragments
    fun switchFragments() {         // TODO: obsolete as fragment switch is not necessary anymore - switch of boardViewModel would be sufficient
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return
        }
        if (activeGame!!.isActivePlayerMe) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_active, GameBoardOpponentFragment(), "other")
                .commit()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_inactive, GameBoardMineFragment(), "mine")
                .commit()
            activeGame!!.isActivePlayerMe = false
        } else {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_active, GameBoardMineFragment(), "mine").commit()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_inactive, GameBoardOpponentFragment(), "other")
                .commit()
            activeGame!!.isActivePlayerMe = true
        }
    }
}
