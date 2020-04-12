package battleships.esa.ffhs.ch.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coordinate_table", primaryKeys = ["x", "y"])
data class CoordinateEntity(val x: Int, val y: Int)