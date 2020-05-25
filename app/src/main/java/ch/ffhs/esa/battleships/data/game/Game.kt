package ch.ffhs.esa.battleships.data.game

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ch.ffhs.esa.battleships.business.game.GameState
import ch.ffhs.esa.battleships.data.player.Player
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Player::class,
        parentColumns = ["uid"],
        childColumns = ["attackerUid"]
    ), ForeignKey(
        entity = Player::class,
        parentColumns = ["uid"],
        childColumns = ["defenderUid"]
    ), ForeignKey(
        entity = Player::class,
        parentColumns = ["uid"],
        childColumns = ["playerAtTurnUid"]
    ), ForeignKey(
        entity = Player::class,
        parentColumns = ["uid"],
        childColumns = ["winnerUid"]
    )]
)
data class Game(
    var lastChangedAt: Date,
    var state: GameState,
    @ColumnInfo(index = true)
    var defenderUid: String
) {
    @PrimaryKey
    var uid: String = ""

    @ColumnInfo(index = true)
    var attackerUid: String? = null

    @ColumnInfo(index = true)
    var playerAtTurnUid: String? = null

    @ColumnInfo(index = true)
    var winnerUid: String? = null

}
