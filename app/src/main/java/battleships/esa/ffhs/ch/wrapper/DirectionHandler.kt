package battleships.esa.ffhs.ch.wrapper

import battleships.esa.ffhs.ch.model.Direction

class DirectionHandler(var direction: Direction) {

    fun getNextClockwiseNondiagonalDirection(): Direction {
        return when {
            direction == Direction.UP -> {
                Direction.RIGHT
            }
            direction == Direction.RIGHT -> {
                Direction.DOWN
            }
            direction == Direction.DOWN -> {
                Direction.LEFT
            }
            direction == Direction.LEFT -> {
                Direction.UP
            }
            else -> direction
        }
    }

    fun getRandomDirection(): Direction {
        direction = listOf(
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        ).random()
        return direction
    }

}
