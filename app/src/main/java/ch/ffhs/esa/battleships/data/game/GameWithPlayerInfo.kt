package ch.ffhs.esa.battleships.data.game

import android.text.format.DateUtils
import java.util.*

data class GameWithPlayerInfo(
    val gameId: Long,
    val attackerName: String,
    val defenderName: String,
    val playerAtTurnName: String,
    val lastChangedAt: Date,
    val attackerUID: String,
    val defenderUID: String
) {
    fun lastChangedAtFormatted(): CharSequence {
        return DateUtils.getRelativeTimeSpanString(lastChangedAt.time)
    }
}
