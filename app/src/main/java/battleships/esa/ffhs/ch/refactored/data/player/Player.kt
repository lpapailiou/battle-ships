package battleships.esa.ffhs.ch.refactored.data.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @ColumnInfo(index = true) val playerId: String,
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var id: Long = 0
}

