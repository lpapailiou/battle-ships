package ch.ffhs.esa.battleships.data.source.local.board

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ch.ffhs.esa.battleships.data.board.Board

@Dao
interface BoardDao {

    @Query("select * from Board where uid = :uid")
    fun findByUid(uid: String): Board?

    @Query(
        "select Board.* from Board inner join Player on Board.playerUid = Player.uid where Player.uid = :playerUid and Board.gameUid = :gameId"
    )
    fun findByGameAndPlayer(gameId: String, playerUid: String): Board?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(board: Board)
}
