package ch.ffhs.esa.battleships.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.ui.auth.LoginFragment
import ch.ffhs.esa.battleships.ui.bridge.BridgeFragment
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navOwnPlayerId
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.skipLogin
import com.google.firebase.auth.FirebaseAuth

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)       // hide navigation drawer icon
        initMainFragment()
    }

    // ----------------------------- fragment choice (depending on first startup) -----------------------------

    fun initMainFragment() {
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_main) != null) {
            try {
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                if (skipLogin) {
                    var frag: Fragment? = null
                    for (i in childFragmentManager.fragments) {
                        if (i is BridgeFragment) {
                            frag = i
                            break
                        }
                    }
                    if (frag == null) {
                        frag = BridgeFragment()
                    }
                    if (navOwnPlayerId == OFFLINE_PLAYER_ID && auth.currentUser != null) {
                        navOwnPlayerId = auth.currentUser!!.uid
                    }
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_main, frag, "bridge").commit()
                } else {
                    var frag: Fragment? = null
                    for (i in childFragmentManager.fragments) {
                        if (i is LoginFragment) {
                            frag = i
                            break
                        }
                    }
                    if (frag == null) {
                        frag = LoginFragment()
                    }
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_main, frag, "login").commit()
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

}