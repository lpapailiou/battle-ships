package battleships.esa.ffhs.ch.refactored.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.arellomobile.mvp.MvpAppCompatActivity
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import battleships.esa.ffhs.ch.refactored.ui.main.MainActivity.Companion.isFirstLogin
import javax.inject.Inject


class IntroModel : MvpAppCompatActivity(), FirebaseAuth.AuthStateListener {

    @Inject
    lateinit var mAuth: FirebaseAuth
    
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.intro_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BattleShipsApplication.getAppComponent().inject(this)
    }


}
