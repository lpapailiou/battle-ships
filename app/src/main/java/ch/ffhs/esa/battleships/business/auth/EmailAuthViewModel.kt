package ch.ffhs.esa.battleships.business.auth

import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.ffhs.esa.battleships.event.Event
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import javax.inject.Inject

class EmailAuthViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val _loginSucceededEvent = MutableLiveData<Event<String>>()
    val loginSucceededEvent: LiveData<Event<String>> = _loginSucceededEvent

    private val _loginFailedEvent = MutableLiveData<Event<String>>()
    val loginFailedEvent: LiveData<Event<String>> = _loginFailedEvent

    private val TAG = "FirebaseEmailPassword"

    fun createUserWithEmailAndPassword(name: String, email: String, password: String) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        updateUserProfile(name)
                    } else if (!task.isSuccessful) {
                        triggerLoginFailedEvent(task.exception!!)
                    }
                }
        } catch (e: Exception) {
            triggerLoginFailedEvent(e)
        }
    }

    fun signInWithEmailAndPassword(emailAuthModel: EmailAuthModel) {
        try {
            firebaseAuth.signInWithEmailAndPassword(emailAuthModel.email, emailAuthModel.password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        _loginSucceededEvent.value = Event("Success!")
                    } else if (!task.isSuccessful) {
                        triggerLoginFailedEvent(task.exception!!)
                    }
                }
        } catch (e: Exception) {
            triggerLoginFailedEvent(e)
        }
    }

    private fun triggerLoginFailedEvent(e: Exception) {
        val message = when (e) {
            is FirebaseAuthInvalidCredentialsException -> "Invalid email or password"
            is FirebaseAuthEmailException -> "Invalid email address format"
            is IllegalArgumentException -> "Please enter an email address and password"
            else -> "Network error, please try restarting"
        }

        _loginFailedEvent.value = Event(message)
    }

    private fun updateUserProfile(name: String) {
        val user = firebaseAuth.currentUser

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //do some...
                }
            }
    }



}
