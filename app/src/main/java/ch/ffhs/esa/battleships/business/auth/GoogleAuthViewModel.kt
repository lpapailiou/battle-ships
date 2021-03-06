package ch.ffhs.esa.battleships.business.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.data.player.Player
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import ch.ffhs.esa.battleships.event.Event
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoogleAuthViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val _loginSucceededEvent = MutableLiveData<Event<String>>()
    val loginSucceededEvent: LiveData<Event<String>> = _loginSucceededEvent

    private val _loginFailedEvent = MutableLiveData<Event<String>>()
    val loginFailedEvent: LiveData<Event<String>> = _loginFailedEvent

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        try {
            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        createPlayer(
                            firebaseAuth.currentUser!!.uid,
                            firebaseAuth.currentUser!!.email!!
                        )

                    } else {
                        _loginFailedEvent.value = Event("Error")
                    }
                }
        } catch (e: Exception) {
            _loginFailedEvent.value = Event("Error")
            e.stackTrace
        }
    }


    private fun createPlayer(uid: String, name: String) = viewModelScope.launch {
        try {
            Log.d("procedureLogger", "------------- >>>>>>> auth createPlayer()")
        val player = Player(name)
        player.uid = uid
        playerRepository.save(player)
        _loginSucceededEvent.value = Event(firebaseAuth.currentUser!!.uid)
        } catch (e: Exception) {
            e.stackTrace
        }
    }
}
