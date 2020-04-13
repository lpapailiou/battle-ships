package battleships.esa.ffhs.ch.entity

import androidx.room.*
import battleships.esa.ffhs.ch.utils.Converters

@Entity(tableName = "shot_table",
    foreignKeys = arrayOf(
        ForeignKey(entity = BoardEntity::class,
            parentColumns = ["board_id"],
            childColumns = ["shot_board_owner_id"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ShipEntity::class,
            parentColumns = ["ship_id"],
            childColumns = ["shot_ship_owner_id"],
            onDelete = ForeignKey.CASCADE)
    ))
data class ShotEntity(
    @ColumnInfo(index = true) val shot_board_owner_id: Int,
    @ColumnInfo(index = true) val shot_ship_owner_id: Int,
    @Embedded(prefix = "shot_coord_") val coordinate: CoordinateEntity,
    var drawable: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    var shot_id: Int = 0
}