package ch.ffhs.esa.battleships.data.game

data class GameWithPlayerInfo(
    val gameId: Long,
    val attackerName: String,
    val defenderName: String
)
