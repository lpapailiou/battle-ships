package battleships.esa.ffhs.ch.refactored.data.player

import battleships.esa.ffhs.ch.refactored.data.DataResult

interface PlayerRepository {

    suspend fun findById(id: Long): DataResult<Player>

    suspend fun findByPlayerId(playerId: String): DataResult<Player>
}
