package battleships.esa.ffhs.ch.database

import androidx.lifecycle.LiveData
import androidx.room.*
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.entity.ShipEntity
import battleships.esa.ffhs.ch.entity.ShotEntity

@Dao
interface GameDao {

    // ----------------------------- game queries -----------------------------

    @Query("SELECT * from game_table")
    fun getGames(): LiveData<List<GameEntity>>

    @Query("SELECT * from game_table WHERE state != 3")
    fun getActiveGames(): LiveData<List<GameEntity>>

    @Query("SELECT * from game_table WHERE state = 3")
    fun getFinishedGames(): LiveData<List<GameEntity>>

    @Query("SELECT * FROM game_table WHERE isCurrentGame = 1 LIMIT 1")
    fun getCurrentGame(): LiveData<GameEntity>

    @Query("SELECT * FROM game_table WHERE game_id = :id LIMIT 1")
    fun getGameById(id: Int): LiveData<GameEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(game: GameEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: GameEntity)

    // ----------------------------- ship queries -----------------------------

    @Query("SELECT shipid, ship_coord_x, ship_coord_y, size, direction, shotCount, isPositionValid, isHidden, ship_id, ship_owner_board_id " +
            "from game_table " +
            "INNER JOIN board_table on board_id = mine_board_id " +
            "INNER JOIN ship_table on ship_owner_board_id = mine_board_id " +
            "WHERE isCurrentGame = 1")
    fun getMyShips(): LiveData<List<ShipEntity>>

    @Query("SELECT shipid, ship_coord_x, ship_coord_y, size, direction, shotCount, isPositionValid, isHidden, ship_id, ship_owner_board_id " +
            "from game_table " +
            "INNER JOIN board_table on board_id = opponent_board_id " +
            "INNER JOIN ship_table on ship_owner_board_id = opponent_board_id " +
            "WHERE isCurrentGame = 1")
    fun getOpponentShips(): LiveData<List<ShipEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(ship: ShipEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ship: ShipEntity)

    // ----------------------------- shot queries -----------------------------

    @Query("SELECT shot_coord_x, shot_coord_y, isHit, drawable, shot_id, shot_owner_board_id, owner_ship_id " +
            "from game_table " +
            "INNER JOIN board_table on board_id = mine_board_id " +
            "INNER JOIN shot_table on board_id = mine_board_id " +
            "WHERE isCurrentGame = 1")
    fun getMyShots(): LiveData<List<ShotEntity>>

    @Query("SELECT shot_coord_x, shot_coord_y, isHit, drawable, shot_id, shot_owner_board_id, owner_ship_id " +
            "from game_table " +
            "INNER JOIN board_table on board_id = mine_board_id " +
            "INNER JOIN shot_table on board_id = mine_board_id " +
            "WHERE isCurrentGame = 1 and owner_ship_id != null")
    fun getMyShipShots(): LiveData<List<ShotEntity>>

    @Query("SELECT shot_coord_x, shot_coord_y, isHit, drawable, shot_id, shot_owner_board_id, owner_ship_id " +
            "from game_table " +
            "INNER JOIN board_table on board_id = opponent_board_id " +
            "INNER JOIN shot_table on board_id = opponent_board_id " +
            "WHERE isCurrentGame = 1")
    fun getOpponentShots(): LiveData<List<ShotEntity>>

    @Query("SELECT shot_coord_x, shot_coord_y, isHit, drawable, shot_id, shot_owner_board_id, owner_ship_id " +
            "from game_table " +
            "INNER JOIN board_table on board_id = opponent_board_id " +
            "INNER JOIN shot_table on board_id = opponent_board_id " +
            "WHERE isCurrentGame = 1 and owner_ship_id != null")
    fun getOpponentShipShots(): LiveData<List<ShotEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(shot: ShotEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shot: ShotEntity)

}