package battleships.esa.ffhs.ch.ui.viewmodel

import androidx.lifecycle.ViewModel
import battleships.esa.ffhs.ch.entity.GameInstance
import battleships.esa.ffhs.ch.entity.GameRepository

class GameListViewModel (private val gameRepository: GameRepository): ViewModel() {

    fun getGames() = gameRepository.getGames()

    fun getActiveGame() = gameRepository.getActiveGame()

    fun addGame(game: GameInstance) = gameRepository.addGame(game)
}