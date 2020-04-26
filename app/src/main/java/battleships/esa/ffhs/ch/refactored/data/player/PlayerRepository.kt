package battleships.esa.ffhs.ch.refactored.data.player

import battleships.esa.ffhs.ch.refactored.data.DataResult

interface PlayerRepository {

    suspend fun findById(playerId: String): DataResult<Player>
}
