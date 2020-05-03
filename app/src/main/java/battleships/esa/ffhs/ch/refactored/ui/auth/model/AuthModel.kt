package battleships.esa.ffhs.ch.refactored.ui.auth.model

import com.arellomobile.mvp.MvpView

interface AuthModel: MvpView {

    fun onResultRequest(state: AuthState)
}
