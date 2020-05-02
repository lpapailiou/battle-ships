package battleships.esa.ffhs.ch.refactored.data.impl.local.shot

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import battleships.esa.ffhs.ch.refactored.data.shot.Shot

@Dao
interface ShotDao {
    @Query("select * from Shot where boardId = :boardId")
    fun findByBoard(boardId: Long): List<Shot>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shot: Shot): Long
}
