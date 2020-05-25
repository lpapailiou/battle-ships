package ch.ffhs.esa.battleships.data.ship

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ch.ffhs.esa.battleships.data.board.Board

@Entity(
    foreignKeys = [ForeignKey(
        entity = Board::class,
        parentColumns = ["uid"],
        childColumns = ["boardUid"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Ship(
    var x: Int,
    var y: Int,
    val size: Int,
    var direction: Direction,
    @ColumnInfo(index = true) var boardUid: String
) {
    @PrimaryKey
    var uid: String = ""
}
