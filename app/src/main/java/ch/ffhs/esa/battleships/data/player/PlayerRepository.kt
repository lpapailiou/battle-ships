package ch.ffhs.esa.battleships.data.player

import ch.ffhs.esa.battleships.data.DataResult

interface PlayerRepository {

    suspend fun findByUid(uid: String): DataResult<Player>

    suspend fun save(player: Player): DataResult<String>
}
