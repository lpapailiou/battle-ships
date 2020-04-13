package battleships.esa.ffhs.ch.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import battleships.esa.ffhs.ch.model.Direction

@Entity(tableName = "ship_table")
data class ShipEntity(
    val shipid: Int,
    @Embedded(prefix = "ship_owner_") val owner: BoardEntity,
    @Embedded(prefix = "ship_coord_") var bowCoordinate: CoordinateEntity,
    val size: Int,
    var direction: Direction,
    var shotCount: Int,
    var isPositionValid: Boolean,
    var isHidden: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var ship_id: Int = 0
}