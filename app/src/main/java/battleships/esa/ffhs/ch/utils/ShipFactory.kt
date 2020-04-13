package battleships.esa.ffhs.ch.utils

import battleships.esa.ffhs.ch.entity.CoordinateEntity
import battleships.esa.ffhs.ch.entity.ShipEntity
import battleships.esa.ffhs.ch.model.Direction

class ShipFactory(val boardId: Int) {

    val shipSizes = intArrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
    var newShips: List<ShipEntity>

    init {
        newShips =  shipSizes.mapIndexed { index, size ->
            ShipEntity(
                index,
                boardId,
                CoordinateEntity(0, index),
                size,
                Direction.RIGHT,
                true,
                false
            )
        }.toList()
    }

    fun getShips(): List<ShipEntity> {
        return newShips
    }
}