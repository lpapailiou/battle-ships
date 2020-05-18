package ch.ffhs.esa.battleships.ui.auth.presenter

import ch.ffhs.esa.battleships.ui.auth.AuthState
import ch.ffhs.esa.battleships.ui.auth.model.AuthModel
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

//    init {
//        BattleShipsApplication.getAppComponent().inject(this)
//    }

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
