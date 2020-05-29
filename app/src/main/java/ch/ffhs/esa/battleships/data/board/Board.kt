package ch.ffhs.esa.battleships.data.board

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.player.Player

@Entity(
    foreignKeys = [ForeignKey(
        entity = Game::class,
        parentColumns = ["uid"],
        childColumns = ["gameUid"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Player::class,
        parentColumns = ["uid"],
        childColumns = ["playerUid"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Board(
    @ColumnInfo(index = true)
    var gameUid: String = "",

    @ColumnInfo(index = true)
    var playerUid: String = ""
) {
    @PrimaryKey
    var uid: String = ""
}
