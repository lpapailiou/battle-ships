package battleships.esa.ffhs.ch.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(        // TODO: not in use yet
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var user_id: Int = 0
}

