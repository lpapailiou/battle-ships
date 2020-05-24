package ch.ffhs.esa.battleships.data.player

import ch.ffhs.esa.battleships.data.DataResult

interface PlayerRepository {

    suspend fun findById(id: Long): DataResult<Player>

    suspend fun findByUID(uid: String): DataResult<Player>
//    suspend fun createIfNotExists(uid: String): DataResult<Long>
}
