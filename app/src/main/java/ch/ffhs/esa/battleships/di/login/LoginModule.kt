package ch.ffhs.esa.battleships.di.login

import androidx.lifecycle.ViewModel
import ch.ffhs.esa.battleships.business.auth.EmailAuthViewModel
import ch.ffhs.esa.battleships.business.auth.GoogleAuthViewModel
import ch.ffhs.esa.battleships.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginModule {

    @Binds
    @IntoMap
    @ViewModelKey(GoogleAuthViewModel::class)
    abstract fun bindGoogleAuthViewModel(viewModel: GoogleAuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EmailAuthViewModel::class)
    abstract fun bindEmailAuthViewModel(viewModel: EmailAuthViewModel): ViewModel
}
