package ch.ffhs.esa.battleships.business.ship

import ch.ffhs.esa.battleships.business.BOARD_SIZE
import ch.ffhs.esa.battleships.business.STRICT_OVERLAP_RULE
import ch.ffhs.esa.battleships.business.board.Cell
import ch.ffhs.esa.battleships.data.ship.Direction
import kotlin.math.abs

class ShipModel(
    var x: Int,
    var y: Int,
    val size: Int,
    var direction: Direction,
    var boardId: Long,
    var isVisible: Boolean,
    private val directionLogic: DirectionLogic
) {

    var isPositionValid: Boolean = true

    fun getShipCells(): HashSet<Cell> {
        val cells = HashSet<Cell>()
        for (i in 0 until size) {
            cells.add(Cell(x + direction.x * i, y + direction.y * i))
        }
        return cells
    }


    fun getOccupiedCells(): HashSet<Cell> {
        val occupiedCells: HashSet<Cell> = linkedSetOf()
        val shipCells = getShipCells()
        occupiedCells.addAll(shipCells)
        if (STRICT_OVERLAP_RULE) {
            shipCells.forEach { shipCell ->
                occupiedCells.addAll(shipCell.getSurroundingCells())
            }
        }
        return occupiedCells
    }


    fun isShipWithinOccupiedRangeOfOtherShip(otherShip: ShipModel): Boolean {
        val occupiedArea = otherShip.getOccupiedCells()
        return occupiedArea.intersect(getShipCells()).isNotEmpty()
    }


    fun isShipCompletelyOnBoard(): Boolean {
        return getShipCells().none { cell ->
            cell.x !in 0 until BOARD_SIZE || cell.y !in 0 until BOARD_SIZE
        }
    }


    fun rotateAround(cell: Cell) {
        val index = abs(cell.x - x) + abs(cell.y - y)

        direction = directionLogic.getNextClockwiseNonDiagonalDirection(direction)
        x = cell.x - (direction.x * index)
        y = cell.y - (direction.y * index)
    }


}
