package ch.ffhs.esa.battleships.data.ship

import ch.ffhs.esa.battleships.data.DataResult

interface ShipDataSource {
    suspend fun insert(ship: Ship): DataResult<Long>

    suspend fun loadByBoard(boardId: Long): DataResult<List<Ship>>
}
