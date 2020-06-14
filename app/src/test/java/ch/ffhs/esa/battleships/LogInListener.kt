package ch.ffhs.esa.battleships

interface LogInListener {

    fun logInSuccess(email: String?, password: String?)
    fun logInFailure(
        exception: Exception?,
        email: String?,
        password: String?
    )
}