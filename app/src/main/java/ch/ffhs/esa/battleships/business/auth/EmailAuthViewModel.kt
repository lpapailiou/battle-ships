package ch.ffhs.esa.battleships.business.auth

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

    private val _signUpSucceededEvent = MutableLiveData<Event<String>>()
    val signUpSucceededEvent: LiveData<Event<String>> = _signUpSucceededEvent

    private val _signUpFailedEvent = MutableLiveData<Event<String>>()
    val signUpFailedEvent: LiveData<Event<String>> = _signUpFailedEvent

    fun createUserWithEmailAndPassword(name: String, email: String, password: String) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        _signUpSucceededEvent.value = Event(firebaseAuth.currentUser!!.uid)
                    } else if (!task.isSuccessful) {
                        triggerSignUpFailedEvent(task.exception!!)
                    }
                }
        } catch (e: Exception) {
            triggerSignUpFailedEvent(e)
        }
    }

    fun signInWithEmailAndPassword(emailAuthModel: EmailAuthModel) {
        try {
            firebaseAuth.signInWithEmailAndPassword(emailAuthModel.email, emailAuthModel.password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        _loginSucceededEvent.value = Event(firebaseAuth.currentUser!!.uid)
                    } else if (!task.isSuccessful) {
                        triggerLoginFailedEvent(task.exception!!)
                    }
                }
        } catch (e: Exception) {
            triggerLoginFailedEvent(e)
        }
    }

    private fun triggerSignUpFailedEvent(e: Exception) {
        val message = when (e) {
            is FirebaseAuthUserCollisionException -> "This email already exists."
            is FirebaseAuthWeakPasswordException -> "The password should be at least 6 characters"
            is FirebaseAuthInvalidCredentialsException -> "Invalid email address format"
            is FirebaseAuthEmailException -> "Invalid email address format"
            is IllegalArgumentException -> "Please enter an email address and password"
            else -> "Network error, please try restarting"
        }

        _signUpFailedEvent.value = Event(message)

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
}