package battleships.esa.ffhs.ch.utils

import battleships.esa.ffhs.ch.entity.BoardEntity
import battleships.esa.ffhs.ch.entity.GameEntity
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.mainViewModel
import java.lang.Math.random
import java.text.SimpleDateFormat
import java.util.*

class GameFactory() {

    var newGame: GameEntity

    init {
        newGame = GameEntity(
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
            GameState.PREPARATION,
            0,
            true,
            true,
            true,
            "Bot")
        mainViewModel.addGame(newGame)
    }

    fun getGame(): GameEntity {
        return newGame
    }

}