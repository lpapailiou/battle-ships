package battleships.esa.ffhs.ch.database

import androidx.lifecycle.LiveData
import androidx.room.*
import battleships.esa.ffhs.ch.entity.BoardEntity
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.entity.ShipEntity
import battleships.esa.ffhs.ch.entity.ShotEntity

@Dao
interface GameDao {

    // ----------------------------- game queries -----------------------------

    @Query("SELECT * from game_table")
    fun getGames(): LiveData<List<GameEntity>>

    @Query("SELECT COUNT(*) from game_table")
    fun getGameCount(): Int

    @Query("SELECT * from game_table WHERE state != 3")
    fun getActiveGames(): LiveData<List<GameEntity>>

    @Query("SELECT * from game_table WHERE state = 3")
    fun getFinishedGames(): LiveData<List<GameEntity>>

    @Query("SELECT * FROM game_table WHERE isCurrentGame LIMIT 1")
    fun getCurrentGame(): LiveData<GameEntity>

    @Query("SELECT * FROM game_table WHERE game_id = :id LIMIT 1")
    fun getGameById(id: Int): LiveData<GameEntity>

    @Query("SELECT * FROM game_table WHERE ROWID = :id LIMIT 1")
    fun getGameByRowId(id: Long): GameEntity

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(game: GameEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: GameEntity): Long

    // ----------------------------- board queries -----------------------------

    @Query("SELECT * FROM board_table WHERE ROWID = :id LIMIT 1")
    fun getBoardByRowId(id: Long): BoardEntity

    @Query("SELECT * FROM board_table WHERE game_owner_id = :id AND isMine = 0 LIMIT 1")
    fun getOpponentBoardByGameId(id: Int): LiveData<BoardEntity>

    @Query("SELECT * FROM board_table WHERE game_owner_id = :id AND isMine = 1 LIMIT 1")
    fun getMyBoardByGameId(id: Int): LiveData<BoardEntity>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM board_table JOIN game_table on game_owner_id = game_id WHERE isCurrentGame = 1 AND ismine = 0 LIMIT 1")
    fun getOpponentBoardCurrent(): LiveData<BoardEntity>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM board_table JOIN game_table on game_owner_id = game_id WHERE isCurrentGame = 1 AND ismine = 1 LIMIT 1")
    fun getMyBoardCurrent(): LiveData<BoardEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(board: BoardEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(board: BoardEntity): Long

    // ----------------------------- ship queries -----------------------------

    @Query("SELECT * from ship_table WHERE ship_owner_id = :id")
    fun getShipsByBoardId(id: Int): LiveData<List<ShipEntity>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from ship_table JOIN board_table on ship_owner_id = board_id JOIN game_table on game_owner_id = game_id WHERE isCurrentGame = 1 AND isMine = 0")
    fun getShipsOpponentCurrent(): LiveData<List<ShipEntity>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from ship_table JOIN board_table on ship_owner_id = board_id JOIN game_table on game_owner_id = game_id WHERE isCurrentGame = 1 AND isMine = 1")
    fun getShipsMineCurrent(): LiveData<List<ShipEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(ship: ShipEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ship: ShipEntity): Long

    // ----------------------------- shot queries -----------------------------

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from shot_table JOIN board_table on shot_board_owner_id = board_id JOIN game_table on game_owner_id = game_id WHERE isCurrentGame = 1 AND isMine = 0")
    fun getOpponentShots(): LiveData<List<ShotEntity>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from shot_table JOIN board_table on shot_board_owner_id = board_id JOIN game_table on game_owner_id = game_id WHERE isCurrentGame = 1 AND isMine = 1")
    fun getMyShots(): LiveData<List<ShotEntity>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from shot_table JOIN board_table on shot_board_owner_id = board_id JOIN game_table on game_owner_id = game_id JOIN ship_table on ship_owner_id = game_id WHERE isCurrentGame = 1 AND isMine = 0 AND shot_ship_owner_id = :id")
    fun getOpponentShotsforShipId(id: Int): LiveData<List<ShotEntity>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from shot_table JOIN board_table on shot_board_owner_id = board_id JOIN game_table on game_owner_id = game_id JOIN ship_table on ship_owner_id = game_id WHERE isCurrentGame = 1 AND isMine = 1 AND shot_ship_owner_id = :id")
    fun getMyShotsforShipId(id: Int): LiveData<List<ShotEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(shot: ShotEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shot: ShotEntity): Long

}