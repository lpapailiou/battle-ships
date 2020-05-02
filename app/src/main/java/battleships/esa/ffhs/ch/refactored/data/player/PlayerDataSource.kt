package battleships.esa.ffhs.ch.refactored.data.player

import battleships.esa.ffhs.ch.refactored.data.DataResult

interface PlayerDataSource {

    suspend fun findById(id: Long): DataResult<Player>

    suspend fun findByPlayerId(googlePlayerId: String): DataResult<Player>
}
