package battleships.esa.ffhs.ch.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import battleships.esa.ffhs.ch.entity.GameEntity

@Dao
interface GameDao {

    @Query("SELECT * from game_table")
    fun getGames(): LiveData<List<GameEntity>>

    @Query("SELECT * FROM game_table WHERE isCurrentGame = 1")
    fun getCurrentGame(): List<GameEntity>

    @Query("SELECT * FROM game_table WHERE game_id == :id")
    fun getGameById(id: Int): List<GameEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: GameEntity)

}