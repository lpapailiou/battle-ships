package ch.ffhs.esa.battleships.ui.score

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.WINNING_SCORE_MULTIPLIER
import ch.ffhs.esa.battleships.business.game.GameState
import ch.ffhs.esa.battleships.business.game.GameViewModel
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import ch.ffhs.esa.battleships.ui.main.MainActivity
import kotlinx.android.synthetic.main.score_fragment.*
import javax.inject.Inject

class ScoreFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val gameViewModel by viewModels<GameViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.score_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObserver()
    }

    // TODO: add score counter to view (bot games and real games), maybe ranking as soon as we made the connection


    private fun initializeObserver() {


    }
//        GameViewModel.observe(viewLifecycleOwner, Observer { games ->
//            val finishedGames = games.filter { game -> game.state == GameState.ENDED }
//            val itemsAdapter: ArrayAdapter<String> =
//                ArrayAdapter<String>(
//                    (activity as MainActivity),
//                    android.R.layout.simple_list_item_1,
//                    finishedGames.map { game -> gameViewModel.printScore(game) }
//                )
//            score_game_list.adapter = itemsAdapter
//
//            val scoreMulti = finishedGames.filter { game -> game.opponentName != "Bot" }.map { game -> game.result * WINNING_SCORE_MULTIPLIER }.sum()
//            val scoreBot = finishedGames.filter { game -> game.opponentName == "Bot" }.map { game -> game.result * WINNING_SCORE_MULTIPLIER }.sum()
//            score_points_multiplayer.setText(scoreMulti.toString())
//            score_points_bot.setText(scoreBot.toString())
//
//
//     }

}
