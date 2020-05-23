package ch.ffhs.esa.battleships.business.shot

import ch.ffhs.esa.battleships.business.board.Cell
import java.util.*

class ShotModel(
    val id: Long = 0,
    val x: Int,
    val y: Int,
    val boardId: Long,
    val isHit: Boolean,
    var isVisible: Boolean
) {

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
