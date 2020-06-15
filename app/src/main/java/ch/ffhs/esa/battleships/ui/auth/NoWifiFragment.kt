package ch.ffhs.esa.battleships.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navOwnPlayerId
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.nowifi_fragment.*
import kotlinx.android.synthetic.main.signout_fragment.*
import javax.inject.Inject

class NoWifiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.nowifi_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        k_button.setOnClickListener {
            findNavController().navigate(AuthHostFragmentDirections.actionAuthHostFragmentToMainFragment())
        }
    }

}

