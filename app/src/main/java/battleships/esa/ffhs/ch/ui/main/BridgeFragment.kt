package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.InjectorUtils.provideGameViewModelFactory
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.bridge_fragment.*

class BridgeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bridge_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setList() // TODO: replace
        initializeGameList()
    }

    private fun initializeGameList() {
        val factory = provideGameViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory).get(GameViewModel::class.java)
        viewModel.getGames().observe(viewLifecycleOwner, Observer { games ->
            val ongoingGames = games.filter { game -> game.data.state != GameState.ENDED }
            val itemsAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(
                    (activity as MainActivity),
                    android.R.layout.simple_list_item_1,
                    ongoingGames.map{ game -> game.printActive()}
                )
            bridge_game_list.adapter = itemsAdapter
        })
    }

    // ----------------------------- list view for currently active games -----------------------------

    // temporary handler for static gui testing
    fun setList() {
        val itemsAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                (activity as MainActivity),
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.staticgamedata)
            )
        bridge_game_list.adapter = itemsAdapter
    }

}
