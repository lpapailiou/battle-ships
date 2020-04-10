package battleships.esa.ffhs.ch.entity

import battleships.esa.ffhs.ch.model.Game
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.drawable.BoardPainter
import battleships.esa.ffhs.ch.ui.viewmodel.BoardMineViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.BoardOpponentViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/*
data class Game() {
// TODO: create game data class

}*/
class GameInstance (gameData: Game) {

    companion object {
        var isAnyGameActive: Boolean = false
    }

    val data = gameData

    var opponentBoard: BoardOpponentViewModel
    var myBoard: BoardMineViewModel

    var opponentBoardDrawable: BoardPainter? = null

    var preparedShips: List<ShipViewModel> = listOf()
    // TODO: move many of these to data class
    var isActivePlayerMe: Boolean = true
    var connectedGame = ""  // placeholder, later to be id of connected game
    var opponent = "Bot"    // placeholder, later to be user of connected game or bot as fallback
    var result = 0          // suggestion: 1 for win, 0 for unclear, -1 for loss



    private var active: Boolean = false

    init {
        opponentBoard = BoardOpponentViewModel(this)
        myBoard = BoardMineViewModel(this)
        data.state = GameState.PREPARATION
    }

    fun isActive(): Boolean {
        return active
    }

    fun setActive(isNowActive: Boolean) {
        if (isNowActive && isAnyGameActive) {
            // TODO: get active game and set inactive
            // do not forget to check if game-to-be-deactivated is this game
        }
        active = isNowActive
    }

    fun printActive(): String {
        var gameState: String
        if (data.state == GameState.PREPARATION) {
            gameState = "(preparing)"
        } else {
            gameState = "ACTIVE"
        }
        return (toString() + " " + gameState)
    }

    fun printScore(): String {
        return (toString() + ": " + result)
    }

    // ----------------------------- overriding equals, hashCode and toString -----------------------------

    override fun equals(other: Any?): Boolean {
        val otherG: GameInstance = (other as GameInstance)
        return this.data.id == otherG.data.id
    }

    override fun hashCode(): Int {
        return Objects.hash(data.id)
    }

    override fun toString(): String {
        return ("" + data.lastChange + ": vs. " + opponent)
    }
}