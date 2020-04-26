package battleships.esa.ffhs.ch.old.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R


class GameActiveFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.game_active_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGameFragments()
    }

    // ----------------------------- child fragment control -----------------------------

    private fun initGameFragments() {
//        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_game_active) != null) {
//            childFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container_game_active, BoardFragment(), "mine")
//                .commit()
//        }
//        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_game_inactive) != null) {
//            childFragmentManager.beginTransaction()
//                .replace(
//                    R.id.fragment_container_game_inactive,
//                    GameBoardOpponentFragment(),
//                    "other"
//                )
//                .commit()
//        }
    }

    // will be accessed as well from child fragments
    fun switchFragments() {
//        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            return
//        }
//        if (currentGame.isMyBoardVisible()) {
//            childFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container_game_active, GameBoardOpponentFragment(), "other")
//                .commit()
//            childFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container_game_inactive, BoardFragment(), "mine")
//                .commit()
//            currentGame.setMyBoardVisible(false)
//        } else {
//            childFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container_game_active, BoardFragment(), "mine")
//                .commit()
//            childFragmentManager.beginTransaction()
//                .replace(
//                    R.id.fragment_container_game_inactive,
//                    GameBoardOpponentFragment(),
//                    "other"
//                )
//                .commit()
//            currentGame.setMyBoardVisible(true)
//        }
    }

}
