package battleships.esa.ffhs.ch.components

class Board (val id: Int, val owner: Owner, val ships: List<Ship> = listOf(),
             val hits: Set<Point> = setOf(), val misses: Set<Point> = setOf(),
             val otherHits: Set<Point> = setOf(), val otherMisses: Set<Point> = setOf()) {

    val size = 14
    val activeShips = ships.filter { !it.didSink }.map{ it.id }
    val sunkenShips = ships.filter { it.didSink }.map{ it.id }
    val endOfGame = activeShips.isEmpty() && sunkenShips.isNotEmpty()



}