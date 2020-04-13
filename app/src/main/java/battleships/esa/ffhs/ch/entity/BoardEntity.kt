package battleships.esa.ffhs.ch.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "board_table",
    foreignKeys = arrayOf(
        ForeignKey(entity = GameEntity::class,
            parentColumns = ["game_id"],
            childColumns = ["game_owner_id"],
            onDelete = ForeignKey.CASCADE)
    ))
data class BoardEntity(
    @ColumnInfo(index = true) val game_owner_id: Int,
    val isMine: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var board_id: Int = 0
}