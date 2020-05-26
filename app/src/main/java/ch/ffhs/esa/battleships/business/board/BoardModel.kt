package ch.ffhs.esa.battleships.business.board

import androidx.lifecycle.MutableLiveData
import ch.ffhs.esa.battleships.business.ship.ShipModel
import ch.ffhs.esa.battleships.business.shot.ShotModel
import java.util.*

class BoardModel(
    val uid: String?,
    var gameUid: String?,
    var playerUid: String?
) {

    val ships = MutableLiveData<MutableList<ShipModel>>()
    val shots = MutableLiveData<MutableList<ShotModel>>()

    init {
        ships.value = mutableListOf()
        shots.value = mutableListOf()
    }


    fun randomizeShipPositions() {
        ships.value!!.forEach { it.randomizePosition() }

        var invalidlyPositionedShips = getInvalidlyPositionedShips()
        while (invalidlyPositionedShips.isNotEmpty()) {
            invalidlyPositionedShips.first().randomizePosition()
            invalidlyPositionedShips = getInvalidlyPositionedShips()
        }

        updateShipPositionValidity()

    }


    private fun getInvalidlyPositionedShips(): HashSet<ShipModel> {
        val overlappingShips = getOverlappingShips()
        val shipsOutsideOfBoard = getShipsOutsideOfBoard()

        overlappingShips.addAll(shipsOutsideOfBoard)

        return overlappingShips
    }


    private fun getOverlappingShips(): HashSet<ShipModel> {
        val overlappingShips = hashSetOf<ShipModel>()
        for (ship in ships.value!!) {
            if (overlappingShips.contains(ship)) continue

            for (otherShip in ships.value!!) {
                if (ship == otherShip) continue

                if (ship.isShipWithinOccupiedRangeOfOtherShip(otherShip)) {
                    overlappingShips.add(ship)
                    overlappingShips.add(otherShip)
                }
            }
        }

        return overlappingShips
    }

    private fun getShipsOutsideOfBoard(): HashSet<ShipModel> {
        return ships.value!!.filter { ship ->
            !ship.isShipCompletelyOnBoard()
        }.toHashSet()
    }


    fun updateShipPositionValidity() {
        val invalidlyPositionedShips = getInvalidlyPositionedShips()
        ships.value!!.forEach { ship ->
            ship.isPositionValid = !invalidlyPositionedShips.contains(ship)
        }
    }

    fun isBoardInValidState(): Boolean {
        return getInvalidlyPositionedShips().isEmpty()
    }

    fun revealShips() {
        ships.value!!.forEach {
            it.isVisible = true
        }
    }
}
