package battleships.esa.ffhs.ch.refactored.data.player

import battleships.esa.ffhs.ch.refactored.data.DataResult

interface PlayerDataSource {
    suspend fun findById(googlePlayerId: String): DataResult<Player>
}
