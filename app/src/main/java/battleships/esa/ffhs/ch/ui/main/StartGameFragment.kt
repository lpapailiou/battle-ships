package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.ui.viewmodel.GameListViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.start_game_fragment.*


class StartGameFragment : Fragment() {

    lateinit var factory: ViewModelFactory
    lateinit var viewModel: GameListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        factory = InjectorUtils.provideGameViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(GameListViewModel::class.java)
        return inflater.inflate(R.layout.start_game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------------------------- button change listeners -----------------------------

        offline_button.setOnClickListener {
            viewModel.setGameActive(null)
            findNavController().navigate(R.id.action_mainFragment_to_boardFragment)
        }

        online_button.setOnClickListener {
            viewModel.setGameActive(null)
            findNavController().navigate(R.id.action_mainFragment_to_boardFragment)
        }

    }

}
