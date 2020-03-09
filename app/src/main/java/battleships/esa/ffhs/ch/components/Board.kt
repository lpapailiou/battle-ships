package battleships.esa.ffhs.ch.components

class Board (val id: Int, val owner: Owner, val ships: List<Ship> = listOf(),
             val hits: Set<Point> = setOf(), val misses: Set<Point> = setOf(),
             val otherHits: Set<Point> = setOf(), val otherMisses: Set<Point> = setOf()) {

    val size = 14
    val activeShips = ships.filter { !it.didSink }.map{ it.id }
    val sunkenShips = ships.filter { it.didSink }.map{ it.id }
    val endOfGame = activeShips.isEmpty() && sunkenShips.isNotEmpty()

    init {
        if (ships.isEmpty()) {
            ships.toMutableList().add(Ship(0, null, 4, null, setOf()))
            ships.toMutableList().add(Ship(1, null, 3, null, setOf()))
            ships.toMutableList().add(Ship(2, null, 3, null, setOf()))
            ships.toMutableList().add(Ship(3, null, 2, null, setOf()))
            ships.toMutableList().add(Ship(4, null, 2, null, setOf()))
            ships.toMutableList().add(Ship(5, null, 2, null, setOf()))
            ships.toMutableList().add(Ship(6, null, 1, null, setOf()))
            ships.toMutableList().add(Ship(7, null, 1, null, setOf()))
            ships.toMutableList().add(Ship(8, null, 1, null, setOf()))
            ships.toMutableList().add(Ship(9, null, 1, null, setOf()))
        }
    }
}


