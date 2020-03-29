package battleships.esa.ffhs.ch.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import battleships.esa.ffhs.ch.ui.drawable.Shot
import battleships.esa.ffhs.ch.ui.drawable.Direction
import battleships.esa.ffhs.ch.ui.drawable.Point
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame

open class BoardViewModel : ViewModel() {

    val username = MutableLiveData<String>()

    val shipSizes: IntArray = intArrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
    var ships: List<ShipViewModel> = listOf()
    var shots: MutableList<Shot> = mutableListOf()

    var currentShip: ShipViewModel? = null
    var offset = Point(0,0)

    // ----------------------------- game handling -----------------------------

    fun endGameCheck(): Boolean {
        var sunkenShips = ships.filter { s -> s.isSunk() }.count()
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
        offset = Point(0,0)
    }

    open fun repeatClickCheck(pointerPosition: Point): Boolean {
        return false
    }

    open fun identifyShip(pointerPosition: Point): Boolean {
        return false
    }

    open fun clickAction(pointerPosition: Point): Boolean {
        return false
    }

    open fun moveAction(pointerPosition: Point): Boolean {
        return false
    }

    // ----------------------------- shot handling -----------------------------

    protected fun hit (shot: Shot) {
        hit(null, shot)
    }

    protected fun hit(ship: ShipViewModel?, shot: Shot) {
        if (ship != null) {
            shot.isHit(true)
            ship!!.hit(shot)
        }
        shots.add(shot)
    }

    fun getOverlappingShips(): List<ShipViewModel> {
        val overlapList = mutableListOf<ShipViewModel>()
        for (ship in ships) {
            val shipPoints = ship.getOverlapArea()
            var doesOverlap = false

            for (otherShip in ships) {
                if (ship != otherShip) {
                    val otherShipPoints = otherShip.getPoints()
                    for (shipPoint in shipPoints) {
                        for (otherShipPoint in otherShipPoints) {
                            if (shipPoint.equals(otherShipPoint)) {
                                doesOverlap = true
                                break
                            }
                        }
                    }
                }
            }

            if (doesOverlap) {
                overlapList.add(ship)
            }
        }
        return overlapList
    }

    // ----------------------------- initialization of ships -----------------------------

    protected fun initShips(): List<ShipViewModel> {
        return shipSizes.mapIndexed { index, size ->
            ShipViewModel(
                index,
                Point(0, index),
                size,
                Direction.RIGHT,
                mutableSetOf()
            )
        }.toList()
    }

    fun setShipsRandomly() {
        ships.forEach{s -> s.setRandomly()}
        var overlapList = getOverlappingShips()
        while (!overlapList.isEmpty()) {
            overlapList.first().setRandomly()
            overlapList = getOverlappingShips()
        }
    }
}
