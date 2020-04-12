package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.utils.InjectorUtils
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.model.WON_GAME_VALUE
import battleships.esa.ffhs.ch.ui.viewmodel.GameListViewModel
import kotlinx.android.synthetic.main.score_fragment.*

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
        val factory = InjectorUtils.provideGameViewModelFactory()
        val viewModel = ViewModelProvider(this, factory).get(GameListViewModel::class.java)
        viewModel.getGames().observe(viewLifecycleOwner, Observer { games ->
            val finishedGames = games.filter { game -> game.getState().value == GameState.ENDED }
            val itemsAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(
                    (activity as MainActivity),
                    android.R.layout.simple_list_item_1,
                    finishedGames.map{ game -> game.printScore()}
                )
            score_game_list.adapter = itemsAdapter

            val scoreMulti = finishedGames.filter { game -> game.getOpponentName() != "Bot" }.map { game -> game.getResult().value!! * WON_GAME_VALUE }.sum()
            val scoreBot = finishedGames.filter { game -> game.getOpponentName() == "Bot" }.map { game -> game.getResult().value!! * WON_GAME_VALUE }.sum()
            score_points_multiplayer.setText(scoreMulti.toString())
            score_points_bot.setText(scoreBot.toString())
        })

    }

}
