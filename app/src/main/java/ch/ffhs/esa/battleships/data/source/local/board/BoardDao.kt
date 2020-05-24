package ch.ffhs.esa.battleships.data.source.local.board

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ch.ffhs.esa.battleships.data.board.Board

@Dao
interface BoardDao {

    @Query("select * from Board where id = :id")
    fun findById(id: Int): Board?

    @Query(
        "select Board.* from Board inner join Player on Board.playerId = Player.id where Player.uid = :playerUID and Board.gameId = :gameId"
//        "select b.id as id, gameId, playerId from Board as b inner join Player as p on b.playerId = p.id where gameId = :gameId and p.uid = :playerUID"
    )
    fun findByGameAndPlayer(gameId: Long, playerUID: String): Board?

    @Insert
    fun insert(board: Board): Long
}
