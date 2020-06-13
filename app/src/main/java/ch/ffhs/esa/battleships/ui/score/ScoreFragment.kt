package ch.ffhs.esa.battleships.ui.score

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.business.score.ScoreViewModel
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import ch.ffhs.esa.battleships.databinding.ScoreFragmentBinding
import ch.ffhs.esa.battleships.ui.main.MainActivity
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navOwnPlayerId
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.skipLogin
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.score_fragment.*
import javax.inject.Inject

class ScoreFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val scoreViewModel by viewModels<ScoreViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: ScoreFragmentBinding

    private lateinit var listAdapter: ClosedGamesListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as BattleShipsApplication).appComponent.scoreComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        skipLogin = true
        if (firebaseAuth.currentUser != null) {
            navOwnPlayerId = firebaseAuth.currentUser!!.uid
        }

        scoreViewModel.start(firebaseAuth.currentUser?.uid ?: OFFLINE_PLAYER_ID)

        viewDataBinding = ScoreFragmentBinding.inflate(inflater, container, false).apply {
            scoreViewModel = this@ScoreFragment.scoreViewModel
        }

        return viewDataBinding.root
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.scoreViewModel
        listAdapter = ClosedGamesListAdapter(viewModel!!) {}
        viewDataBinding.scoreGameList.adapter = listAdapter
    }

    override fun onResume() {
        super.onResume()
        if (firebaseAuth.currentUser != null) {
            navOwnPlayerId = firebaseAuth.currentUser!!.uid
        }
    }
}
