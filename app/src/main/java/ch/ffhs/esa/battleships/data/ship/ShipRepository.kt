package ch.ffhs.esa.battleships.data.ship

import ch.ffhs.esa.battleships.data.DataResult

interface ShipRepository {

    suspend fun insert(ship: Ship): DataResult<String>

    suspend fun findByBoard(boardUid: String): DataResult<List<Ship>>
}
