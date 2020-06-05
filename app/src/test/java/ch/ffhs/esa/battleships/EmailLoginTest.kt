package ch.ffhs.esa.battleships

import androidx.lifecycle.ViewModel
import ch.ffhs.esa.battleships.business.auth.EmailAuthViewModel
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import com.google.firebase.auth.FirebaseAuth
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito


class EmailLoginTest @javax.inject.Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = -1
        private const val UNDEF = 0
    }

    @Mock
    private lateinit var mAuth: FirebaseAuth
    private lateinit var logInModel: EmailAuthViewModel

    private var logInResult = UNDEF

    @Test
    fun logInSuccess_test() {
        val email = "test@test.ch"
        val password = "123456"
        Mockito.`when`(mAuth!!.signInWithEmailAndPassword(email, password))
            .thenReturn(successTask)
        logInModel!!.logIn(email, password)
        assert(logInResult == SUCCESS)
    }


    @Test
    fun logInFailure_test() {
        val email = "cool@cool.com"
        val password = "123_456"
        Mockito.`when`(mAuth!!.signInWithEmailAndPassword(email, password))
            .thenReturn(failureTask)
        accountModel!!.logIn(email, password)
        assert(logInResult == FAILURE)
    }






}
