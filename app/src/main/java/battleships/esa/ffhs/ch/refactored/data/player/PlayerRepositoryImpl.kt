package battleships.esa.ffhs.ch.refactored.data.player

import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.di.AppModule.LocalPlayerDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    @LocalPlayerDataSource private val localPlayerDataSource: PlayerDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlayerRepository {

    override suspend fun findByPlayerId(playerId: String): DataResult<Player> {
        return withContext(ioDispatcher) {
            return@withContext localPlayerDataSource.findByPlayerId(playerId)
        }
    }
}
