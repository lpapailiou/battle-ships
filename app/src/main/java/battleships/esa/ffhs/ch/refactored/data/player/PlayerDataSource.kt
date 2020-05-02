package battleships.esa.ffhs.ch.refactored.data.player

import battleships.esa.ffhs.ch.refactored.data.DataResult

interface PlayerDataSource {
    suspend fun findByPlayerId(googlePlayerId: String): DataResult<Player>
}
