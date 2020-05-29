package ch.ffhs.esa.battleships.ui.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ch.ffhs.esa.battleships.business.score.ScoreViewModel
import ch.ffhs.esa.battleships.databinding.ScoreFragmentBinding
import javax.inject.Inject

class ScoreFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val scoreViewModel by viewModels<ScoreViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: ScoreFragmentBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = ScoreFragmentBinding.inflate(inflater, container, false).apply {
            scoreViewModel = this@ScoreFragment.scoreViewModel
        }
        return viewDataBinding.root
    }
}
