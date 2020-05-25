package ch.ffhs.esa.battleships.data.player

import ch.ffhs.esa.battleships.data.DataResult

interface PlayerDataSource {

    suspend fun findById(id: Long): DataResult<Player>

    suspend fun findByUID(uid: String): DataResult<Player>
}
