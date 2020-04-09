package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.Game
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.isFirstLogin
import kotlinx.android.synthetic.main.intro_fragment.*


class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.intro_fragment, container, false)
    }

    // TODO: validation of user name
    // TODO: prevent navigation if username is not correct

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------------------------- check if username was entered -----------------------------

        // temporary helper method to test gui
        username_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                var text = username_input.text
                if (text != null && text.length > 0) {
                    isFirstLogin = false
                }
            }
        })

        // ----------------------------- button change listeners -----------------------------

        startNewGame()

        offline_button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_boardFragment)
        )

        online_button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_boardFragment)
        )

        if (score_button != null) {
            score_button!!.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_scoreFragment)
            )
        }
    }

    fun startNewGame() {
        activeGame = Game()
        activeGame!!.start()
        activeGame!!.state = GameState.PREPARATION
    }

}
