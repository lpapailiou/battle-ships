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
import ch.ffhs.esa.battleships.business.score.ScoreViewModel
import ch.ffhs.esa.battleships.databinding.ScoreFragmentBinding
import ch.ffhs.esa.battleships.ui.main.MainActivity
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.skipLogin
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class ScoreFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val scoreViewModel by viewModels<ScoreViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: ScoreFragmentBinding


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
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
            scoreViewModel.start(firebaseAuth.currentUser!!.uid)

            viewDataBinding = ScoreFragmentBinding.inflate(inflater, container, false).apply {
                scoreViewModel = this@ScoreFragment.scoreViewModel
            }
        } else {
            viewDataBinding = ScoreFragmentBinding.inflate(inflater, container, false)  // TODO: try to add statistics for bot games
        }

        return viewDataBinding.root
    }
}
