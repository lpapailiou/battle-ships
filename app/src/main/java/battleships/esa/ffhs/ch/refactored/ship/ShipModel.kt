package battleships.esa.ffhs.ch.refactored.ship

import battleships.esa.ffhs.ch.old.model.BOARD_SIZE
import battleships.esa.ffhs.ch.old.model.STRICT_OVERLAP_RULE
import battleships.esa.ffhs.ch.refactored.board.Cell
import battleships.esa.ffhs.ch.refactored.data.ship.Ship
import kotlin.math.abs
import battleships.esa.ffhs.ch.refactored.ship.Ship as IShip

class ShipModel(
    private val ship: Ship,
    private val directionLogic: DirectionLogic
) :
    IShip by ship {

    var isPositionValid: Boolean = true

    fun getShipCells(): HashSet<Cell> {
        val cells = HashSet<Cell>()
        for (i in 0 until ship.size) {
            cells.add(Cell(ship.x + ship.direction.x * i, ship.y + ship.direction.y * i))
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
        val index = abs(cell.x - ship.x) + abs(cell.y - ship.y)

        ship.direction = directionLogic.getNextClockwiseNonDiagonalDirection(ship.direction)
        ship.x = cell.x - (ship.direction.x * index)
        ship.y = cell.y - (ship.direction.y * index)
    }


}
