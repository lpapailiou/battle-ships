package ch.ffhs.esa.battleships.di.main

import ch.ffhs.esa.battleships.business.auth.GoogleAuthViewModel
import ch.ffhs.esa.battleships.ui.auth.LoginFragment
import ch.ffhs.esa.battleships.ui.auth.SignUpFragment
import ch.ffhs.esa.battleships.ui.main.MainFragment
import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(mainFragment: MainFragment)
}
