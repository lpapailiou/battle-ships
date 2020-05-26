package ch.ffhs.esa.battleships.business.shot

import ch.ffhs.esa.battleships.business.board.Cell
import java.util.*

class ShotModel(
    val x: Int,
    val y: Int,
    val boardUid: String,
    val isHit: Boolean,
    var isVisible: Boolean
) {
    var shotUid: String = ""

    override fun equals(other: Any?): Boolean {
        if (other is Cell) {
            return other.x == x && other.y == y
        }
        if (other is ShotModel) {
            return other.x == x && other.y == y
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }
}
