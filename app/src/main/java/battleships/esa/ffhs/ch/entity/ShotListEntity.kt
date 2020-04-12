package battleships.esa.ffhs.ch.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import battleships.esa.ffhs.ch.entity.ShotEntity

@Entity(tableName = "shot_list_table")
data class ShotListEntity (
    @Embedded val shot: ShotEntity
) {
    @PrimaryKey(autoGenerate = true)
    var shot_list_id: Int = 0
}