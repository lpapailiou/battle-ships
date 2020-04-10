package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.GameInstance
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.model.Game
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.isFirstLogin
import battleships.esa.ffhs.ch.ui.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.intro_fragment.*


class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.intro_fragment, container, false)
    }

    // TODO: validation of user name
    // TODO: prevent navigation if username is not correct

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------------------------- check if username was entered -----------------------------

        // temporary helper method to test gui
        username_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                var text = username_input.text
                if (text != null && text.length > 0) {
                    isFirstLogin = false
                }
            }
        })

    }

}
