package ch.ffhs.esa.battleships.data.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["uid"], unique = true)])
data class Player(
    val uid: String,
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var id: Long = 0
}

