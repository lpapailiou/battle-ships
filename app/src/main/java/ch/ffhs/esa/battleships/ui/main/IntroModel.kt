package ch.ffhs.esa.battleships.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.R
import com.google.firebase.auth.FirebaseAuth
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
        (this.application as BattleShipsApplication).appComponent.loginComponent().create()
            .inject(this)
    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        val user = mAuth.currentUser
        if (user == null) {
//            startActivity(Intent(this@IntroModel, LoginActivity::class.java))
            finish()
        }
    }


}
