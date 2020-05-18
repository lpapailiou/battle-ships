package ch.ffhs.esa.battleships.data.player

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.di.AppModule.LocalPlayerDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    @LocalPlayerDataSource private val localPlayerDataSource: PlayerDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlayerRepository {

    override suspend fun findById(id: Long): DataResult<Player> {
        return withContext(ioDispatcher) {
            return@withContext localPlayerDataSource.findById(id)
        }
    }

    override suspend fun findByPlayerId(playerId: String): DataResult<Player> {
        return withContext(ioDispatcher) {
            return@withContext localPlayerDataSource.findByPlayerId(playerId)
        }
    }
}
