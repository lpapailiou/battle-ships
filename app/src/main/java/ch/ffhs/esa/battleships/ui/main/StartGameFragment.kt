package ch.ffhs.esa.battleships.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.ui.bridge.BridgeFragmentDirections
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


        play_vs_bot_button.setOnClickListener {
            navigateToGame()
        }

        play_vs_friend_button.setOnClickListener {
            navigateToGame()
        }

    }

    private fun navigateToGame() {
        val action =
            BridgeFragmentDirections.actionMainFragmentToBoardPreparationFragment(OFFLINE_PLAYER_ID)
        findNavController().navigate(action)

    }

}
