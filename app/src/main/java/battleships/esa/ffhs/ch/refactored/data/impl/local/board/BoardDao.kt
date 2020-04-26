package battleships.esa.ffhs.ch.refactored.data.impl.local.board

import androidx.room.Dao
import androidx.room.Query
import battleships.esa.ffhs.ch.refactored.data.board.Board

@Dao
interface BoardDao {

    @Query("select * from Board where id = :id")
    fun findById(id: Int): Board?
}
