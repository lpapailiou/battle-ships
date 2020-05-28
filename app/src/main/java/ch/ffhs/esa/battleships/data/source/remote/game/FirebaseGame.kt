package ch.ffhs.esa.battleships.data.source.remote.game

import ch.ffhs.esa.battleships.business.game.GameState
import ch.ffhs.esa.battleships.data.game.Game

data class FirebaseGame(
    var state: GameState? = null,
    var defenderUid: String? = null
) {
    var uid: String = ""

    var attackerUid: String? = null

    var playerAtTurnUid: String? = null

    var lastChangedAt: Long? = 0L

    var winnerUid: String? = null

    var timeZoneOffset: Long? = null

    var date: Long? = null

    var day: Long? = null

    var minutes: Long? = null

    var hours: Long? = null

    var seconds: Long? = null

    var time: Long? = null

    var year: Long? = null

    fun toGame(): Game {
        val game = Game(
            System.currentTimeMillis(),
            state,
            defenderUid
        )
        game.uid = uid
        game.attackerUid = attackerUid
        game.playerAtTurnUid = playerAtTurnUid
        game.winnerUid = winnerUid

        return game;
    }

}
