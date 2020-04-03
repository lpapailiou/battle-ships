package battleships.esa.ffhs.ch.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import battleships.esa.ffhs.ch.ui.drawable.Board
import battleships.esa.ffhs.ch.ui.drawable.Cell
import battleships.esa.ffhs.ch.ui.drawable.Direction
import battleships.esa.ffhs.ch.ui.drawable.Shot
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame

open class BoardViewModel() : ViewModel() {

    val username = MutableLiveData<String>()

    val shipSizes: IntArray = intArrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
    var ships: List<ShipViewModel> = listOf()
    var shots: MutableList<Shot> = mutableListOf()

    var currentShip: ShipViewModel? = null
    var offset = Cell(0, 0)

    // ----------------------------- game handling -----------------------------

    fun endGameCheck(): Boolean {
        var sunkenShips = ships.filter { s -> s.isSunken() }.count()
        return (sunkenShips == ships.size)
    }

    fun getGameResult(): Boolean {
        return (ships != activeGame!!.preparedShips)
    }

    fun validateStart(): Boolean {
        if (ships.filter { s -> !s.isPositionValid() }.count() > 0) {
            return false
        }
        activeGame!!.preparedShips = ships
        return true
    }

    // ----------------------------- handle click action from UI -----------------------------
    open fun releaseShip() {
        currentShip = null
        offset = Cell(0, 0)
    }

    open fun repeatClickCheck(pointerPosition: Cell): Boolean {
        return false
    }

    open fun identifyShip(pointerPosition: Cell): Boolean {
        return false
    }

    open fun clickAction(pointerPosition: Cell): Boolean {
        return false
    }

    open fun clickAction(pointerPosition: Cell, board: Board): Boolean {
        return false
    }

    open fun moveAction(pointerPosition: Cell): Boolean {
        return false
    }

    // ----------------------------- shot handling -----------------------------

    protected fun hit(shot: Shot) {
        hit(null, shot)
    }

    protected fun hit(ship: ShipViewModel?, shot: Shot) {
        if (ship != null) {
            shot.isHit(true)
            ship.hit(shot)
        }
        shots.add(shot)
    }

    fun getOverlappingShips(): HashSet<ShipViewModel> {
        val overlappingShips = hashSetOf<ShipViewModel>()
        for (ship in ships) {
            if(overlappingShips.contains(ship)) continue

            for (otherShip in ships) {
                if (ship == otherShip) continue

                if (ship.isWithinOccupiedAreaOfOtherShip(otherShip)) {
                    overlappingShips.add(ship)
                    overlappingShips.add(otherShip)
                }
            }
        }
        return overlappingShips
    }

    // ----------------------------- initialization of ships -----------------------------

    protected fun initShips(): List<ShipViewModel> {
        return shipSizes.mapIndexed { index, size ->
            ShipViewModel(
                index,
                Cell(0, index),
                size,
                Direction.RIGHT,
                mutableSetOf()
            )
        }.toList()
    }

    // TODO: unstable, should be refactored
    fun setShipsRandomly() {
        ships.forEach { s -> s.setRandomly() }
        var overlapList = getOverlappingShips()
        while (!overlapList.isEmpty()) {
            overlapList.first().setRandomly()
            overlapList = getOverlappingShips()
        }
    }
}
