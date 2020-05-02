package battleships.esa.ffhs.ch.refactored.data.ship

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import battleships.esa.ffhs.ch.refactored.data.board.Board

@Entity(
    foreignKeys = [ForeignKey(
        entity = Board::class,
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
    @ColumnInfo(index = true) var boardId: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var id: Long = 0
}
