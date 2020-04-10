package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.bridge_fragment.*
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
        //setList() // TODO: replace
        initializeGameList()
    }

    // TODO: add score counter to view (bot games and real games), maybe ranking as soon as we made the connection

    private fun initializeGameList() {
        val factory = InjectorUtils.provideGameViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory).get(GameViewModel::class.java)
        viewModel.getGames().observe(viewLifecycleOwner, Observer { games ->
            val finishedGames = games.filter { game -> game.data.state == GameState.ENDED }
            val itemsAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(
                    (activity as MainActivity),
                    android.R.layout.simple_list_item_1,
                    finishedGames.map{ game -> game.printScore()}
                )
            score_game_list.adapter = itemsAdapter
        })
    }

}
