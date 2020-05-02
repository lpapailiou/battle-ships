package battleships.esa.ffhs.ch.refactored.data.impl.local.board

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import battleships.esa.ffhs.ch.refactored.data.board.Board

@Dao
interface BoardDao {

    @Query("select * from Board where id = :id")
    fun findById(id: Int): Board?

    @Query("select * from Board where gameId = :gameId and playerId = :playerId")
    fun findByGameAndPlayer(gameId: Long, playerId: Long): Board?

    @Insert
    fun insert(board: Board): Long
}
