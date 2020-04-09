package battleships.esa.ffhs.ch.ui.viewmodel

import battleships.esa.ffhs.ch.model.Coordinate
import battleships.esa.ffhs.ch.model.Ship
import battleships.esa.ffhs.ch.entity.Cell
import battleships.esa.ffhs.ch.entity.DirectionHandler
import battleships.esa.ffhs.ch.entity.Shot
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.strictOverlapRule

class ShipViewModel(val ship: Ship) {

    private var shipCells: MutableList<Cell> = mutableListOf<Cell>()

    init {
        updateCells()
    }

    // ----------------------------- ship body location handling -----------------------------

    fun updateCells() {
        shipCells.clear()
        shipCells.add(
            Cell(
                ship.bowCoordinate.x,
                ship.bowCoordinate.y
            )
        )
        for (i in 1 until ship.size) {
            shipCells.add(
                Cell(
                    ship.bowCoordinate.x + i * ship.direction.x,
                    ship.bowCoordinate.y + i * ship.direction.y
                )
            )
        }
    }

    // ----------------------------- get ship -----------------------------

    fun getShip(cell: Cell): ShipViewModel? {
        if (isCellOnShip(cell)) {
            return this
        }
        return null
    }

    fun isCellOnShip(cellToFind: Cell): Boolean {
        for (shipCell in shipCells) {
            if (cellToFind.equals(shipCell)) {
                return true
            }
        }
        return false
    }

    // ----------------------------- ship location actions -----------------------------

    // TODO: probably better to refactor
    fun setRandomly() {
        do {
            ship.bowCoordinate = Cell(0,0).getRandomCoordinate()
            ship.direction = DirectionHandler(ship.direction).getRandomDirection()
            updateCells()
        } while (!isShipCompletelyOnBoard())
    }

    fun set(cell: Cell) {
        ship.bowCoordinate = cell.coordinate
        updateCells()
    }

    fun set(cell: Cell, offset: Cell) {
        set(
            Cell(
                cell.coordinate.x - offset.coordinate.x,
                cell.coordinate.y - offset.coordinate.y
            )
        )
    }

    fun rotate(cell: Cell) {
        val index = getIndex(cell)
        ship.direction = DirectionHandler(ship.direction).getNextClockwiseNondiagonalDirection()
        ship.bowCoordinate = Coordinate(
            cell.coordinate.x - (ship.direction.x * index),
            cell.coordinate.y - (ship.direction.y * index)
        )
        updateCells()
    }

    // ----------------------------- hit handling -----------------------------

    fun hit(shot: Shot) {
        ship.hits.add(shot)
        isSunken()
    }

    fun isSunken(): Boolean {
        val isSunken = ship.hits.size == shipCells.size
        if (isSunken) {
            ship.hits.forEach { h -> h.undraw() }   // shots of ships get invisible (as they overlap ship)
            ship.isHidden =
                false                               // ships gets visible again; drawn in red to vizualize it is completely sunk
        }
        return isSunken
    }

    // ----------------------------- positioning and check helper methods -----------------------------

    fun isShipCompletelyOnBoard(): Boolean {
        for (cell in shipCells) {
            if (!cell.isValid()) {
                return false
            }
        }
        return true
    }

    /**
     * Returns all Cells which are occupied / taken by the current ship. These are all cells where
     * there must not be another ship.
     */
    fun getOccupiedCells(): HashSet<Cell> {
        if (strictOverlapRule) {
            val occupiedCells: HashSet<Cell> = linkedSetOf()
            occupiedCells.addAll(shipCells)
            shipCells.forEach { shipCell ->
                occupiedCells.addAll(shipCell.getSurroundingCells())
            }
            return occupiedCells
        }
        // TODO: can be implemented by just returning shipCells
        throw NotImplementedError("The game mode without strict overlapping rule has not been implemented yet!")
    }

    fun isWithinOccupiedAreaOfOtherShip(otherShip: ShipViewModel): Boolean {
        val occupiedArea = otherShip.getOccupiedCells()
        val otherShipCells = otherShip.shipCells        // TODO: not used?

        return occupiedArea.intersect(shipCells).isNotEmpty()
    }

    // TODO: check if can be replaced with getOffset method
    fun getIndex(cell: Cell): Int {
        var pos: Cell = Cell(ship.bowCoordinate.x, ship.bowCoordinate.y)

        for ((index, i) in (0 until ship.size).withIndex()) {
            if (pos.equals(cell)) {
                return index
            }
            pos = Cell(
                pos.coordinate.x + ship.direction.x,
                pos.coordinate.y + ship.direction.y
            )
        }
        return -1
    }

    // used for more intuitive moving
    fun getOffset(cell: Cell): Cell {
        return Cell(
            cell.coordinate.x - ship.bowCoordinate.x,
            cell.coordinate.y - ship.bowCoordinate.y
        )
    }

    // ----------------------------- generic getters and setters -----------------------------

    fun hide() {
        ship.isHidden = true
    }

    fun isHidden(): Boolean {
        return ship.isHidden
    }

    fun isPositionValid(): Boolean {
        return ship.isPositionValid
    }

    fun isPositionValid(valid: Boolean) {
        ship.isPositionValid = valid
    }

}
