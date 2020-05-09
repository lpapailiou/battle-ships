package battleships.esa.ffhs.ch.refactored.ui.auth.model

import battleships.esa.ffhs.ch.refactored.ui.auth.AuthState
import com.arellomobile.mvp.MvpView

interface AuthModel: MvpView {

    fun onResultRequest(state: AuthState)
}
