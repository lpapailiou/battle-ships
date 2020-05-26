package ch.ffhs.esa.battleships.data.game

import ch.ffhs.esa.battleships.data.DataResult

interface GameRepository {

    suspend fun findByUid(uid: String): DataResult<Game>
    suspend fun save(game: Game): DataResult<String>
    suspend fun findActiveGamesFromPlayer(playerUid: String): DataResult<List<GameWithPlayerInfo>>
}
