package battleships.esa.ffhs.ch.refactored.data.shot

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
data class Shot(
    val x: Int,
    val y: Int,
    @ColumnInfo(index = true)
    val boardId: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var id: Int = 0
}
