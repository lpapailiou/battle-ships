package battleships.esa.ffhs.ch.entity

import androidx.room.*
import battleships.esa.ffhs.ch.model.Direction

@Entity(tableName = "ship_table",
    foreignKeys = arrayOf(
        ForeignKey(entity = ShipEntity::class,
            parentColumns = ["ship_id"],
            childColumns = ["ship_owner_id"],
            onDelete = ForeignKey.CASCADE)
    ))
data class ShipEntity(
    val shipid: Int,
    @ColumnInfo(index = true) val ship_owner_id: Int,
    @Embedded(prefix = "ship_coord_") var bowCoordinate: CoordinateEntity,
    val size: Int,
    var direction: Direction,
    var isPositionValid: Boolean,
    var isHidden: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var ship_id: Int = 0
}