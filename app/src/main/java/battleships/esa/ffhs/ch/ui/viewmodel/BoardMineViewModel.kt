package battleships.esa.ffhs.ch.ui.viewmodel

import android.app.Activity
import battleships.esa.ffhs.ch.data.GameMockDao
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.wrapper.Cell
import battleships.esa.ffhs.ch.wrapper.ShotWrapper

class BoardMineViewModel(activeGame: GameMockDao) : BoardViewModel(activeGame) {

    init {
        getShips().forEach { it.hide(true) }
    }

    // ----------------------------- handle click action from UI -----------------------------

    override fun repeatClickCheck(pointerPosition: Cell): Boolean {
        return (getShots().filter { s -> s.cell == pointerPosition }.count() > 0)
    }

    override fun identifyShip(pointerPosition: Cell): Boolean {
        return false
    }

    override fun clickAction(pointerPosition: Cell, act: Activity): Boolean {
        var handled: Boolean

        var shot = ShotWrapper(pointerPosition)

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
        if (gameEnded && activeGame.getState().value!! != GameState.ENDED) {
            activeGame.setResult(1)
            activeGame.setState(GameState.ENDED)
            return true
        }
        return false
    }

    fun findShipAtPosition(position: Cell): ShipViewModel? {
        for (ship in getShips()) {
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
        if (activeGame.getState().value != GameState.ENDED) {
            var success = activeGame.getOpponentBoard().value!!.shoot(
                ShotWrapper(
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
