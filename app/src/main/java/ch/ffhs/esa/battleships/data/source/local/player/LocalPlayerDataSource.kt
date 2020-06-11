package ch.ffhs.esa.battleships.data.source.local.player

import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
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

    override suspend fun findByUid(uid: String): DataResult<Player> =
        withContext(ioDispatcher) {
            try {
                val player = playerDao.findByUid(uid)
                if (player != null) {
                    return@withContext Success(player)
                } else {
                    var name = ""
                    var id = ""
                    if (uid == OFFLINE_PLAYER_ID || uid == "") {
                        name = "You"
                        id = OFFLINE_PLAYER_ID
                    } else if (uid == BOT_PLAYER_ID) {
                        name = "Bot"
                        id = BOT_PLAYER_ID
                    } else {
                        name = "Unknown"
                    }
                    val p = Player(name)
                    p.uid = id
                    insert(p)
                    return@withContext Success(p)
                    //return@withContext Error(Exception("Player not found!"))
                }
            } catch (e: Exception) {
                return@withContext Error(e)
            }
        }

    override suspend fun insert(player: Player): DataResult<String> =
        withContext(ioDispatcher) {
            try {
                playerDao.insert(player)
                return@withContext Success("Success")
            } catch (e: Exception) {
                return@withContext Error(e)
            }
        }
}
