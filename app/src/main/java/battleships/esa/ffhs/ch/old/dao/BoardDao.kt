//package battleships.esa.ffhs.ch.old.dao
//
//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//import battleships.esa.ffhs.ch.refactored.data.board.Board
//
//@Dao
//interface BoardDao {
//    @Query("SELECT * FROM board WHERE id = :id")
//    fun loadById(id: Int?): LiveData<Board>
//
//    @Insert
//    fun create(board: Board)
//}
