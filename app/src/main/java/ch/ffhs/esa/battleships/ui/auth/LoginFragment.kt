package ch.ffhs.esa.battleships.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.auth.EmailAuthModel
import ch.ffhs.esa.battleships.business.auth.EmailAuthViewModel
import ch.ffhs.esa.battleships.business.auth.GoogleAuthViewModel
import ch.ffhs.esa.battleships.databinding.LoginFragmentBinding
import ch.ffhs.esa.battleships.event.Event
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.login_fragment.*
import javax.inject.Inject

class LoginFragment : Fragment() {

    private val CODE_SIGN_IN = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val googleAuthViewModel by viewModels<GoogleAuthViewModel> { viewModelFactory }

    private val emailAuthViewModel by viewModels<EmailAuthViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: LoginFragmentBinding

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as BattleShipsApplication).appComponent.loginComponent()
            .create()
            .inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding = LoginFragmentBinding.inflate(inflater, container, false).apply {
            emailAuthModel = EmailAuthModel()
            emailAuthViewModel = this@LoginFragment.emailAuthViewModel
            googleAuthViewModel = this@LoginFragment.googleAuthViewModel
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val successObserver = Observer<Event<String>> {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToBridgeFragment())
        }


        val failureObserver = Observer<Event<String>> {
            Toast.makeText(requireContext(), it.getContentIfNotHandled(), Toast.LENGTH_LONG).show()
        }

        googleAuthViewModel.loginSucceededEvent.observe(viewLifecycleOwner, successObserver)
        emailAuthViewModel.loginSucceededEvent.observe(viewLifecycleOwner, successObserver)

        googleAuthViewModel.loginFailedEvent.observe(viewLifecycleOwner, failureObserver)
        emailAuthViewModel.loginFailedEvent.observe(viewLifecycleOwner, failureObserver)


        sign_up_link.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
        }

        button_sign_in_google.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, CODE_SIGN_IN)
        }

        configureGoogleSignIn()
    }


    private fun configureGoogleSignIn() { // TODO provide with dagger
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    googleAuthViewModel.firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) { // TODO remove once all exceptions known are handled gracefully
//                Toast.makeText(requireContext(), "Google sign in failed:", Toast.LENGTH_LONG).show()
                throw e
            }
        }
    }


}
