package ch.ffhs.esa.battleships.data.source.local.ship

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ch.ffhs.esa.battleships.data.ship.Ship

@Dao
interface ShipDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ship: Ship)

    @Query("select * from Ship where boardUid = :boardId")
    fun loadByBoard(boardId: String): List<Ship>
}
