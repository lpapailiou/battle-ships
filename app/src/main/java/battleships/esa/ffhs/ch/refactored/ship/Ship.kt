package battleships.esa.ffhs.ch.refactored.ship

import battleships.esa.ffhs.ch.refactored.data.ship.Direction

interface Ship {
    var x: Int
    var y: Int
    val size: Int
    var direction: Direction
    val boardId: Int
    var id: Int
}
