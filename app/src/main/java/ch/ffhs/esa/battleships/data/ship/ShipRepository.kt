package ch.ffhs.esa.battleships.data.ship

import ch.ffhs.esa.battleships.data.DataResult

interface ShipRepository {

    suspend fun insert(ship: Ship): DataResult<Long>

    suspend fun findByBoard(boardId: Long): DataResult<List<Ship>>
}
