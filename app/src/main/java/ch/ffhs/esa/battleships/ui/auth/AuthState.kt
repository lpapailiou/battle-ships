package ch.ffhs.esa.battleships.ui.auth

enum class AuthState(var msg: String) {
    SUCCESS("Authentication success."),
    FAILED("Authentication failed."),
    EXCEPTION("Authentication exception.")
}
