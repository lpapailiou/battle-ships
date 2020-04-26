package battleships.esa.ffhs.ch.refactored.data.ship

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import battleships.esa.ffhs.ch.refactored.data.player.Player

@Entity(
    foreignKeys = [ForeignKey(
        entity = Player::class,
        parentColumns = ["id"],
        childColumns = ["boardId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Ship(
    var x: Int,
    var y: Int,
    val size: Int,
    var direction: Direction,
    @ColumnInfo(index = true) val boardId: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var id: Int = 0
}
