package battleships.esa.ffhs.ch.refactored.ui.auth.presenter

import battleships.esa.ffhs.ch.refactored.BattleShipsApplication
import battleships.esa.ffhs.ch.refactored.ui.auth.AuthState
import battleships.esa.ffhs.ch.refactored.ui.auth.model.AuthModel
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

@InjectViewState
class GoogleLogin : MvpPresenter<AuthModel>()  {

    @Inject
    lateinit var mAuth: FirebaseAuth

    init {
        BattleShipsApplication.getAppComponent().inject(this)
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        try {
            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewState.onResultRequest(AuthState.SUCCESS)

                    } else {
                        viewState.onResultRequest(AuthState.FAILED)
                    }
                }
        } catch (e: Exception) {
            viewState.onResultRequest(AuthState.SUCCESS)
        }
    }
}