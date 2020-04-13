package battleships.esa.ffhs.ch.database

import androidx.lifecycle.LiveData
import battleships.esa.ffhs.ch.entity.BoardEntity
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.entity.ShipEntity
import battleships.esa.ffhs.ch.entity.ShotEntity
import battleships.esa.ffhs.ch.utils.ShipFactory
import java.text.SimpleDateFormat
import java.util.*


class GameRepository(private val gameDao: GameDao) {

    // ----------------------------- game queries -----------------------------

    val gameList: LiveData<List<GameEntity>> = gameDao.getGames()
    val activeGameList: LiveData<List<GameEntity>> = gameDao.getActiveGames()
    val finishedGameList: LiveData<List<GameEntity>> = gameDao.getFinishedGames()
    val currentGameEntity: LiveData<GameEntity> = gameDao.getCurrentGame()

    fun getGames(): LiveData<List<GameEntity>> {
        return gameList
    }

    fun getFinishedGames(): LiveData<List<GameEntity>> {
        return activeGameList
    }

    fun getActiveGames(): LiveData<List<GameEntity>> {
        return finishedGameList
    }

    fun getCurrentGame(): LiveData<GameEntity> {
        return currentGameEntity
    }

    fun getGame(id: Int): LiveData<GameEntity> {
        return gameDao.getGameById(id)
    }

    suspend fun update(game: GameEntity) {
        game.lastChange = SimpleDateFormat("yyyy-MMd-dd HH:mm:ss").format(Date())
        gameDao.update(game)
    }

    suspend fun insert(game: GameEntity) {
        // create game
        val rowId= gameDao.insert(game)
        val dbGame = gameDao.getGameByRowId(rowId)
        if (dbGame != null) {
            // create boards for game
            val rowIdOpponentBoard = gameDao.insert(BoardEntity(dbGame.game_id, false))
            val rowIdMyBoard = gameDao.insert(BoardEntity(dbGame.game_id, true))

            // create ships for boards
            val dbBoadOpponent = gameDao.getBoardByRowId(rowIdOpponentBoard)
            if (dbBoadOpponent != null) {
                var newOpponentShips = ShipFactory(dbBoadOpponent.board_id).getShips()
                for (ship in newOpponentShips) {
                    gameDao.insert(ship)
                }
            }

            val dbBoadMine = gameDao.getBoardByRowId(rowIdMyBoard)
            if (dbBoadMine != null) {
                var newMineShips = ShipFactory(dbBoadMine.board_id).getShips()
                for (ship in newMineShips) {
                    gameDao.insert(ship)
                }
            }
        }
    }

    // ----------------------------- ship queries -----------------------------

    val myShipList = gameDao.getShipsMineCurrent()
    val opponentShipList = gameDao.getShipsOpponentCurrent()

    fun getMyShips(): LiveData<List<ShipEntity>> {
        return myShipList
    }

    fun getOpponentShips(): LiveData<List<ShipEntity>> {
        return opponentShipList
    }

    suspend fun update(ship: ShipEntity) {
        gameDao.update(ship)
    }

    suspend fun insert(ship: ShipEntity) {
        gameDao.insert(ship)
    }

    // ----------------------------- shot queries -----------------------------

    val myShotList = gameDao.getMyShots()
    val opponentShotList = gameDao.getOpponentShots()

    fun getMyShots(): LiveData<List<ShotEntity>> {
        return myShotList
    }

    fun getOpponentShots(): LiveData<List<ShotEntity>> {
        return opponentShotList
    }

    suspend fun update(shot: ShotEntity) {
        gameDao.update(shot)
    }

    suspend fun insert(shot: ShotEntity) {
        gameDao.insert(shot)
    }
}