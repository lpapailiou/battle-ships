package ch.ffhs.esa.battleships.data.source.local.game

import android.util.Log
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.game.GameDataSource
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalGameDataSource internal constructor(
    private val gameDao: GameDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameDataSource {

    override suspend fun findClosedByPlayer(playerUid: String): DataResult<List<Game>> {
        TODO("Not yet implemented")
    }

    override suspend fun findByUid(uid: String): DataResult<Game> =
        withContext(ioDispatcher) {
            try {
                val game = gameDao.findByUid(uid)
                if (game != null) {
                    return@withContext DataResult.Success(game)
                } else {
                    return@withContext DataResult.Error(Exception("Game not found!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun save(game: Game): DataResult<String> =
        withContext(ioDispatcher) {
            try {
                gameDao.insert(game)
                return@withContext DataResult.Success("Success")
            } catch (e: Exception) {
                Log.e("LocalGameDataSource#save", "Game could not be saved")
                throw e
            }
        }

    override suspend fun findActiveGames(uid: String): DataResult<List<GameWithPlayerInfo>> =
        withContext(ioDispatcher) {
            try {
                val games = gameDao.findAllWithPlayerInfo(uid)
                return@withContext DataResult.Success(games)
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun findClosedGames(uid: String): DataResult<List<GameWithPlayerInfo>> =
        withContext(ioDispatcher) {
            try {
                val games = gameDao.findAllClosedWithPlayerInfo(uid)
                return@withContext DataResult.Success(games)
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun update(game: Game): DataResult<String> =
        withContext(ioDispatcher) {
            try {
                gameDao.update(game)
                Log.i("LocalGameDataSource#update", "Game updated!!!!!")
                return@withContext DataResult.Success("Success")
            } catch (e: Exception) {
                Log.e("LocalGameDataSource#update", "Game could not be updated")
                throw e
            }
        }

    override suspend fun findAllGamesByPlayer(playerUid: String): DataResult<List<Game>> {
        Log.d("implementationMissing", "=====================>>>>>>>>>>>> LocalGameDataSource.findAllGamesByPlayer() not implemented!")
        TODO("Local observation not implemented")
    }

    override suspend fun findLatestGameWithNoOpponent(ownPlayerUid: String): DataResult<Game?> {
        throw Exception("This data is not kept locally")
    }

    override suspend fun removeFromOpenGames(game: Game): DataResult<Game> {
        throw Exception("This data is not kept locally")
    }

    override suspend fun findByPlayer(playerUid: String): DataResult<List<Game>> {
        Log.d("implementationMissing", "=====================>>>>>>>>>>>> LocalGameDataSource.findByPlayer() not implemented!")
        TODO("Local observation not implemented")
    }

    override suspend fun observe(gameUid: String, playerUid: String): Flow<Game> {
        Log.d("implementationMissing", "=====================>>>>>>>>>>>> LocalGameDataSource.observe() not implemented!")
        TODO("Local observation not implemented")
    }
}
