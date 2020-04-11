package battleships.esa.ffhs.ch.ui.viewmodel

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import battleships.esa.ffhs.ch.ui.drawable.BoardPainter
import battleships.esa.ffhs.ch.entity.Cell
import battleships.esa.ffhs.ch.entity.GameInstance
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.entity.Shot
import battleships.esa.ffhs.ch.ui.game.GameActiveFragment
import battleships.esa.ffhs.ch.ui.game.GameBoardMineFragment
import battleships.esa.ffhs.ch.ui.main.MainActivity

class BoardMineViewModel(activeGame: GameInstance) : BoardViewModel(activeGame) {

    init {
        ships = initShips()
        setShipsRandomly()
        ships.forEach { it.hide() }
    }

    // ----------------------------- handle click action from UI -----------------------------

    override fun repeatClickCheck(pointerPosition: Cell): Boolean {
        return (shots.filter { s -> s.cell == pointerPosition }.count() > 0)
    }

    override fun identifyShip(pointerPosition: Cell): Boolean {
        return false
    }

    override fun clickAction(pointerPosition: Cell, act: Activity): Boolean {
        var handled: Boolean

        var shot = Shot(pointerPosition)

        currentShip = findShipAtPosition(pointerPosition)

        if (currentShip != null) {
            hit(currentShip!!, shot)
            (act as MainActivity).vibrate()
            currentShip = null
        } else {
            hit(shot)
        }
        handled = true

        if (!endGameCheck() && handled) {
            randomShot()
        }

        return handled
    }

    override fun endGameCheck(): Boolean {
        val gameEnded = super.endGameCheck()
        if (gameEnded && activeGame.data.state != GameState.ENDED) {
            println("============END CHECK POSITIVE ME")
            activeGame.data.result = 1      // game won
            activeGame.data.state = GameState.ENDED
            return true
        }
        return false
    }

    fun findShipAtPosition(position: Cell): ShipViewModel? {
        for (ship in ships) {
            if (ship.isCellOnShip(position)) {

                return ship
            }
        }

        return null
    }

    override fun moveAction(pointerPosition: Cell): Boolean {
        var handled = false
        return handled
    }

    // ----------------------------- trigger shot on opponents board -----------------------------

    fun randomShot(): Boolean {
        if (activeGame.data.state != GameState.ENDED) {
            var success = activeGame.opponentBoard.shoot(
                Shot(
                    Cell(0, 0).getRandomCell()
                )
            )
            if (!success) {
                // TODO: refactor, replace with bot (and more stable version)
                randomShot()
            } else {
                return true
            }
        } else {
            return true
        }
        return false
    }


}
