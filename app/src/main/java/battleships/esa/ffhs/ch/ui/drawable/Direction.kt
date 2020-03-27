package battleships.esa.ffhs.ch.ui.drawable

enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    fun getNext(): Direction {
        if (this == UP) {
            return RIGHT
        } else if (this == RIGHT) {
            return DOWN
        } else if (this == DOWN) {
            return LEFT
        } else if (this == LEFT) {
            return UP
        }
        return this
    }

    fun getOpposite(): Direction {
        return this.getNext().getNext()
    }

    fun getNextX(): Int {
        var result: Int = 0
        if (this == LEFT) {
            result = -1
        } else if (this == RIGHT) {
            result = 1
        }
        return result
    }

    fun getNextY(): Int {
        var result: Int = 0
        if (this == UP) {
            result = -1
        } else if (this == DOWN) {
            result = 1
        }
        return result
    }
}
