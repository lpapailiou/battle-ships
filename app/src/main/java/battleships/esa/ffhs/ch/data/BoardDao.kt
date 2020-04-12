package battleships.esa.ffhs.ch.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import battleships.esa.ffhs.ch.entity.BoardEntity

@Dao
interface BoardDao {

    @Query("SELECT * from board_table")
    fun getBoards(): LiveData<List<BoardEntity>>

}