package battleships.esa.ffhs.ch.ui.drawable

import java.util.*


class Cell(val x: Int, val y: Int) {

    fun isValid(): Boolean {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
            return false
        }
        return true
    }

    fun getRandomCell(): Cell {
        return Cell((0 until BOARD_SIZE).random(), (0 until BOARD_SIZE).random())
    }

    fun getSurroundingCells(): HashSet<Cell> {
        val directions: Array<Direction> = Direction.values()
        return directions.map { direction ->
            Cell(x + direction.x, y + direction.y)
        }.toHashSet()
    }

    // ----------------------------- overriding equals, hashCode and toString -----------------------------

    override fun equals(other: Any?): Boolean {
        val otherC: Cell = (other as Cell)
        return this.x == otherC.x && this.y == otherC.y
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}
