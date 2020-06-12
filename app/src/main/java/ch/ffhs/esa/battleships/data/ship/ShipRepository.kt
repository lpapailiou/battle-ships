package ch.ffhs.esa.battleships.data.ship

import ch.ffhs.esa.battleships.data.DataResult

interface ShipRepository {

    suspend fun insert(ship: Ship, isBotGame: Boolean): DataResult<String>

    suspend fun findByBoard(boardUid: String, isBotGame: Boolean): DataResult<List<Ship>>
}
