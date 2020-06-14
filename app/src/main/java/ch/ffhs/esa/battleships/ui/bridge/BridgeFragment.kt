package ch.ffhs.esa.battleships.ui.bridge

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.business.bridge.BridgeViewModel
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import ch.ffhs.esa.battleships.databinding.BridgeFragmentBinding
import ch.ffhs.esa.battleships.ui.main.MainActivity
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navEnemyId
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navGameId
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navIsBotGame
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navOwnPlayerId
import ch.ffhs.esa.battleships.ui.main.MainFragmentDirections
import ch.ffhs.esa.battleships.ui.main.NotificationUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
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
        bridgeViewModel.start(navOwnPlayerId)

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val isLoggedIn = auth.currentUser != null

        play_vs_bot_button.setOnClickListener {
            startNewGame(true)
        }

        if (!isLoggedIn || !(activity as MainActivity).hasWifi()) {
            play_vs_friend_button.visibility = View.GONE

            sign_up_to_play_online_button.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToAuthHostFragment())
            }
            return
        }

        sign_up_to_play_online_button.visibility = View.GONE

        play_vs_friend_button.setOnClickListener {
            startNewGame(false)
        }

    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewModel
        listAdapter = ActiveGamesListAdapter(viewModel!!) { game: GameWithPlayerInfo ->
            navGameId.value = game.gameUid
            if (game.attackerUid == null) {
                showSnackBar("Still no enemies in sight. Those damn stealth ships!")
                return@ActiveGamesListAdapter
            } else if (!isItMyTurn(game.attackerUid, game.attackerName ?: "", game.playerAtTurnName ?: "") && !isItMyTurn(game.defenderUid, game.defenderName , game.playerAtTurnName ?: "")) {
                showSnackBar("Yer enemy is still plotting. Stupid land rat!")
                return@ActiveGamesListAdapter
            }
            resumeGame(game)
        }
        viewDataBinding.bridgeGameList.adapter = listAdapter
    }

    private fun isItMyTurn(playerId: String, playerName: String, nextTurn: String): Boolean {
        if ((navOwnPlayerId.equals(playerId) || OFFLINE_PLAYER_ID.equals(playerId)) && playerName.equals(nextTurn)) {
            return true
        }
        return false
    }

    private fun startNewGame(isBotGame: Boolean) {
        navIsBotGame = isBotGame
        navGameId.value = null
        val action =
             MainFragmentDirections.actionMainFragmentToGameHostFragment()
        if (isBotGame) {
            navOwnPlayerId = OFFLINE_PLAYER_ID
        }
        findNavController().navigate(action)
    }

    private fun resumeGame(game: GameWithPlayerInfo) {
        val enemyPlayerUid =
            if (game.attackerUid == navOwnPlayerId) game.defenderUid else game.attackerUid
        navEnemyId = enemyPlayerUid!!
        val action =
            MainFragmentDirections.actionMainFragmentToGameHostFragment()
        findNavController().navigate(action)
    }

    private fun showSnackBar(message: String) {
        val snackBar =
            Snackbar.make(requireView(), message, 2000)
        snackBar.setBackgroundTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorComplementary
            )
        )
        snackBar.show()
    }

}
