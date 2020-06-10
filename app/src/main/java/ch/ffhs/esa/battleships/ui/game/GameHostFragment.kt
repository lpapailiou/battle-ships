package ch.ffhs.esa.battleships.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.ui.auth.SignOutFragment
import ch.ffhs.esa.battleships.ui.boardpreparation.BoardPreparationFragment
import ch.ffhs.esa.battleships.ui.main.MainActivity
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navUid
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.skipLogin

class GameHostFragment : Fragment() {

    companion object {
        var gameId: MutableLiveData<String> = MutableLiveData()
        var navEnemyId: String = ""
    }

    private val args: GameHostFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        skipLogin = true
        navEnemyId = args.enemyPlayerUid
        navUid = args.ownPlayerUid
        if (args.gameUid != "") {
            gameId.value = args.gameUid
        }
        return inflater.inflate(R.layout.game_host_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameId.observe(viewLifecycleOwner, Observer{
            initStateSensitiveBoardFragment()
        })
        initStateSensitiveBoardFragment()
    }

    // ----------------------------- fragment choice (depending on first startup) -----------------------------

    open fun initStateSensitiveBoardFragment() {
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_game) != null) {
            if (gameId.value == null) {
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
                var navargs: Bundle = Bundle()
                navargs.putString("uid", args.uid)
                navargs.putBoolean("isBotGame", args.isBotGame)
                frag.arguments = navargs
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
                var navargs: Bundle = Bundle()
                navargs.putString("gameUid", gameId.value)
                navargs.putString("ownPlayerUid", navUid)
                navargs.putString("enemyPlayerUid", navEnemyId)
                frag.arguments = navargs
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_game, frag, "game").commit()
            }
        }
    }

}