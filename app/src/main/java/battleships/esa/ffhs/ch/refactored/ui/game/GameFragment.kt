package battleships.esa.ffhs.ch.refactored.ui.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import battleships.esa.ffhs.ch.refactored.business.game.GameViewModel
import battleships.esa.ffhs.ch.refactored.ui.board.BoardView
import javax.inject.Inject

class GameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val gameViewModel by viewModels<GameViewModel> { viewModelFactory }

    private val args: GameFragmentArgs by navArgs()

    private lateinit var activeBoard: BoardView

    private lateinit var inactiveBoard: BoardView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as BattleShipsApplication).appComponent.gameComponent()
            .create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.game_fragment, container, false)
        activeBoard = view.findViewById(R.id.board_active)
        inactiveBoard = view.findViewById(R.id.board_inactive)

        gameViewModel.start(args.gameId, args.currentPlayerId, args.enemyPlayerId)
        gameViewModel.activeBoard.observe(viewLifecycleOwner, Observer { boardModel ->
//            activeBoard.setShips(boardModel.getShips())
        })
        gameViewModel.inactiveBoard.observe(viewLifecycleOwner, Observer { boardModel ->
//            inactiveBoard.setShips(boardModel.getShips())
        })

        return view
    }


}
