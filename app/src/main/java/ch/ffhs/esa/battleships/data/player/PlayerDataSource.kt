package ch.ffhs.esa.battleships.data.player

import ch.ffhs.esa.battleships.data.DataResult

interface PlayerDataSource {

    suspend fun findByUid(uid: String): DataResult<Player>

    suspend fun insert(player: Player): DataResult<String>
}
