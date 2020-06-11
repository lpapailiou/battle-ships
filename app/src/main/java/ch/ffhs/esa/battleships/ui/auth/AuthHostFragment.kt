package ch.ffhs.esa.battleships.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class AuthHostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.auth_host_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)       // hide navigation drawer icon
        initMainFragment()
    }

    override fun onResume() {
        super.onResume()
        initMainFragment()
    }

    // ----------------------------- fragment choice (depending on first startup) -----------------------------

    open fun initMainFragment() {
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_auth) != null) {
            try {
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                if (auth.currentUser != null) {
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
                println(e.stackTrace.toString())
            }
        }
    }

}