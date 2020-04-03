package battleships.esa.ffhs.ch.ui.drawable

import java.util.*
import kotlin.collections.HashSet


class Cell(val col: Int, val row: Int) {

    fun isValid(): Boolean {
        if (col < 0 || col >= BOARD_SIZE || row < 0 || row >= BOARD_SIZE) {
            return false
        }
        return true
    }

    fun getRandomCell(): Cell {
        return Cell((0..BOARD_SIZE - 1).random(), (0..BOARD_SIZE - 1).random())
    }

    fun getSurroundingCells(): HashSet<Cell> {
        val directions: Array<Direction> = Direction.values()
        return directions.map { direction ->
            Cell(col + direction.x, row + direction.y)
        }.toHashSet()
    }

    // ----------------------------- ovverriding equals, hashCode and toString -----------------------------

    override fun equals(other: Any?): Boolean {
        val otherP: Cell = (other as Cell)
        return this.col == otherP.col && this.row == otherP.row
    }

    override fun hashCode(): Int {
        return Objects.hash(col, row)
    }

    override fun toString(): String {
        return "(" + col + ", " + row + ")"
    }
}
