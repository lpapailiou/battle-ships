package ch.ffhs.esa.battleships.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navUid
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.signout_fragment.*
import javax.inject.Inject

class SignOutFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signout_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        sign_out_button.setOnClickListener {
            navUid = ""
            signOut()
            findNavController().navigate(AuthHostFragmentDirections.actionAuthHostFragmentToMainFragment())
        }
    }

    fun signOut(){
        firebaseAuth.signOut()
    }
}

