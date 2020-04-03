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

    fun getNextClockwiseNondiagonalDirection(): Direction {
        return when {
            this == UP -> {
                RIGHT
            }
            this == RIGHT -> {
                DOWN
            }
            this == DOWN -> {
                LEFT
            }
            this == LEFT -> {
                UP
            }
            else -> this
        }
    }

    fun getOppositeDirection(): Direction {
        return this.getNextClockwiseNondiagonalDirection().getNextClockwiseNondiagonalDirection()
    }

    fun getRandomDirection(): Direction {
        return listOf(UP, DOWN, LEFT, RIGHT).random()
    }

}
