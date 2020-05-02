package battleships.esa.ffhs.ch.refactored.business.board

import battleships.esa.ffhs.ch.refactored.data.ship.Direction
import java.util.*

class Cell(val x: Int, val y: Int) {

    fun getSurroundingCells(): HashSet<Cell> {
        val directions: Array<Direction> = Direction.values()
        return directions.map { direction ->
            Cell(
                x + direction.x,
                y + direction.y
            )
        }.toHashSet()
    }

    override fun equals(other: Any?): Boolean {
        val otherCell: Cell = (other as Cell)
        return this.x == otherCell.x && this.y == otherCell.y
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}
