package ch.ffhs.esa.battleships.di.login

import ch.ffhs.esa.battleships.business.auth.GoogleAuthViewModel
import ch.ffhs.esa.battleships.ui.auth.LoginFragment
import ch.ffhs.esa.battleships.ui.auth.SignUpFragment
import dagger.Subcomponent

@Subcomponent(modules = [LoginModule::class])
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }

    fun inject(loginFragment: LoginFragment)
    fun inject(signUpFragment: SignUpFragment)
    fun inject(googleLoginViewModel: GoogleAuthViewModel)
}
