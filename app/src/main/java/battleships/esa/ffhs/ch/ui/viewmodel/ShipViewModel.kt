package battleships.esa.ffhs.ch.ui.viewmodel

import battleships.esa.ffhs.ch.ui.drawable.Cell
import battleships.esa.ffhs.ch.ui.drawable.Direction
import battleships.esa.ffhs.ch.ui.drawable.Shot
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.strictOverlapRule

class ShipViewModel(
    val id: Int,
    var bowCell: Cell,
    val size: Int,
    var direction: Direction,
    val hits: MutableSet<Shot> = mutableSetOf()
) {

    private var isPositionValid = true
    private var isHidden = false
    var shipCells: MutableList<Cell> = mutableListOf<Cell>()

    init {
        updateCells()
    }

    // ----------------------------- ship body location handling -----------------------------

    fun updateCells() {
        shipCells.clear()
        shipCells.add(bowCell)
        for (i in 1 until size) {
            shipCells.add(Cell(bowCell.col + i * direction.x, bowCell.row + i * direction.y))
        }
    }

    // ----------------------------- get ship -----------------------------

    fun getShip(p: Cell): ShipViewModel? {
        if (isCellOnShip(p)) {
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
            bowCell = bowCell.getRandomCell()
            direction = direction.getRandomDirection()
            updateCells()
        } while (!isShipCompletelyOnBoard())
    }

    fun set(p: Cell) {
        bowCell = p
        updateCells()
    }

    fun set(p: Cell, offset: Cell) {
        set(Cell(p.col - offset.col, p.row - offset.row))
    }

    fun rotate(p: Cell) {
        val index = getIndex(p)

        direction = direction.getNextClockwiseNondiagonalDirection()
        bowCell = Cell(p.col - (direction.x * index), p.row - (direction.y * index))
        updateCells()
    }

    // ----------------------------- hit handling -----------------------------

    fun hit(shot: Shot) {
        hits.add(shot)
        isSunken()
    }

    fun isSunken(): Boolean {
        val isSunken = hits.size == shipCells.size
        if (isSunken) {
            hits.forEach { h -> h.undraw() }    // shots of ships get invisible (as they overlap ship)
            isHidden = false                    // ships gets visible again; drawn in red to vizualize it is completely sunk
        }
        return isSunken
    }

    // ----------------------------- positioning and check helper methods -----------------------------

    fun isShipCompletelyOnBoard(): Boolean {
        for (p in shipCells) {
            if (!p.isValid()) {
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
        throw NotImplementedError("The game mode without strict overlapping rule has not been implemented yet!")
    }

    fun isWithinOccupiedAreaOfOtherShip(otherShip: ShipViewModel): Boolean {
        val occupiedArea = otherShip.getOccupiedCells()
        val otherShipCells = otherShip.shipCells

        return occupiedArea.intersect(shipCells).isNotEmpty()
    }

    // TODO: check if can be replaced with getOffset method
    fun getIndex(p: Cell): Int {
        var pos: Cell = bowCell

        for ((index, i) in (0 until size).withIndex()) {
            if (pos.equals(p)) {
                return index
            }
            pos = Cell(pos.col + direction.x, pos.row + direction.y)
        }
        return -1
    }

    // used for more intuitive moving
    fun getOffset(p: Cell): Cell {
        return Cell(p.col - bowCell.col, p.row - bowCell.row)
    }

    // ----------------------------- generic getters and setters -----------------------------

    fun hide() {
        isHidden = true
    }

    fun isHidden(): Boolean {
        return isHidden
    }

    fun isPositionValid(): Boolean {
        return isPositionValid
    }

    fun isPositionValid(valid: Boolean) {
        isPositionValid = valid
    }

}
