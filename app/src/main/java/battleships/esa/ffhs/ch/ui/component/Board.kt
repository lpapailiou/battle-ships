package battleships.esa.ffhs.ch.ui.component

class Board(
    val id: Int, val ships: List<Ship> = listOf(),
    val hits: Set<Point> = setOf(), val misses: Set<Point> = setOf(),
    val otherHits: Set<Point> = setOf(), val otherMisses: Set<Point> = setOf()
) {

    val BOARD_SIZE = 14
    val activeShips = ships.filter { !it.didSink }.map { it.id }
    val sunkenShips = ships.filter { it.didSink }.map { it.id }
    val endOfGame = activeShips.isEmpty() && sunkenShips.isNotEmpty()

    val shipSizes: IntArray = intArrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)

    init {
        if (ships.isEmpty()) {
            shipSizes.forEachIndexed { index, size ->
                ships.toMutableList().add(
                    Ship(
                        index,
                        null,
                        size,
                        null,
                        setOf()
                    )
                )
            }
        }
    }
}


