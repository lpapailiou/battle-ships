package battleships.esa.ffhs.ch.ui.viewmodel

import battleships.esa.ffhs.ch.ui.drawable.GameState
import battleships.esa.ffhs.ch.ui.drawable.Shot
import battleships.esa.ffhs.ch.ui.drawable.Point
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame

class BoardOtherViewModel : BoardViewModel() {

    fun lateInit() {
        if (activeGame!!.preparedShips.isEmpty()) {
            ships = initShips()
            setShipsRandomly()
        } else {
            ships = activeGame!!.preparedShips
        }
        currentShip = null
    }

    // ----------------------------- handle click action from UI -----------------------------

    override fun repeatClickCheck(pointerPosition: Point): Boolean {
        return false
    }

    override fun identifyShip(pointerPosition: Point): Boolean {
        var handled = false
        if (currentShip != null) {
            handled = true
        }
        for (ship in ships) {
            if (ship.isHere(pointerPosition) && currentShip == null) {
                currentShip = ship.getShip(pointerPosition)
                offset = currentShip!!.getOffset(pointerPosition)
                handled = true
            }
        }
        return handled
    }

    override fun clickAction(pointerPosition: Point): Boolean {
        currentShip!!.rotate(pointerPosition)
        shipInvalidPositionValidityCheck()
        currentShip = null
        return true
    }

    override fun moveAction(pointerPosition: Point): Boolean {
        var oldPos = currentShip!!.getPoint()
        currentShip!!.set(pointerPosition, offset)
        if (!currentShip!!.isShipOnBoard()) {
            currentShip!!.set(oldPos)
        }
        shipInvalidPositionValidityCheck()
        return true
    }

    // ----------------------------- take a shot on opponents board -----------------------------

    fun shoot(shot: Shot): Boolean {
        if (activeGame!!.state == GameState.ENDED) {
            return true
        }
        var refresh: Boolean = false
        var pointerPosition: Point = shot.point
        if (shots.filter{s -> s.point.equals(pointerPosition)}.count() > 0) {
            return false
        }

        for (ship in ships) {
            if (ship.isHere(pointerPosition)) {
                currentShip = ship.getShip(pointerPosition)
                if (currentShip != null) {
                    hit(currentShip!!, shot)
                    currentShip = null
                }
                refresh = true

            } else {
                hit(shot)
                refresh = true
            }


        }
        if (refresh && activeGame != null && activeGame!!.opponentBoardDrawable != null) {
            activeGame!!.opponentBoardDrawable!!.invalidate()
            activeGame!!.opponentBoardDrawable!!.endGameCheck()
            return true
        }
        return false
    }

    fun shipInvalidPositionValidityCheck(): Boolean {
        var changed = false
        var overlapList = getOverlappingShips()
        for (ship in ships) {
            var isPosValid = true

            if (!ship.isShipOnBoard() || overlapList.contains(ship)) {
                isPosValid = false
            }

            if (isPosValid != ship.isPositionValid()) {
                ship.isPositionValid(isPosValid)
                changed = true
            }
        }
        return changed
    }

}
