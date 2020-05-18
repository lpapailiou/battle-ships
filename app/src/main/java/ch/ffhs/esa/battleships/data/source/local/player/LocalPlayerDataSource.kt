package ch.ffhs.esa.battleships.data.source.local.player

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.DataResult.Error
import ch.ffhs.esa.battleships.data.DataResult.Success
import ch.ffhs.esa.battleships.data.player.Player
import ch.ffhs.esa.battleships.data.player.PlayerDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalPlayerDataSource internal constructor(
    private val playerDao: PlayerDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlayerDataSource {

    override suspend fun findById(id: Long): DataResult<Player> =
        withContext(ioDispatcher) {
            try {
                val player = playerDao.findById(id)
                if (player != null) {
                    return@withContext DataResult.Success(player)
                } else {
                    return@withContext DataResult.Error(Exception("Player not found"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun findByPlayerId(googlePlayerId: String): DataResult<Player> =
        withContext(ioDispatcher) {
            try {
                val player = playerDao.findByPlayerId(googlePlayerId)
                if (player != null) {
                    return@withContext Success(player)
                } else {
                    return@withContext Error(Exception("Player not found!"))
                }
            } catch (e: Exception) {
                return@withContext Error(e)
            }
        }

}
