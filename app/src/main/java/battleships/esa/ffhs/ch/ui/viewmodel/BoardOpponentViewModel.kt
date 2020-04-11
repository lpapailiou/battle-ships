package battleships.esa.ffhs.ch.ui.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.Cell
import battleships.esa.ffhs.ch.entity.GameInstance
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.model.GameState
import battleships.esa.ffhs.ch.entity.Shot

class BoardOpponentViewModel(activeGame: GameInstance) : BoardViewModel(activeGame) {

    init {
        ships = initShips()
        setShipsRandomly()
    }

    // ----------------------------- handle click action from UI -----------------------------

    override fun repeatClickCheck(pointerPosition: Cell): Boolean {
        return false
    }

    override fun identifyShip(pointerPosition: Cell): Boolean {
        if (currentShip != null) {
            return true
        }
        for (ship in ships) {
            if (ship.isCellOnShip(pointerPosition) && currentShip == null) {
                currentShip = ship.getShip(pointerPosition)
                offset = currentShip!!.getOffset(pointerPosition)
                return true
            }
        }
        return false
    }

    override fun clickAction(pointerPosition: Cell): Boolean {
        currentShip!!.rotate(pointerPosition)
        shipInvalidPositionValidityCheck()
        currentShip = null
        return true
    }

    override fun moveAction(pointerPosition: Cell): Boolean {
        var oldPos = currentShip!!.ship.bowCoordinate
        currentShip!!.set(pointerPosition, offset)
        if (!currentShip!!.isShipCompletelyOnBoard()) {
            currentShip!!.set(
                Cell(
                    oldPos.x,
                    oldPos.y
                )
            )
        }
        shipInvalidPositionValidityCheck()
        return true
    }

    // ----------------------------- take a shot on opponents board -----------------------------

    fun shoot(shot: Shot): Boolean {
        if (activeGame.data.state == GameState.ENDED) {
            return true
        }
        var refresh: Boolean = false
        var pointerPosition: Cell = shot.cell
        if (shots.filter { s -> s.cell == pointerPosition }.count() > 0) {
            return false
        }

        for (ship in ships) {
            if (ship.isCellOnShip(pointerPosition)) {
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
        if (refresh) {
            //TODO: make change listener to invalidate in fragment
            endGameCheck()
            return true
        }
        return false
    }

    override fun endGameCheck(): Boolean {
        val gameEnded = super.endGameCheck()
        if (gameEnded && activeGame.data.state != GameState.ENDED) {
            activeGame.data.result = 0      // game lost
            println("============END CHECK POSITIVE OPPONENT")
            activeGame.data.state = GameState.ENDED
            return true
        }
        return false
    }

    fun shipInvalidPositionValidityCheck(): Boolean {
        var changed = false
        val overlapList = getOverlappingShips()
        for (ship in ships) {
            var isPosValid = true

            if (!ship.isShipCompletelyOnBoard() || overlapList.contains(ship)) {
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
