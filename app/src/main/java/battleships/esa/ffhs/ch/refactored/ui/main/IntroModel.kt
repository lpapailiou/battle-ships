package battleships.esa.ffhs.ch.refactored.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.arellomobile.mvp.MvpAppCompatActivity
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import battleships.esa.ffhs.ch.refactored.ui.auth.LoginActivity
import javax.inject.Inject


@SuppressLint("Registered")
class IntroModel : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    // TODO: It should not be a AppCompatActivity() it should be a MvpAppCompatActivity() -> Error in Library, what can we do?

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

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        val user = mAuth.currentUser
        if (user == null) {
            startActivity(Intent(this@IntroModel, LoginActivity::class.java))
            finish()
        }
    }


}
