package ch.ffhs.esa.battleships.data.source.local.shot

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ch.ffhs.esa.battleships.data.shot.Shot

@Dao
interface ShotDao {
    @Query("select * from Shot where boardUid = :boardUid")
    fun findByBoard(boardUid: String): List<Shot>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shot: Shot)
}
