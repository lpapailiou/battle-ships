package battleships.esa.ffhs.ch.ui.drawable

import android.app.Application
import battleships.esa.ffhs.ch.ui.viewmodel.BoardMineViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.BoardOtherViewModel
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel

class Game() {
    companion object {

    }

    var opponentBoard: BoardOtherViewModel
    var myBoard: BoardMineViewModel

    var opponentBoardDrawable: Board? = null

    var preparedShips: List<ShipViewModel> = listOf()

    var isActivePlayerMe: Boolean = true

    var state = GameState.INIT

    init {
        opponentBoard = BoardOtherViewModel()
        myBoard = BoardMineViewModel()
    }

    fun start() {
        opponentBoard.lateInit()
        myBoard.lateInit()
    }
}
