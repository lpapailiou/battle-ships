package battleships.esa.ffhs.ch.ui.drawable

class Point (val col: Int, val row: Int) {

    fun isValid(): Boolean {
        if (col < 0 || col >= BOARD_SIZE || row < 0 || row >= BOARD_SIZE) {
            return false
        }
        return true
    }

    fun getRandom():Point {
        return Point((0..BOARD_SIZE-1).shuffled().first(), (0..BOARD_SIZE-1).shuffled().first())
    }

    override fun equals(other: Any?): Boolean {
        var otherP: Point = (other as Point)
        return this.col == otherP.col && this.row == otherP.row
    }

    override fun hashCode(): Int {
        return (col.toString() + "" + row.toString()).toInt()
    }

    override fun toString(): String {
        return "(" + col + ", " + row + ")"
    }
}
