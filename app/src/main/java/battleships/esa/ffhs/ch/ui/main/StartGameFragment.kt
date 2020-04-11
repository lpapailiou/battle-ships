package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.GameInstance
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.model.Game
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.isFirstLogin
import battleships.esa.ffhs.ch.ui.viewmodel.GameViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.bridge_fragment.*
import kotlinx.android.synthetic.main.intro_fragment.*
import kotlinx.android.synthetic.main.start_game_fragment.*


class StartGameFragment : Fragment() {

    lateinit var factory: ViewModelFactory
    lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        factory = InjectorUtils.provideGameViewModelFactory()
        viewModel = ViewModelProviders.of(this, factory).get(GameViewModel::class.java)
        return inflater.inflate(R.layout.start_game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------------------------- button change listeners -----------------------------

        offline_button.setOnClickListener {
            val currentGame = viewModel.getActiveGame()
            if (currentGame != null) {
                currentGame.setActive(false)
            }
            findNavController().navigate(R.id.action_mainFragment_to_boardFragment)
        }

        online_button.setOnClickListener {
            val currentGame = viewModel.getActiveGame()
            if (currentGame != null) {
                currentGame.setActive(false)
            }
            findNavController().navigate(R.id.action_mainFragment_to_boardFragment)
        }

    }

}
