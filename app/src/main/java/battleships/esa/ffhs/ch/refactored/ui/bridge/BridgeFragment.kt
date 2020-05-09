package battleships.esa.ffhs.ch.refactored.ui.bridge

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
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import battleships.esa.ffhs.ch.refactored.business.bridge.BridgeViewModel
import kotlinx.android.synthetic.main.bridge_fragment.*
import javax.inject.Inject

class BridgeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val bridgeViewModel by viewModels<BridgeViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as BattleShipsApplication).appComponent.bridgeComponent()
            .create().inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bridge_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bridgeViewModel.start()

        bridgeViewModel.activeGames.observe(viewLifecycleOwner, Observer {

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                it.map { game -> game.lastChangedAt.toString() }
            )
            bridge_game_list.adapter = adapter
        })
    }

    // ----------------------------- list view for currently active games -----------------------------

    private fun initializeGameList() {
//        mainViewModel.getGames().observe(viewLifecycleOwner, Observer { games ->
//            val ongoingGames = games.filter { game -> game.state != GameState.ENDED }
//            itemsAdapter =
//                ArrayAdapter<String>(
//                    (activity as MainActivity),
//                    android.R.layout.simple_list_item_1,
//                    ongoingGames.map{ game -> mainViewModel.printActive(game)}
//                )
//            bridge_game_list.adapter = itemsAdapter
//        })

        bridge_game_list.setOnItemClickListener { parent, view, position, id ->

//            val gameList =
//                mainViewModel.getGames().value?.filter { game -> game.state != GameState.ENDED }
//            val clickedGame = gameList?.get(position)
//            if (clickedGame != null) {
//                mainViewModel.setGameActive(clickedGame)
//                findNavController().navigate(R.id.action_mainFragment_to_boardFragment)
//            }

        }
    }

}
