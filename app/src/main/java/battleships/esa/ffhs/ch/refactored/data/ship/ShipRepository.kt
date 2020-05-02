package battleships.esa.ffhs.ch.refactored.data.ship

import battleships.esa.ffhs.ch.refactored.data.DataResult

interface ShipRepository {

    suspend fun insert(ship: Ship): DataResult<Long>
}
