package ch.ffhs.esa.battleships.data.source.local.player

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ch.ffhs.esa.battleships.data.player.Player

@Dao
interface PlayerDao {

    @Query("select * from Player where uid = :uid")
    fun findByUID(uid: String): Player?

    @Query("select * from Player where id = :id")
    fun findById(id: Long): Player?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(player: Player): Long

}
