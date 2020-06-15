package ch.ffhs.esa.battleships.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.ui.boardpreparation.BoardPreparationFragment
import ch.ffhs.esa.battleships.ui.main.MainActivity
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navGameId

class GameHostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.game_host_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navGameId.observe(viewLifecycleOwner, Observer{
            initStateSensitiveBoardFragment()
        })
        initStateSensitiveBoardFragment()
    }

    // ----------------------------- fragment choice (depending on first startup) -----------------------------

    fun initStateSensitiveBoardFragment() {
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_game) != null) {
            if (navGameId.value == null) {
                var frag: Fragment? = null
                for (i in childFragmentManager.fragments) {
                    if (i is BoardPreparationFragment) {
                        frag = i
                        break
                    }
                }
                if (frag == null) {
                    frag = BoardPreparationFragment()
                }
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_game, frag, "preparation").commit()
            } else {
                var frag: Fragment? = null
                for (i in childFragmentManager.fragments) {
                    if (i is GameFragment) {
                        frag = i
                        break
                    }
                }
                if (frag == null) {
                    frag = GameFragment()
                }
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_game, frag, "game").commit()
            }
        }
    }

}