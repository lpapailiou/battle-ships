package battleships.esa.ffhs.ch.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// BoardEntity will be referenced from GameEntity and as owner by ShipEntity and ShotEntity
@Entity(tableName = "board_table")
data class BoardEntity(@PrimaryKey(autoGenerate = true) var board_id: Int)