package ch.ffhs.esa.battleships.old.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.ffhs.esa.battleships.R

class ScoreFragment : Fragment() {

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
//        mainViewModel.getGames().observe(viewLifecycleOwner, Observer { games ->
//            val finishedGames = games.filter { game -> game.state == GameState.ENDED }
//            val itemsAdapter: ArrayAdapter<String> =
//                ArrayAdapter<String>(
//                    (activity as MainActivity),
//                    android.R.layout.simple_list_item_1,
//                    finishedGames.map { game -> mainViewModel.printScore(game) }
//                )
//            score_game_list.adapter = itemsAdapter

//            val scoreMulti = finishedGames.filter { game -> game.opponentName != "Bot" }.map { game -> game.result * WINNING_SCORE_MULTIPLIER }.sum()
//            val scoreBot = finishedGames.filter { game -> game.opponentName == "Bot" }.map { game -> game.result * WINNING_SCORE_MULTIPLIER }.sum()
//            score_points_multiplayer.setText(scoreMulti.toString())
//            score_points_bot.setText(scoreBot.toString())


    }

}
