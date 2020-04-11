package battleships.esa.ffhs.ch.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.game.GameFragment.Companion.currentGame
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.game_preparation_fragment.*


class GamePreparationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.game_preparation_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // triggered when game is supposed to start. will change the state of the game and navigate to the actual game fragment
        startgame_button.setOnClickListener {
            if (currentGame.getOpponentBoard().value!!.validateStart()) {
                currentGame.setState(GameState.ACTIVE)
                (parentFragment as GameFragment).switchToGameFragment()
            } else {
                showSnackBar(it)
            }
        }
    }

    private fun showSnackBar(view: View) {
        val snackbar = Snackbar.make(view, "board must be in valid state", 1000)
        snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.colorComplementary))
        snackbar.show()
    }

}
