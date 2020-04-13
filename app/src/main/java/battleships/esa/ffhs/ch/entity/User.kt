package battleships.esa.ffhs.ch.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(        // TODO: not in use yet
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var user_id: Int = 0
}

