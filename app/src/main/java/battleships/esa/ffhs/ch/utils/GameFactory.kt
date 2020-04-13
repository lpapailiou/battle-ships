package battleships.esa.ffhs.ch.utils

import battleships.esa.ffhs.ch.entity.BoardEntity
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.gameListViewModel
import java.lang.Math.random

class GameFactory() {

    var newGame: GameEntity
    var randomMyBoardId = random().toInt()
    var randomOpponentBoardId = random().toInt()

    init {
        newGame = GameEntity("today", GameState.PREPARATION, 0, BoardEntity(1), BoardEntity(2), true, true, true, "Bot")
    }

    fun getGame(): GameEntity {
        gameListViewModel.addGame(newGame)

        return newGame
    }

}