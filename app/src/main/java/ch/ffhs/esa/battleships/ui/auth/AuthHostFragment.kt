package ch.ffhs.esa.battleships.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class AuthHostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.auth_host_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAuthFragment()
    }

    override fun onResume() {
        super.onResume()
        initAuthFragment()
    }

    // ----------------------------- fragment choice (depending on first startup) -----------------------------

    fun initAuthFragment() {
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_auth) != null) {
            try {
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                if (!(activity as MainActivity).hasWifi()) {
                    var frag: Fragment? = null
                    for (i in childFragmentManager.fragments) {
                        if (i is NoWifiFragment) {
                            frag = i
                            break
                        }
                    }
                    if (frag == null) {
                        frag = NoWifiFragment()
                    }
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_auth,
                            frag, "noWifi").commit()
                } else if (auth.currentUser != null) {
                    var frag: Fragment? = null
                    for (i in childFragmentManager.fragments) {
                        if (i is SignOutFragment) {
                            frag = i
                            break
                        }
                    }
                    if (frag == null) {
                        frag = SignOutFragment()
                    }
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_auth,
                            frag, "signOut").commit()
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
                        .replace(R.id.fragment_container_auth, frag, "login").commit()
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

}