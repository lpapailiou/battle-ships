package battleships.esa.ffhs.ch.refactored.ship

import battleships.esa.ffhs.ch.refactored.data.ship.Direction

class DirectionLogic {

    fun getNextClockwiseNonDiagonalDirection(direction: Direction): Direction {
        return when (direction) {
            Direction.UP -> {
                Direction.RIGHT
            }
            Direction.RIGHT -> {
                Direction.DOWN
            }
            Direction.DOWN -> {
                Direction.LEFT
            }
            Direction.LEFT -> {
                Direction.UP
            }
            else -> direction
        }
    }

    fun getRandomDirection(): Direction {
        return listOf(
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        ).random()
    }

}
