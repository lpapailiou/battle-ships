package battleships.esa.ffhs.ch.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import battleships.esa.ffhs.ch.entity.GameDao
import battleships.esa.ffhs.ch.entity.GameRepository

class GameListViewModel (private val gameRepository: GameRepository): ViewModel() {

    fun getGames() = gameRepository.getGames()

    private fun getActiveGame() = gameRepository.getActiveGame()

    fun hasActiveGame() = gameRepository.hasActiveGame()

    fun addGame(game: GameDao) = gameRepository.addGame(game)

    fun getGameViewModel(): GameViewModel {
        val game = getActiveGame()
        var gameToReturn = MutableLiveData<GameDao>()
        if (game.value == null) {
            val newGame = GameDao()
            addGame(newGame)
            gameToReturn.value = newGame
        } else {
            gameToReturn.value = game.value!!
        }
        return GameViewModel(gameToReturn)
    }

    fun setGameActive(game: GameDao?) {
        val currentGame = getActiveGame().value
        if (currentGame != null) {
            currentGame.setActive(false)
        }
        if (game != null) {
            game.setActive(true)
        }
    }
}