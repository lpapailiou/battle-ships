//package ch.ffhs.esa.battleships.data.impl.local.game
//
//import androidx.lifecycle.LiveData
//import ch.ffhs.esa.battleships.data.game.Game
//import ch.ffhs.esa.battleships.data.ship.Ship
//import ch.ffhs.esa.battleships.data.shot.Shot
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//class GameRepositoryOld(private val gameDao: GameDao) {
//
//    // ----------------------------- game queries -----------------------------
//
//    val gameList: LiveData<List<Game>> = gameDao.getGames()
//    val activeGameList: LiveData<List<Game>> = gameDao.getActiveGames()
//    val finishedGameList: LiveData<List<Game>> = gameDao.getFinishedGames()
//
//    fun getGames(): LiveData<List<Game>> {
//        return gameList
//    }
//
//    fun getFinishedGames(): LiveData<List<Game>> {
//        return activeGameList
//    }
//
//    fun getActiveGames(): LiveData<List<Game>> {
//        return finishedGameList
//    }
//
//    fun getGame(id: Int): LiveData<Game> {
//        return gameDao.getGameById(id)
//    }
//
//    suspend fun update(game: Game) {
//        game.lastChangedAt = SimpleDateFormat("yyyy-MMd-dd HH:mm:ss").format(Date())
//        gameDao.update(game)
//    }
//
//    suspend fun insert(game: Game) {
//        // create game
//        val rowId = gameDao.insert(game)
//        val dbGame = gameDao.getGameByRowId(rowId)
//        // create boards for game
////        val rowIdOpponentBoard = gameDao.insert(Board(dbGame.id))
////        val rowIdMyBoard = gameDao.insert(Board(dbGame.id))
//
//        // create ships for boards
////        val dbBoadOpponent = gameDao.getBoardByRowId(rowIdOpponentBoard)
////        var newOpponentShips = ShipFactory(dbBoadOpponent.id).getShips()
////        for (ship in newOpponentShips) {
////            gameDao.insert(ship)
////        }
//
////        val dbBoadMine = gameDao.getBoardByRowId(rowIdMyBoard)
////        var newMineShips = ShipFactory(dbBoadMine.id).getShips()
////        for (ship in newMineShips) {
////            gameDao.insert(ship)
////        }
//    }
//
//    // ----------------------------- ship queries -----------------------------
//
//    val myShipList = gameDao.getShipsMineCurrent()
//    val opponentShipList = gameDao.getShipsOpponentCurrent()
//
//    fun getMyShips(): LiveData<List<Ship>> {
//        return myShipList
//    }
//
//    fun getOpponentShips(): LiveData<List<Ship>> {
//        return opponentShipList
//    }
//
//    suspend fun update(ship: Ship) {
//        gameDao.update(ship)
//    }
//
//    suspend fun insert(ship: Ship) {
//        gameDao.insert(ship)
//    }
//
//    // ----------------------------- shot queries -----------------------------
//
//    val myShotList = gameDao.getMyShots()
//    val opponentShotList = gameDao.getOpponentShots()
//
//    fun getMyShots(): LiveData<List<Shot>> {
//        return myShotList
//    }
//
//    fun getOpponentShots(): LiveData<List<Shot>> {
//        return opponentShotList
//    }
//
//    suspend fun update(shot: Shot) {
//        gameDao.update(shot)
//    }
//
//    suspend fun insert(shot: Shot) {
//        gameDao.insert(shot)
//    }
//}
