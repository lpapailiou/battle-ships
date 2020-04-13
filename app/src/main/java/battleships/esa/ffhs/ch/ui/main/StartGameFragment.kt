package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.mainViewModel
import kotlinx.android.synthetic.main.start_game_fragment.*


class StartGameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.start_game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------------------------- button change listeners -----------------------------

        offline_button.setOnClickListener {
            mainViewModel.setGameActive(null)       // TODO: we want to start a new game, so the current game is set to null. idea: this will let the reporistory know to create a new game. malfunctioning because async for the moment
            findNavController().navigate(R.id.action_mainFragment_to_boardFragment)
        }

        online_button.setOnClickListener {
            mainViewModel.setGameActive(null)       // TODO: same as above
            findNavController().navigate(R.id.action_mainFragment_to_boardFragment)
        }

    }

}
