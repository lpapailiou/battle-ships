package battleships.esa.ffhs.ch.ui.drawable

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    fun getNextX(): Int {
        return x
    }
    fun getNextY(): Int {
        return y
    }

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




}
