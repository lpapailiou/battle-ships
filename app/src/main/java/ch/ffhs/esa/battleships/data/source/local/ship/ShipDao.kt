package ch.ffhs.esa.battleships.data.source.local.ship

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ch.ffhs.esa.battleships.data.ship.Ship

@Dao
interface ShipDao {

    @Insert
    fun insert(ship: Ship): Long

    @Query("select * from Ship where boardId = :boardId")
    fun loadByBoard(boardId: Long): List<Ship>
}
