package battleships.esa.ffhs.ch.ui.viewmodel

import battleships.esa.ffhs.ch.ui.drawable.BoardPainter
import battleships.esa.ffhs.ch.entity.Cell
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.entity.Shot
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame

class BoardMineViewModel : BoardViewModel() {

    fun lateInit() {
        ships = initShips()
        setShipsRandomly()
        ships.forEach { it.hide() }
        currentShip = null
    }

    // ----------------------------- handle click action from UI -----------------------------

    override fun repeatClickCheck(pointerPosition: Cell): Boolean {
        return (shots.filter { s -> s.cell == pointerPosition }.count() > 0)
    }

    override fun identifyShip(pointerPosition: Cell): Boolean {
        return false
    }

    override fun clickAction(pointerPosition: Cell, board: BoardPainter): Boolean {
        var handled: Boolean

        var shot = Shot(pointerPosition, null)

        currentShip = findShipAtPosition(pointerPosition)

        if (currentShip != null) {
            hit(currentShip!!, shot)
            board.vibrate()
            currentShip = null
        } else {
            hit(shot)
        }
        handled = true

        if (handled) {
            randomShot()
        }

        return handled
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
        if (activeGame!!.state != GameState.ENDED) {
            var success = activeGame!!.opponentBoard.shoot(
                Shot(
                    Cell(0, 0).getRandomCell(),
                    null
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
