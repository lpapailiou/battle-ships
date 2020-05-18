package ch.ffhs.esa.battleships.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.ui.auth.model.AuthModel
import ch.ffhs.esa.battleships.ui.auth.presenter.EmailLogin
import ch.ffhs.esa.battleships.ui.auth.presenter.GoogleLogin
import ch.ffhs.esa.battleships.ui.main.IntroModel
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_login.*

@SuppressLint("Registered")
class LoginActivity : AppCompatActivity(), AuthModel {

    // TODO: It should not be a AppCompatActivity() it should be a MvpAppCompatActivity() -> Error in Library, what can we do?

    private val CODE_SIGN_IN = 0

    @InjectPresenter
    lateinit var mEmailPresenter: EmailLogin

    @InjectPresenter
    lateinit var mGooglePresenter: GoogleLogin

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        (this.application as BattleShipsApplication).appComponent.loginComponent().create()
            .inject(this)

        configureGoogleSignIn()
    }

    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun onClickEmailLogin(v: View) {
        mEmailPresenter.signInWithEmailAndPassword(
            edit_text_email.text.toString(),
            edit_text_password.text.toString()
        )
    }

    fun onClickSignUpButton(v: View) {
        startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        finish()
    }

    fun onClickGoogleLogin(v: View) {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, CODE_SIGN_IN)
    }

    override fun onResultRequest(state: AuthState) {
        when (state) {
            AuthState.SUCCESS -> onSuccessAuth()
            AuthState.FAILED -> showMessage(state)
            AuthState.EXCEPTION -> showMessage(state)
        }
    }

    private fun onSuccessAuth() {
        startActivity(Intent(this@LoginActivity, IntroModel::class.java))
        finish()
    }

    private fun showMessage(authState: AuthState) {
        Toast.makeText(this, authState.msg, Toast.LENGTH_SHORT).show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                mGooglePresenter.firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }


}
