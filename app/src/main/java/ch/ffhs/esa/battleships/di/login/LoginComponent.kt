package ch.ffhs.esa.battleships.di.login

import ch.ffhs.esa.battleships.ui.auth.LoginActivity
import ch.ffhs.esa.battleships.ui.auth.presenter.EmailLogin
import ch.ffhs.esa.battleships.ui.auth.presenter.GoogleLogin
import ch.ffhs.esa.battleships.ui.main.IntroModel
import dagger.Subcomponent

@Subcomponent(modules = [LoginModule::class])
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }

    fun inject(IntroModel: IntroModel)
    fun inject(loginActivity: LoginActivity)
    fun inject(authPresenter: EmailLogin)
    fun inject(authPresenter: GoogleLogin)
}
