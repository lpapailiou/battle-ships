package battleships.esa.ffhs.ch.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "board_table")
data class BoardEntity(
    @Embedded var ships: ShipListEntity,
    @Embedded var shots: ShotListEntity
) {
    @PrimaryKey(autoGenerate = true)
    var board_id: Int = 0
}