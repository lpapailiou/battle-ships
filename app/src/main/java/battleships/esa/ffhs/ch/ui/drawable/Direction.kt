package battleships.esa.ffhs.ch.ui.drawable

enum class Direction(val tag: String, val x: Int, val y: Int) {
    UP("UP", 0, -1),
    DOWN("DOWN", 0, 1),
    LEFT("LEFT", -1, 0),
    RIGHT("RIGHT", 1, 0),

    // directions used for overlap-with-gap-checks:
    UP_RIGHT("UP_RIGHT", 1, -1),
    UP_LEFT("UP_LEFT", -1, -1),
    DOWN_RIGHT("DOWN_RIGHT", 1, 1),
    DOWN_LEFT("DOWN_LEFT", -1, 1);

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

    fun getRandom():Direction {
        var directions:List<Direction> = listOf(UP, DOWN, LEFT, RIGHT)
        return directions[(0..3).shuffled().first()]
    }

}
