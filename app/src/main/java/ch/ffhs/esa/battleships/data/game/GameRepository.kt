package ch.ffhs.esa.battleships.data.game

import ch.ffhs.esa.battleships.data.DataResult
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun findByUid(uid: String): DataResult<Game>
    suspend fun save(game: Game): DataResult<String>
    suspend fun findActiveGamesFromPlayer(playerUid: String): DataResult<List<GameWithPlayerInfo>>
    suspend fun findAllGamesByPlayer(playerUid: String): DataResult<List<Game>>

    suspend fun findLatestGameWithNoOpponent(ownPlayerUid: String): DataResult<Game?>
    suspend fun removeFromOpenGames(game: Game): DataResult<Game>

    suspend fun observe(gameUid: String, playerUid: String): Flow<Game>
}
