package battleships.esa.ffhs.ch.refactored.data.impl.local.game

import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.game.Game
import battleships.esa.ffhs.ch.refactored.data.game.GameDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalGameDataSource internal constructor(
    private val gameDao: GameDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameDataSource {

    override suspend fun findById(gameId: Long): DataResult<Game> =
        withContext(ioDispatcher) {
            try {
                val game = gameDao.findById(gameId)
                if (game != null) {
                    return@withContext DataResult.Success(game)
                } else {
                    return@withContext DataResult.Error(Exception("Game not found!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun insert(game: Game): DataResult<Long> =
        withContext(ioDispatcher) {
            try {
                val gameId = gameDao.insert(game)
                if (gameId != 0L) {
                    return@withContext DataResult.Success(gameId)
                } else {
                    return@withContext DataResult.Error(Exception("Game could not be saved!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun update(game: Game): DataResult<Int> =
        withContext(ioDispatcher) {
            try {
                val gameId = gameDao.update(game)
                if (gameId != 0) {
                    return@withContext DataResult.Success(gameId)
                } else {
                    return@withContext DataResult.Error(Exception("Game could not be saved!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun findActiveGames(): DataResult<List<Game>> =
        withContext(ioDispatcher) {
            try {
                val games = gameDao.getActiveGames()
                return@withContext DataResult.Success(games)
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }
}
