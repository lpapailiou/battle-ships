package battleships.esa.ffhs.ch.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.database.GameRepository
import battleships.esa.ffhs.ch.database.GameRoomDatabase
import battleships.esa.ffhs.ch.entity.BoardEntity
import battleships.esa.ffhs.ch.entity.ShipEntity
import battleships.esa.ffhs.ch.entity.ShotEntity
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.model.WON_GAME_VALUE
import battleships.esa.ffhs.ch.utils.GameFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application): AndroidViewModel(application) {


    val repository: GameRepository
    val gameList: LiveData<List<GameEntity>>

    init {
            val gameDao = GameRoomDatabase.getDatabase(application).gameDao()
            repository = GameRepository(gameDao)
            gameList = repository.gameList
    }

    fun getCurrentGameAsViewModel(): GameViewModel {
        var game: LiveData<GameEntity> = repository.getCurrentGame()
        if (game.value == null) {
            var newGame = MutableLiveData<GameEntity>()
            newGame.value = GameFactory().getGame()
            println("----------------------------------------------------------- create game")
            return GameViewModel(newGame)

        }
        println("----------------------------------------------------------- get existing game")
        return GameViewModel(game)
    }

    fun setGameActive(game: GameEntity?) {
        var tempGame = getCurrentGame()
        if (tempGame.value != null) {                           // if we have another game flagge as 'current', we remove the flag
            tempGame.value!!.isCurrentGame = false
            save(tempGame.value!!)
        }
        if (game != null) {
            game.isCurrentGame = true                           // we flag the chosen game as 'current'
            save(game)
        }
    }

    // ----------------------------- getting data from repository -----------------------------

    fun getGames() = repository.getGames()      // or use gameList instead?
    fun getCurrentGame() = repository.getCurrentGame()
    fun hasActiveGame() = (repository.getCurrentGame().value != null)

    fun getMyShips() = repository.getMyShips()
    fun getMyShots() = repository.getMyShots()
    fun getOpponentShips() = repository.getOpponentShips()
    fun getOpponentShots() = repository.getOpponentShots()

    fun getShips(mine: Boolean): LiveData<List<ShipEntity>> {
        if (mine) {
            return repository.getMyShips()
        }
        return repository.getOpponentShips()
    }

    fun getShots(mine: Boolean): LiveData<List<ShotEntity>> {
        if (mine) {
            return repository.getMyShots()
        }
        return repository.getOpponentShots()
    }

    // ----------------------------- updating data in repository -----------------------------

    fun save(game: GameEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(game)
    }

    fun save(game: GameViewModel) {
        save(game.data.value!!)
    }

    fun save(ship: ShipEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(ship)
    }

    fun save(shot: ShotEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(shot)
    }

    // ----------------------------- inserting data to repository -----------------------------

    fun addGame(game: GameEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(game)
    }

    fun addShip(ship: ShipEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(ship)
    }

    fun addShot(shot: ShotEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(shot)
    }

    // ----------------------------- providing string output for views -----------------------------

    // used in bridge view (game list)
    fun printActive(game: GameEntity): String {
        var gameState: String = ""
        if (game.state == GameState.PREPARATION) {
            gameState = ", preparing"
        }
        if (game.isCurrentGame) {
            gameState = gameState + " (CURRENT)"
        }
        return (print(game) + "" + gameState)
    }

    // used in score view (game list)
    fun printScore(game: GameEntity): String {
        return (print(game) + ": " + (game.result * WON_GAME_VALUE))
    }

    private fun print(game: GameEntity): String {
        return ("" + game.lastChange + ": me vs. " + game.opponentName)
    }

}