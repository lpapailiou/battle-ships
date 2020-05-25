package ch.ffhs.esa.battleships.data.player

import ch.ffhs.esa.battleships.data.DataResult

interface PlayerRepository {

    suspend fun findById(id: Long): DataResult<Player>

    suspend fun findByPlayerId(playerId: String): DataResult<Player>
}