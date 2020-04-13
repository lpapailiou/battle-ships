package battleships.esa.ffhs.ch.entity

import androidx.room.*
import battleships.esa.ffhs.ch.utils.Converters

@Entity(tableName = "shot_table")
data class ShotEntity(
    @Embedded(prefix = "shot_coord_") val coordinate: CoordinateEntity,
    @Embedded(prefix = "shot_owner_") val owner_board: BoardEntity,
    val owner_ship_id: Int,
    var isHit: Boolean,
    var drawable: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var shot_id: Int = 0
}