package ch.ffhs.esa.battleships.ui.auth.model

import ch.ffhs.esa.battleships.ui.auth.AuthState
import com.arellomobile.mvp.MvpView

interface AuthModel: MvpView {

    fun onResultRequest(state: AuthState)
}
