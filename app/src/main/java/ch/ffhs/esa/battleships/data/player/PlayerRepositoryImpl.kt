package ch.ffhs.esa.battleships.data.player

import android.util.Log
import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.di.AppModule.LocalPlayerDataSource
import ch.ffhs.esa.battleships.di.AppModule.RemotePlayerDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    @LocalPlayerDataSource private val localPlayerDataSource: PlayerDataSource,
    @RemotePlayerDataSource private val remotePlayerDataSource: PlayerDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlayerRepository {

    override suspend fun findByUid(uid: String): DataResult<Player> {
        return withContext(ioDispatcher) {

            if (uid == OFFLINE_PLAYER_ID) { //BOT_PLAYER_ID
                return@withContext localPlayerDataSource.findByUid(uid)
            }
            val findResult = remotePlayerDataSource.findByUid(uid)


            if (findResult is DataResult.Error) {
                Log.e("PlayerRepo#findByUid", "remote player not found!")
                return@withContext findResult
            }

            if (findResult !is DataResult.Success) {
                Log.e("PlayerRepo#findByUid", "wtf!")
                return@withContext findResult
            }

            val insertResult = localPlayerDataSource.insert(findResult.data)

            if (insertResult is DataResult.Error) {
                Log.e("PlayerRepo#findByUid", "local caching failed!")
                throw insertResult.exception
            }

            return@withContext findResult
        }
    }

    override suspend fun save(player: Player): DataResult<String> {
        return withContext(ioDispatcher) {

            val result = remotePlayerDataSource.insert(player)
            if (result is DataResult.Error) {
                return@withContext result
            }

            return@withContext localPlayerDataSource.insert(player)
        }
    }
}
