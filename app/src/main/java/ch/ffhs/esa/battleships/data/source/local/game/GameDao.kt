package ch.ffhs.esa.battleships.data.source.local.game

import androidx.lifecycle.LiveData
import androidx.room.*
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo

@Dao
interface GameDao {

    // ----------------------------- game queries -----------------------------

    @Query("SELECT * from game where id = :id")
    fun findById(id: Long): Game?

    @Query(
        "select g.id as gameId, p1.name as attackerName, p2.name as defenderName " +
                "from Game g " +
                "inner join Player p1 on p1.id = attackerId " +
                "inner join Player p2 on p2.id = g.defenderId"
    )
    fun findAllWithPlayerInfo(): List<GameWithPlayerInfo>

    @Query("SELECT * from game")
    fun getGames(): LiveData<List<Game>>

    @Query("SELECT COUNT(*) from game")
    fun getGameCount(): Int

    @Query("SELECT * from game WHERE state = 3")
    fun getFinishedGames(): LiveData<List<Game>>

    @Query("SELECT * FROM game LIMIT 1")
    fun getCurrentGame(): LiveData<Game>

    @Query("SELECT * FROM game WHERE id = :id LIMIT 1")
    fun getGameById(id: Int): LiveData<Game>

    @Query("SELECT * FROM game WHERE ROWID = :id LIMIT 1")
    fun getGameByRowId(id: Long): Game

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(game: Game): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game): Long

}
