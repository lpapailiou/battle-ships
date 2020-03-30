package battleships.esa.ffhs.ch.ui.viewmodel

import battleships.esa.ffhs.ch.ui.drawable.Board
import battleships.esa.ffhs.ch.ui.drawable.GameState
import battleships.esa.ffhs.ch.ui.drawable.Shot
import battleships.esa.ffhs.ch.ui.drawable.Point
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame

class BoardMineViewModel : BoardViewModel() {

    fun lateInit() {
        ships = initShips()
        setShipsRandomly()
        ships.forEach {it.hide()}
        currentShip = null
    }

    // ----------------------------- handle click action from UI -----------------------------

    override fun repeatClickCheck(pointerPosition: Point): Boolean {
        return (shots.filter{s -> s.point.equals(pointerPosition)}.count() > 0)
    }

    override fun identifyShip(pointerPosition: Point): Boolean {
        return false
    }

    override fun clickAction(pointerPosition: Point, board: Board): Boolean {
        var handled = false

        var shot = Shot(pointerPosition, null)

        for (ship in ships) {
            if (ship.isHere(pointerPosition)) {
                currentShip = ship.getShip(pointerPosition)
            }
        }

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

    override fun moveAction(pointerPosition: Point): Boolean {
        var handled = false
        return handled
    }

    // ----------------------------- trigger shot on opponents board -----------------------------

    fun randomShot(): Boolean {
        if (activeGame!!.opponentBoard != null && activeGame!!.state != GameState.ENDED) {
            var success = activeGame!!.opponentBoard!!.shoot(
                Shot(
                    Point(0, 0).getRandom(),
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
