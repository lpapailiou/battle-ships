package battleships.esa.ffhs.ch.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.data.GameRepository
import battleships.esa.ffhs.ch.data.GameRoomDatabase
import battleships.esa.ffhs.ch.data.GameMockDao
import battleships.esa.ffhs.ch.data.GameMockRepository

class GameListViewModel (private val gameRepository: GameMockRepository, application: Application): AndroidViewModel(application) {
// class GameListViewModel (application: Application): AndroidViewModel(application) {    // new class signature

    private val repo: GameRepository
    val gameList: LiveData<List<GameEntity>>

    init {
        val gameDao = GameRoomDatabase.getDatabase(application).gameDao()
        repo = GameRepository(gameDao)
        gameList = repo.gameList
    }


    fun getGames() = gameRepository.getGames()

    private fun getActiveGame() = gameRepository.getActiveGame()

    fun hasActiveGame() = gameRepository.hasActiveGame()

    fun addGame(game: GameMockDao) = gameRepository.addGame(game)

    fun getGameViewModel(): GameViewModel {
        val game = getActiveGame()
        var gameToReturn = MutableLiveData<GameMockDao>()
        if (game.value == null) {
            val newGame = GameMockDao()
            addGame(newGame)
            gameToReturn.value = newGame
        } else {
            gameToReturn.value = game.value!!
        }
        return GameViewModel(gameToReturn)
    }

    fun setGameActive(game: GameMockDao?) {
        val currentGame = getActiveGame().value
        if (currentGame != null) {
            currentGame.setActive(false)
        }
        if (game != null) {
            game.setActive(true)
        }
    }
}