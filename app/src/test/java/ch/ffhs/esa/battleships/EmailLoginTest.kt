package ch.ffhs.esa.battleships

import android.app.Activity
import ch.ffhs.esa.battleships.business.auth.EmailAuthModel
import ch.ffhs.esa.battleships.business.auth.EmailAuthViewModel
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class EmailLoginTest() : LogInListener  {

    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = -1
        private const val UNDEF = 0
    }

    private lateinit var successTask: Task<AuthResult>
    private lateinit var failureTask: Task<AuthResult>

    @Mock
    private lateinit var mAuth: FirebaseAuth
    private lateinit var logInModel: EmailAuthModel
    private lateinit var loginViewModel: EmailAuthViewModel

    private var logInResult = UNDEF

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        successTask = object : Task<AuthResult>() {
            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = true

            override fun addOnCompleteListener(executor: Executor,
                                               onCompleteListener: OnCompleteListener<AuthResult>
            ): Task<AuthResult> {
                onCompleteListener.onComplete(successTask)
                return successTask
            }

            override fun getException(): Exception? {
                return successTask.exception
            }

            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                return addOnFailureListener(p0)
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                return addOnFailureListener(p0, p1)
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                return addOnFailureListener(p0, p1)
            }

            override fun getResult(): AuthResult? {
                return result
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult? {
                return getResult(p0)
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                return addOnSuccessListener(p0)
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                return addOnSuccessListener(p0, p1)
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                return addOnSuccessListener(p0, p1)
            }

            override fun isCanceled(): Boolean {
                return isCanceled
            }


        }

        failureTask = object : Task<AuthResult>() {
            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = false

            override fun addOnCompleteListener(executor: Executor,
                                               onCompleteListener: OnCompleteListener<AuthResult>): Task<AuthResult> {
                onCompleteListener.onComplete(failureTask)
                return failureTask
            }

            override fun getException(): java.lang.Exception? {
                return failureTask.exception
            }

            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                return addOnFailureListener(p0)
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                return addOnFailureListener(p0, p1)
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                return addOnFailureListener(p0, p1)
            }

            override fun getResult(): AuthResult? {
                return result
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult? {
                return getResult(p0)
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                return addOnSuccessListener(p0)
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                return addOnSuccessListener(p0, p1)
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                return addOnSuccessListener(p0, p1)
            }

            override fun isCanceled(): Boolean {
                return isCanceled
            }
        }

        logInModel = EmailAuthModel("", "")

        val player = Mockito.mock(PlayerRepository::class.java)
            loginViewModel = EmailAuthViewModel(player)

    }

    @Test
    fun logInSuccess_test() {
        val email = "test1@test.ch"
        val password = "123456"
        Mockito.`when`(mAuth.signInWithEmailAndPassword(email, password))
            .thenReturn(successTask)
        if(successTask.isSuccessful){
            logInResult = SUCCESS
        }
        mAuth.signInWithEmailAndPassword(email, password)
        assert(logInResult == SUCCESS)
    }

    @Test
    fun logInFailure_test() {
        val email = "test1@test.ch"
        val password = "123_456"
        Mockito.`when`(mAuth.signInWithEmailAndPassword(email, password))
            .thenReturn(failureTask)
        if(!failureTask.isSuccessful){
            logInResult = FAILURE
        }
        mAuth.signInWithEmailAndPassword(email, password)
        assert(logInResult == FAILURE)
    }

    override fun logInSuccess(email: String?, password: String?) {
        logInResult = SUCCESS
    }

    override fun logInFailure(exception: Exception?, email: String?, password: String?) {
        logInResult = FAILURE
    }

}
