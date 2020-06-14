package ch.ffhs.esa.battleships.data.game

import android.text.format.DateUtils
import java.util.*

data class GameWithPlayerInfo(
    val gameUid: String,
    val attackerName: String?,
    val defenderName: String,
    val playerAtTurnName: String?,
    val lastChangedAt: Long?,
    val attackerUid: String?,
    val defenderUid: String,
    val winnerUid: String?
) {
    fun lastChangedAtFormatted(): CharSequence {
        return DateUtils.getRelativeTimeSpanString(lastChangedAt!!)
    }
}
