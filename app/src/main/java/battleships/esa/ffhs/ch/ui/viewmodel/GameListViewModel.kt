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

class GameListViewModel (application: Application): AndroidViewModel(application) {


    val repository: GameRepository
    val gameList: LiveData<List<GameEntity>>

    init {
        val gameDao = GameRoomDatabase.getDatabase(application).gameDao()
        repository = GameRepository(gameDao)
        gameList = repository.gameList
    }



    fun getGames() = repository.getGames()

    fun getCurrentGame() = repository.getCurrentGame()

    fun getCurrentGameAsViewModel(): GameViewModel {
        var game: LiveData<GameEntity> = repository.getCurrentGame()
        if (game.value == null) {
            var newGame = MutableLiveData<GameEntity>()
            newGame.value = GameFactory().getGame()
            return GameViewModel(newGame, repository)
        }
        return GameViewModel(game, repository)
    }

    fun getMyShips() = repository.getMyShips()
    fun getOpponentShips() = repository.getOpponentShips()



    fun hasActiveGame() = (repository.getCurrentGame().value != null)

    fun save(game: GameEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(game)
    }

    fun save(ship: ShipEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(ship)
    }

    fun save(shot: ShotEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(shot)
    }

    fun addGame(game: GameEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(game)
    }

    fun addShip(ship: ShipEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(ship)
    }

    fun addShot(shot: ShotEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(shot)
    }

    fun setGameActive(game: GameEntity?) {
        if (game == null) {
            var tempGame = getCurrentGame()
            if (tempGame.value != null) {
                tempGame.value!!.isCurrentGame = false
            }
        } else {
            game.isCurrentGame = true
        }
    }

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

    fun printScore(game: GameEntity): String {
        return (print(game) + ": " + (game.result * WON_GAME_VALUE))
    }

    fun print(game: GameEntity): String {
        return ("" + game.game_id + ": vs. " + game.opponentName)
    }

}