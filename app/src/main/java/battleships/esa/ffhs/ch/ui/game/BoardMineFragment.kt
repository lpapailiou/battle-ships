package battleships.esa.ffhs.ch.ui.game

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame
import kotlinx.android.synthetic.main.board_mine_fragment.*


class BoardMineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.board_mine_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        board_mine.setOnClickListener {
            if (!activeGame!!.isActivePlayerMe && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                (parentFragment as BoardGameFragment).switchFragments()
            }
        }
    }

}
