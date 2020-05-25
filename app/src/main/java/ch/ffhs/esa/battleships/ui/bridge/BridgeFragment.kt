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
import androidx.navigation.fragment.navArgs
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.business.bridge.BridgeViewModel
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import ch.ffhs.esa.battleships.databinding.BridgeFragmentBinding
import kotlinx.android.synthetic.main.bridge_fragment.*
import javax.inject.Inject

class BridgeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val bridgeViewModel by viewModels<BridgeViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: BridgeFragmentBinding

    private lateinit var listAdapter: ActiveGamesListAdapter

    private val args: BridgeFragmentArgs by navArgs()


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

        bridgeViewModel.start(args.uid)

        play_vs_bot_button.setOnClickListener {
            startNewGame()
        }

        if (args.uid == OFFLINE_PLAYER_ID) {
            play_vs_friend_button.visibility = View.GONE

            sign_up_to_play_online_button.setOnClickListener {
                findNavController().navigate(BridgeFragmentDirections.actionMainFragmentToSignupFragment())
            }
            return
        }

        sign_up_to_play_online_button.visibility = View.GONE

        play_vs_friend_button.setOnClickListener {
            startNewGame()
        }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewModel
        listAdapter = ActiveGamesListAdapter(viewModel!!) { game: GameWithPlayerInfo ->
            resumeGame(game)
        }
        viewDataBinding.bridgeGameList.adapter = listAdapter
    }


    private fun startNewGame() {
        val action =
            BridgeFragmentDirections.actionMainFragmentToBoardPreparationFragment(
                args.uid
            )
        findNavController().navigate(action)
    }

    private fun resumeGame(game: GameWithPlayerInfo) {
        val enemyPlayerUID =
            if (game.attackerUID == args.uid) game.defenderUID else game.attackerUID

        val action =
            BridgeFragmentDirections.actionMainFragmentToGameFragment(
                game.gameId,
                args.uid,
                enemyPlayerUID
            )
        findNavController().navigate(action)
    }

}
