package battleships.esa.ffhs.ch.refactored.game

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import javax.inject.Inject

class GameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val gameViewModel by viewModels<GameViewModel> { viewModelFactory }

    private val args: GameFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as BattleShipsApplication).appComponent.gameComponent()
            .create().inject(this)
    }



}
