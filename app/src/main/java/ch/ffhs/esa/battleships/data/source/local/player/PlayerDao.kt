package ch.ffhs.esa.battleships.data.source.local.player

import androidx.room.Dao
import androidx.room.Query
import ch.ffhs.esa.battleships.data.player.Player

@Dao
interface PlayerDao {

    @Query("select * from Player where playerId = :playerId")
    fun findByPlayerId(playerId: String): Player?

    @Query("select * from Player where id = :id")
    fun findById(id: Long): Player?
}
