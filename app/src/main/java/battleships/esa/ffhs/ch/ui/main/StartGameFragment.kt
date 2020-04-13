package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.gameListViewModel
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
            gameListViewModel.setGameActive(null)
            findNavController().navigate(R.id.action_mainFragment_to_boardFragment)
        }

        online_button.setOnClickListener {
            gameListViewModel.setGameActive(null)
            findNavController().navigate(R.id.action_mainFragment_to_boardFragment)
        }

    }

}
