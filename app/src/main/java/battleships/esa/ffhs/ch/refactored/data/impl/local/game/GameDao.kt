package battleships.esa.ffhs.ch.refactored.data.impl.local.game

import androidx.lifecycle.LiveData
import androidx.room.*
import battleships.esa.ffhs.ch.refactored.data.board.Board
import battleships.esa.ffhs.ch.refactored.data.game.Game
import battleships.esa.ffhs.ch.refactored.data.ship.Ship
import battleships.esa.ffhs.ch.refactored.data.shot.Shot

@Dao
interface GameDao {

    // ----------------------------- game queries -----------------------------

    @Query("SELECT * from game where id = :id")
    fun findById(id: Long): Game?

    @Query("SELECT * from game")
    fun getGames(): LiveData<List<Game>>

    @Query("SELECT COUNT(*) from game")
    fun getGameCount(): Int

    @Query("SELECT * from game WHERE state != 3")
    fun getActiveGames(): LiveData<List<Game>>

    @Query("SELECT * from game WHERE state = 3")
    fun getFinishedGames(): LiveData<List<Game>>

    @Query("SELECT * FROM game LIMIT 1")
    fun getCurrentGame(): LiveData<Game>

    @Query("SELECT * FROM game WHERE id = :id LIMIT 1")
    fun getGameById(id: Int): LiveData<Game>

    @Query("SELECT * FROM game WHERE ROWID = :id LIMIT 1")
    fun getGameByRowId(id: Long): Game

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(game: Game)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game): Long

    // ----------------------------- board queries -----------------------------

    @Query("SELECT * FROM board WHERE ROWID = :id LIMIT 1")
    fun getBoardByRowId(id: Long): Board

    @Query("SELECT * FROM board WHERE gameId = :id  LIMIT 1")
    fun getOpponentBoardByGameId(id: Int): LiveData<Board>

    @Query("SELECT * FROM board WHERE gameId = :id LIMIT 1")
    fun getMyBoardByGameId(id: Int): LiveData<Board>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM board JOIN game on gameId = game.id LIMIT 1")
    fun getOpponentBoardCurrent(): LiveData<Board>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM board JOIN game on gameId = game.id LIMIT 1")
    fun getMyBoardCurrent(): LiveData<Board>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(board: Board)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(board: Board): Long

    // ----------------------------- ship queries -----------------------------

    @Query("SELECT * from ship WHERE boardId = :id")
    fun getShipsByBoardId(id: Int): LiveData<List<Ship>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from ship JOIN board on ship.boardId = board.id JOIN game on board.gameId = game.id")
    fun getShipsOpponentCurrent(): LiveData<List<Ship>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from ship JOIN board on ship.boardId = board.id JOIN game on board.gameId = game.id")
    fun getShipsMineCurrent(): LiveData<List<Ship>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(ship: Ship)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ship: Ship): Long

    // ----------------------------- shot queries -----------------------------

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from shot JOIN board on boardId = board.id JOIN game on gameId = board.id")
    fun getOpponentShots(): LiveData<List<Shot>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from shot JOIN board on boardId = board.id JOIN game on gameId = board.id")
    fun getMyShots(): LiveData<List<Shot>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from shot JOIN board on shot.boardId = board.id JOIN game on board.gameId = game.id JOIN ship on ship.boardId = board.id where ship.id = :id")
    fun getOpponentShotsforShipId(id: Int): LiveData<List<Shot>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from shot JOIN board on shot.boardId = board.id JOIN game on board.gameId = game.id JOIN ship on ship.boardId = board.id where ship.id = :id")
    fun getMyShotsforShipId(id: Int): LiveData<List<Shot>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(shot: Shot)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shot: Shot): Long

}
