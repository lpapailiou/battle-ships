package battleships.esa.ffhs.ch.entity

import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.drawable.BoardPainter
import battleships.esa.ffhs.ch.ui.viewmodel.BoardMineViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.BoardOtherViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel

/*
data class Game() {

}*/
class Game {
    companion object

    var opponentBoard: BoardOtherViewModel
    var myBoard: BoardMineViewModel

    var opponentBoardDrawable: BoardPainter? = null

    var preparedShips: List<ShipViewModel> = listOf()

    var isActivePlayerMe: Boolean = true

    var state = GameState.INIT

    init {
        opponentBoard =
            BoardOtherViewModel()
        myBoard = BoardMineViewModel()
    }

    fun start() {
        opponentBoard.lateInit()
        myBoard.lateInit()
    }
}