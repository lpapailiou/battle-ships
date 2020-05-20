package ch.ffhs.esa.battleships.data.game

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.di.AppModule.LocalGameDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    @LocalGameDataSource private val localGameDataSource: GameDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameRepository {

    override suspend fun findById(gameId: Long): DataResult<Game> {
        return withContext(ioDispatcher) {
            return@withContext localGameDataSource.findById(gameId)
        }
    }

    override suspend fun saveGame(game: Game): DataResult<Long> {
        return withContext(ioDispatcher) {
            return@withContext localGameDataSource.insert(game)
        }
    }

    override suspend fun update(game: Game): DataResult<Int> {
        return withContext(ioDispatcher) {
            return@withContext localGameDataSource.update(game)
        }
    }

    override suspend fun findActiveGames(): DataResult<List<GameWithPlayerInfo>> {
        return withContext(ioDispatcher) {
            return@withContext localGameDataSource.findActiveGames()
        }
    }
}
