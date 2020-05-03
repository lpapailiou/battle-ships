package battleships.esa.ffhs.ch.refactored.ui.auth

enum class AuthState(var msg: String) {
    SUCCESS("Authentication success."),
    FAILED("Authentication failed."),
    EXCEPTION("Authentication exception.")
}