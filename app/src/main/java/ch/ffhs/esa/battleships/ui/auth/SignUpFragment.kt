package ch.ffhs.esa.battleships.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ch.ffhs.esa.battleships.BattleShipsApplication
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.business.auth.EmailAuthModel
import ch.ffhs.esa.battleships.business.auth.EmailAuthViewModel
import ch.ffhs.esa.battleships.databinding.SignupFragmentBinding
import ch.ffhs.esa.battleships.event.Event
import ch.ffhs.esa.battleships.ui.main.MainActivity
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navOwnPlayerId
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.skipLogin
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.signup_fragment.*
import javax.inject.Inject

class SignUpFragment : Fragment() {

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
            emailAuthModel = EmailAuthModel()
            emailAuthViewModel = this@SignUpFragment.emailAuthViewModel
        }

        return viewDataBinding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        val successObserver = Observer<Event<String>> {
            val uid = it.getContentIfNotHandled()
            navOwnPlayerId = uid ?: OFFLINE_PLAYER_ID
            skipLogin = true
            (activity as MainActivity).setMenuVisible(true)
            findNavController().navigate(
                SignUpFragmentDirections.actionSignUpFragmentToMainFragment()
            )
            val toast = Toast.makeText(requireContext(), "Successfully registered!", Toast.LENGTH_LONG)
            toast.view.setBackgroundColor(Color.parseColor("#FA021F"))
            toast.view.findViewById<TextView>(android.R.id.message).setTextColor(Color.WHITE)
            toast.show()
        }

        val failureObserver = Observer<Event<String>> {
            val toast = Toast.makeText(requireContext(), it.getContentIfNotHandled(), Toast.LENGTH_LONG)
            toast.view.setBackgroundColor(Color.parseColor("#FA021F"))
            toast.view.findViewById<TextView>(android.R.id.message).setTextColor(Color.WHITE)
            toast.show()
        }

        emailAuthViewModel.signUpSucceededEvent.observe(viewLifecycleOwner, successObserver)
        emailAuthViewModel.signUpFailedEvent.observe(viewLifecycleOwner, failureObserver)

        button_sign_up.setOnClickListener {
            emailAuthViewModel.createUserWithEmailAndPassword(
                edit_text_email_signup.text.toString(),
                edit_text_password_signup.text.toString()
            )
        }
    }


}
