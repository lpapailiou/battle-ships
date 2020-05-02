package battleships.esa.ffhs.ch.refactored.data.impl.local.ship

import androidx.room.Dao
import androidx.room.Insert
import battleships.esa.ffhs.ch.refactored.data.ship.Ship

@Dao
interface ShipDao {

    @Insert
    fun insert(ship: Ship): Long
}
