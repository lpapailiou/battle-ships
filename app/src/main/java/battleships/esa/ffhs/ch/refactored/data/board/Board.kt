package battleships.esa.ffhs.ch.refactored.data.board

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import battleships.esa.ffhs.ch.refactored.data.game.Game
import battleships.esa.ffhs.ch.refactored.data.player.Player

@Entity(
    foreignKeys = [ForeignKey(
        entity = Game::class,
        parentColumns = ["id"],
        childColumns = ["gameId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Player::class,
        parentColumns = ["id"],
        childColumns = ["playerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Board(
    @ColumnInfo(index = true)
    val gameId: Long = 0,

    @ColumnInfo(index = true)
    val playerId: Long?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var id: Long = 0

}
