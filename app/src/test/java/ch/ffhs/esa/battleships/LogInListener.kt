package ch.ffhs.esa.battleships

import ch.ffhs.esa.battleships.data.player.PlayerRepository

interface LogInListener {

    fun logInSuccess(email: String?, password: String?)
    fun logInFailure(
        exception: Exception?,
        email: String?,
        password: String?
    )
}