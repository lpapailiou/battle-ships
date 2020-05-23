package ch.ffhs.esa.battleships.di.login

import ch.ffhs.esa.battleships.ui.auth.LoginFragment
import dagger.Subcomponent

@Subcomponent(modules = [LoginModule::class])
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }

    fun inject(loginFragment: LoginFragment)
}
