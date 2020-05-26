package ch.ffhs.esa.battleships.data.shot

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
data class Shot(
    val x: Int,
    val y: Int,
    @ColumnInfo(index = true)
    val boardUid: String
) {
    @PrimaryKey
    var uid: String = ""
}
