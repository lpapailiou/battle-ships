package ch.ffhs.esa.battleships.business.auth

data class EmailAuthModel(
    var email: String = "",
    var password: String = ""
) {

    fun clearCredentials() {
        email = "";
        password = "";
    }

}
