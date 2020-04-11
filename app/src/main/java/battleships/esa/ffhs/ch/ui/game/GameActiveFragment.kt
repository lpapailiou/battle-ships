package battleships.esa.ffhs.ch.ui.game

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.drawable.CustomDialog
import battleships.esa.ffhs.ch.ui.game.GameFragment.Companion.currentGame
import battleships.esa.ffhs.ch.ui.main.MainActivity


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
        if (currentGame.isMyTurn().value!!) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_active, GameBoardOpponentFragment(), "other")
                .commit()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_inactive, GameBoardMineFragment(), "mine")
                .commit()
            currentGame.setMyTurn(false)
        } else {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_active, GameBoardMineFragment(), "mine").commit()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_inactive, GameBoardOpponentFragment(), "other")
                .commit()
            currentGame.setMyTurn(true)
        }
    }

}
