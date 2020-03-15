package battleships.esa.ffhs.ch.ui.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.main.MainActivity
import kotlinx.android.synthetic.main.intro_fragment.*
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------------------------- button change listener -----------------------------
        /*
        save_username_button.setOnClickListener(
            // TODO: validate input from  username_input_settings and save if valid

        )*/

    }

}

