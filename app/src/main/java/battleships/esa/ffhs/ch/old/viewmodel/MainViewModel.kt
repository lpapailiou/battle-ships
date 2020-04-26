package battleships.esa.ffhs.ch.old.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import battleships.esa.ffhs.ch.refactored.data.impl.local.game.GameRepository
import battleships.esa.ffhs.ch.refactored.data.impl.local.BattleShipsDatabase
import battleships.esa.ffhs.ch.refactored.data.game.Game
import battleships.esa.ffhs.ch.refactored.data.ship.Ship
import battleships.esa.ffhs.ch.refactored.data.shot.Shot

// this class is currently used as memory to keep track of the game list (active and finished) and to start new games
class MainViewModel(application: Application) :
    AndroidViewModel(application) {  // TODO: quite disfunctional yet (async tasks)

    val repository: GameRepository
    val gameList: LiveData<List<Game>>

    init {
        val gameDao = BattleShipsDatabase.getDatabase(application).gameDao()
        repository =
            GameRepository(
                gameDao
            )
        gameList = repository.gameList
    }

    fun setGameActive(game: Game?) {
        
        if (game != null) {

        }
    }

    // ----------------------------- getting data from repository -----------------------------

    fun getGames() = repository.getGames()      // or use gameList instead?
//    fun getCurrentGame() = repository.getCurrentGame()
//    fun hasActiveGame() = (repository.getCurrentGame().value != null)

    fun getMyShips() = repository.getMyShips()
    fun getMyShots() = repository.getMyShots()
    fun getOpponentShips() = repository.getOpponentShips()
    fun getOpponentShots() = repository.getOpponentShots()

    fun getShips(mine: Boolean): LiveData<List<Ship>> {
        if (mine) {
            return repository.getMyShips()
        }
        return repository.getOpponentShips()
    }

    fun getShots(mine: Boolean): LiveData<List<Shot>> {
        if (mine) {
            return repository.getMyShots()
        }
        return repository.getOpponentShots()
    }


}
