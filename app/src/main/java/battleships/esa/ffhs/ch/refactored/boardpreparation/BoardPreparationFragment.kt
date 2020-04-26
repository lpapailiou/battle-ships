package battleships.esa.ffhs.ch.refactored.boardpreparation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.old.ui.drawable.BoardPainter
import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.board_preparation_fragment.*
import javax.inject.Inject

class BoardPreparationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val boardPreparationViewModel by viewModels<BoardPreparationViewModel> { viewModelFactory }

    private val args: BoardPreparationFragmentArgs by navArgs()

    lateinit var boardPainter: BoardPainter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as BattleShipsApplication).appComponent.boardPreparationComponent()
            .create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.board_preparation_fragment, container, false)

        boardPreparationViewModel.start(args.googlePlayerId)

        boardPainter = view.findViewById(R.id.preparation_board)
        boardPainter.setShips(boardPreparationViewModel.ships)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        startgame_button.setOnClickListener {
//            if (currentGame.getOpponentBoard().validateStart()) {
//                currentGame.setState(GameState.ACTIVE)
//                (parentFragment as GameFragment).switchToGameFragment()
//            } else {
//                showSnackBar(it)
//            }
        }
    }

    private fun showSnackBar(view: View) {
        val snackBar =
            Snackbar.make(view, "Some of your ships are still too close to each other!", 1000)
        snackBar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.colorComplementary))
        snackBar.show()
    }
}
