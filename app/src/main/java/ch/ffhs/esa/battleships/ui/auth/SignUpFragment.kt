package ch.ffhs.esa.battleships.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.business.auth.EmailAuthViewModel
import ch.ffhs.esa.battleships.databinding.SignupFragmentBinding
import ch.ffhs.esa.battleships.event.Event
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.signup_fragment.*
import javax.inject.Inject

class SignUpFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var viewDataBinding: SignupFragmentBinding

    private val emailAuthViewModel by viewModels<EmailAuthViewModel> { viewModelFactory }

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

        viewDataBinding = SignupFragmentBinding.inflate(inflater, container, false).apply {
            emailAuthViewModel = this@SignUpFragment.emailAuthViewModel
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        val successObserver = Observer<Event<String>> {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToBridgeFragment())
        }

        val failureObserver = Observer<Event<String>> {
            Toast.makeText(requireContext(), it.getContentIfNotHandled(), Toast.LENGTH_LONG).show()
        }

        button_sign_up.setOnClickListener {
            signUpUser()
        }
    }

        fun signUpUser(){
            if(edit_text_email_signup.text.toString().isEmpty()){
                edit_text_email_signup.error = "Please enter email!"
                edit_text_email_signup.requestFocus()
                return
            }
            if(Patterns.EMAIL_ADDRESS.matcher(edit_text_email_signup.text.toString()).matches()){
                edit_text_email_signup.error = "Please enter a valid email!"
                edit_text_email_signup.requestFocus()
                return
            }

            if(edit_text_password_signup.text.toString().isEmpty()){
                edit_text_email_signup.error = "Please enter a password!"
                edit_text_email_signup.requestFocus()
                return
            }


    }





    

}
