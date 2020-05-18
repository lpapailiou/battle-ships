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
        parentColumns = ["id"],
        childColumns = ["attackerId"]
    ), ForeignKey(
        entity = Player::class,
        parentColumns = ["id"],
        childColumns = ["defenderId"]
    ), ForeignKey(
        entity = Player::class,
        parentColumns = ["id"],
        childColumns = ["playerAtTurnId"]
    ), ForeignKey(
        entity = Player::class,
        parentColumns = ["id"],
        childColumns = ["winnerId"]
    )]
)
data class Game(
    var lastChangedAt: Date,
    var state: GameState,
    @ColumnInfo(index = true)
    var attackerId: Long,
    @ColumnInfo(index = true)
    var defenderId: Long,
    @ColumnInfo(index = true)
    var playerAtTurnId: Long,
    @ColumnInfo(index = true)
    var winnerId: Long?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var id: Long = 0
}
