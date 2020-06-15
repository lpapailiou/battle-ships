package ch.ffhs.esa.battleships

interface GameListener {

    fun gameSuccess(gameUid: String?, ownPlayerUid: String?, enemyPlayerUid: String?)
}