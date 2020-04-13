package battleships.esa.ffhs.ch.database

import androidx.lifecycle.LiveData
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.entity.ShipEntity
import battleships.esa.ffhs.ch.entity.ShotEntity


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
        gameDao.update(game)
    }

    suspend fun insert(game: GameEntity) {
        println("===================================================game is inserted")
        gameDao.insert(game)
    }

    // ----------------------------- ship queries -----------------------------

    val myShipList = gameDao.getMyShips()
    val opponentShipList = gameDao.getOpponentShips()

    fun getMyShips(): LiveData<List<ShipEntity>> {
        return myShipList
    }

    fun getOpponentShips(): LiveData<List<ShipEntity>> {
        return opponentShipList
    }

    suspend fun update(ship: ShipEntity) {
        println("===================================================ship is updated")
        gameDao.update(ship)
    }

    suspend fun insert(ship: ShipEntity) {
        println("===================================================ship is inserted")
        gameDao.insert(ship)
    }

    // ----------------------------- shot queries -----------------------------

    val myShotList = gameDao.getMyShots()
    val myShipShotList = gameDao.getMyShipShots()
    val opponentShotList = gameDao.getOpponentShots()
    val opponentShipShotList = gameDao.getOpponentShipShots()

    fun getMyShots(): LiveData<List<ShotEntity>> {
        return myShotList
    }

    fun getMyShipShots(): LiveData<List<ShotEntity>> {
        return myShipShotList
    }

    fun getOpponentShots(): LiveData<List<ShotEntity>> {
        return opponentShotList
    }

    fun getOpponentShipShots(): LiveData<List<ShotEntity>> {
        return opponentShipShotList
    }

    suspend fun update(shot: ShotEntity) {
        gameDao.update(shot)
    }

    suspend fun insert(shot: ShotEntity) {
        gameDao.insert(shot)
    }
}