package ch.ffhs.esa.battleships.ui.bridge

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.business.bridge.BridgeViewModel
import ch.ffhs.esa.battleships.databinding.BridgeFragmentBinding
import kotlinx.android.synthetic.main.bridge_fragment.*
import javax.inject.Inject

class BridgeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val bridgeViewModel by viewModels<BridgeViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: BridgeFragmentBinding

    private lateinit var listAdapter: ActiveGamesListAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as BattleShipsApplication).appComponent.bridgeComponent()
            .create().inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = BridgeFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = bridgeViewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bridgeViewModel.start()



        play_vs_bot_button.setOnClickListener {
            navigateToGame()
        }

        play_vs_friend_button.setOnClickListener {
            navigateToGame()
        }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewModel
        listAdapter = ActiveGamesListAdapter(viewModel!!)
        viewDataBinding.bridgeGameList.adapter = listAdapter
    }


    private fun navigateToGame() {
        val action =
            BridgeFragmentDirections.actionMainFragmentToBoardPreparationFragment(OFFLINE_PLAYER_ID)
        findNavController().navigate(action)

    }

}
