package battleships.esa.ffhs.ch.refactored.data.impl.local.player

import androidx.room.Dao
import androidx.room.Query
import battleships.esa.ffhs.ch.refactored.data.player.Player

@Dao
interface PlayerDao {

    @Query("select * from Player where playerId = :playerId")
    fun findByPlayerId(playerId: String): Player?

    @Query("select * from Player where id = :id")
    fun findById(id: Long): Player?
}
