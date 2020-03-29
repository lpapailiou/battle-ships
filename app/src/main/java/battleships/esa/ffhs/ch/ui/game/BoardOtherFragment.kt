package battleships.esa.ffhs.ch.ui.game

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.drawable.GameState
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame
import kotlinx.android.synthetic.main.board_mine_fragment.*
import kotlinx.android.synthetic.main.board_other_fragment.*

class BoardOtherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.board_other_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boardOther.setOnClickListener {
            if (activeGame!!.isActivePlayerMe && resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE && activeGame!!.state != GameState.PREPARATION) {
                (parentFragment as BoardGameFragment).switchFragments()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
