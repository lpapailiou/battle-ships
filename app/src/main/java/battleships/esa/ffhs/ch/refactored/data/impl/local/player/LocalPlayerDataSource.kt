package battleships.esa.ffhs.ch.refactored.data.impl.local.player

import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.DataResult.Error
import battleships.esa.ffhs.ch.refactored.data.DataResult.Success
import battleships.esa.ffhs.ch.refactored.data.player.Player
import battleships.esa.ffhs.ch.refactored.data.player.PlayerDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalPlayerDataSource internal constructor(
    private val playerDao: PlayerDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlayerDataSource {

    override suspend fun findById(googlePlayerId: String): DataResult<Player> =
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
