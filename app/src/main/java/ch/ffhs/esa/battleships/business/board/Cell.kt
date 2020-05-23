package ch.ffhs.esa.battleships.business.board

import ch.ffhs.esa.battleships.business.shot.ShotModel
import ch.ffhs.esa.battleships.data.ship.Direction
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
        if (other is Cell) {
            return x == other.x && y == other.y
        }
        if (other is ShotModel) {
            return x == other.x && y == other.y
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}
