package battleships.esa.ffhs.ch.refactored.ship

import battleships.esa.ffhs.ch.old.model.STRICT_OVERLAP_RULE
import battleships.esa.ffhs.ch.refactored.board.Cell
import battleships.esa.ffhs.ch.refactored.data.ship.Ship
import javax.inject.Inject

class ShipLogic @Inject constructor(
    private val directionLogic: DirectionLogic
) {

    fun determineShipCells(ship: Ship): HashSet<Cell> {
        val cells = HashSet<Cell>()
        for (i in 0 until ship.size) {
            cells.add(Cell(ship.x + ship.direction.x * i, ship.y + ship.direction.y * i))
        }
        return cells
    }


    fun determineOccupiedCells(ship: Ship): HashSet<Cell> {
        val occupiedCells: HashSet<Cell> = linkedSetOf()
        val shipCells = determineShipCells(ship)
        occupiedCells.addAll(shipCells)
        if (STRICT_OVERLAP_RULE) {
            shipCells.forEach { shipCell ->
                occupiedCells.addAll(shipCell.getSurroundingCells())
            }
        }
        return occupiedCells
    }


    fun areShipsWithinOccupiedRangeOfEachOther(ship: Ship, otherShip: Ship): Boolean {
        val occupiedArea = determineOccupiedCells(ship)
        return occupiedArea.intersect(determineOccupiedCells(otherShip)).isNotEmpty()
    }


    fun rotateAround(ship: Ship, cell: Cell) {
        val distanceX = cell.x - ship.x
        val distanceY = cell.y - ship.y

        ship.direction = directionLogic.getNextClockwiseNonDiagonalDirection(ship.direction)

        ship.x = ship.direction.x * distanceX + cell.y
        ship.y = ship.direction.y * distanceY + cell.x
    }
}
