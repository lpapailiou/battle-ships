package battleships.esa.ffhs.ch.refactored.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.arellomobile.mvp.MvpAppCompatActivity
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import battleships.esa.ffhs.ch.refactored.ui.auth.LoginActivity
import battleships.esa.ffhs.ch.refactored.ui.main.MainActivity.Companion.isFirstLogin
import kotlinx.android.synthetic.main.intro_fragment.*
import javax.inject.Inject


class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.intro_fragment, container, false)
    }

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

