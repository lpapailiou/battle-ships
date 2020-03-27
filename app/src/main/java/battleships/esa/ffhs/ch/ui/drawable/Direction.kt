package battleships.esa.ffhs.ch.ui.drawable

enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    fun getNext(): Direction {
        if (this == UP) {
            println("new direction is: RIGHT")
            return RIGHT
        } else if (this == RIGHT) {
            println("new direction is: DOWN")
            return DOWN
        } else if (this == DOWN) {
            println("new direction is: LEFT")
            return LEFT
        } else if (this == LEFT) {
            println("new direction is: UP")
            return UP
        }
        return this
    }

    fun getOpposite(): Direction {
        if (this == UP) {
            return DOWN
        } else if (this == RIGHT) {
            return LEFT
        } else if (this == DOWN) {
            return UP
        } else if (this == LEFT) {
            return RIGHT
        }
        return this
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
