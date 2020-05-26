package ch.ffhs.esa.battleships.data.source.local.player

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ch.ffhs.esa.battleships.data.player.Player

@Dao
interface PlayerDao {

    @Query("select * from Player where uid = :uid")
    fun findByUid(uid: String): Player?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(player: Player)

}
