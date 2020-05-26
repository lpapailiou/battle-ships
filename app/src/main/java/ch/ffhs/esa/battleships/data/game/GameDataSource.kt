package ch.ffhs.esa.battleships.data.game

import ch.ffhs.esa.battleships.data.DataResult
import kotlinx.coroutines.flow.Flow

interface GameDataSource {
    suspend fun findByUid(uid: String): DataResult<Game>

    suspend fun save(game: Game): DataResult<String>
    suspend fun findActiveGames(uid: String): DataResult<List<GameWithPlayerInfo>>
    suspend fun update(game: Game): DataResult<String>
    suspend fun findLatestGameWithNoOpponent(ownPlayerUid: String): Flow<Game?>
}
