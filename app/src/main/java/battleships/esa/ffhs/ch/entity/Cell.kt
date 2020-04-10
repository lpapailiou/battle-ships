package battleships.esa.ffhs.ch.entity

import battleships.esa.ffhs.ch.model.BOARD_SIZE
import battleships.esa.ffhs.ch.model.Coordinate
import battleships.esa.ffhs.ch.model.Direction
import java.util.*


class Cell(x: Int, y: Int) {

    val coordinate = Coordinate(x, y)

    fun isValid(): Boolean {
        if (coordinate.x < 0 || coordinate.x >= BOARD_SIZE || coordinate.y < 0 || coordinate.y >= BOARD_SIZE) {
            return false
        }
        return true
    }

    fun getRandomCell(): Cell {
        return Cell(
            (0 until BOARD_SIZE).random(),
            (0 until BOARD_SIZE).random()
        )
    }

    fun getRandomCoordinate(): Coordinate {
        return Coordinate(
            (0 until BOARD_SIZE).random(),
            (0 until BOARD_SIZE).random()
        )
    }

    fun getSurroundingCells(): HashSet<Cell> {
        val directions: Array<Direction> = Direction.values()
        return directions.map { direction ->
            Cell(
                coordinate.x + direction.x,
                coordinate.y + direction.y
            )
        }.toHashSet()
    }

    // ----------------------------- overriding equals, hashCode and toString -----------------------------

    override fun equals(other: Any?): Boolean {
        val otherC: Cell = (other as Cell)
        return this.coordinate.x == otherC.coordinate.x && this.coordinate.y == otherC.coordinate.y
    }

    override fun hashCode(): Int {
        return Objects.hash(coordinate.x, coordinate.y)
    }

    override fun toString(): String {
        return "($coordinate.x, $coordinate.y)"
    }
}
