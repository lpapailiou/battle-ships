package battleships.esa.ffhs.ch.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import battleships.esa.ffhs.ch.utils.Converters

@Entity(tableName = "shot_table")
data class ShotEntity(
    @Embedded(prefix = "shot_coord_") val coordinate: CoordinateEntity,
    var isHit: Boolean,
    var drawable: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var shot_id: Int = 0
}