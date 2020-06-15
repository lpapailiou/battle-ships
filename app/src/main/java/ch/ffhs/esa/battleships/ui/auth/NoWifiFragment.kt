package ch.ffhs.esa.battleships.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ch.ffhs.esa.battleships.R
import kotlinx.android.synthetic.main.nowifi_fragment.*

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

