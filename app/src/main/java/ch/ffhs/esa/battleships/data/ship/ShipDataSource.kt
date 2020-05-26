package ch.ffhs.esa.battleships.data.ship

import ch.ffhs.esa.battleships.data.DataResult

interface ShipDataSource {
    suspend fun insert(ship: Ship): DataResult<String>

    suspend fun loadByBoard(boardUid: String): DataResult<List<Ship>>
}
