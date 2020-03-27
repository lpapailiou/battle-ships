package battleships.esa.ffhs.ch.ui.game

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.isGameStarted
import kotlinx.android.synthetic.main.board_preparation_fragment.*


class BoardPreparationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.board_preparation_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // triggered when game is supposed to start. will change the state of the game and navigate to the actual game fragment
        startgame_button.setOnClickListener{
            isGameStarted = true
            var parent: BoardFragment = (parentFragment as BoardFragment)
            parent.switchToGameFragment()
        }
    }

}
