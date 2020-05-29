package ch.ffhs.esa.battleships.data.player

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    val name: String = ""
) {
    @PrimaryKey
    var uid: String = ""
}

