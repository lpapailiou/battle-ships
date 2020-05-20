package ch.ffhs.esa.battleships.business.ship

import ch.ffhs.esa.battleships.data.ship.Direction

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
