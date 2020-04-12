package battleships.esa.ffhs.ch.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import battleships.esa.ffhs.ch.entity.ShipEntity

@Entity(tableName = "ship_list_table")
data class ShipListEntity (
    @Embedded val ship: ShipEntity
) {
    @PrimaryKey(autoGenerate = true)
    var ship_list_id: Int = 0
}