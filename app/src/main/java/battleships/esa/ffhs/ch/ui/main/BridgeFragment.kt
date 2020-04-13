package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.gameListViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.GameListViewModel
import kotlinx.android.synthetic.main.bridge_fragment.*

class BridgeFragment : Fragment() {

    lateinit var itemsAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bridge_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeGameList()
    }

    // ----------------------------- list view for currently active games -----------------------------

    private fun initializeGameList() {
        gameListViewModel.getGames().observe(viewLifecycleOwner, Observer { games ->
            val ongoingGames = games.filter { game -> game.state != GameState.ENDED }
            itemsAdapter =
                ArrayAdapter<String>(
                    (activity as MainActivity),
                    android.R.layout.simple_list_item_1,
                    ongoingGames.map{ game -> gameListViewModel.printActive(game)}
                )
            bridge_game_list.adapter = itemsAdapter
        })

        bridge_game_list.setOnItemClickListener { parent, view, position, id ->

            val gameList = gameListViewModel.getGames().value?.filter{ game -> game.state != GameState.ENDED}
            val clickedGame = gameList?.get(position)
            if (clickedGame != null) {
                gameListViewModel.setGameActive(clickedGame)
                findNavController().navigate(R.id.action_mainFragment_to_boardFragment)
            }

        }
    }

}
