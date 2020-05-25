package ch.ffhs.esa.battleships.data.source.local.game

import androidx.lifecycle.LiveData
import androidx.room.*
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo

@Dao
interface GameDao {

    @Query("SELECT * from game where id = :id")
    fun findById(id: Long): Game?

    @Query(
        "select g.id as gameId, attacker.name as attackerName, defender.name as defenderName, playerAtTurn.name as playerAtTurnName, g.lastChangedAt as lastChangedAt, attacker.uid as attackerUID, defender.uid as defenderUID from Game as g inner join Player as attacker on attacker.id = g.attackerId  inner join Player as defender on defender.id = g.defenderId  inner join Player as playerAtTurn on playerAtTurn.id = g.playerAtTurnId  where attacker.uid = :uid  or defender.uid = :uid  order by g.lastChangedAt desc"
    )
    fun findAllWithPlayerInfo(uid: String): List<GameWithPlayerInfo>

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
