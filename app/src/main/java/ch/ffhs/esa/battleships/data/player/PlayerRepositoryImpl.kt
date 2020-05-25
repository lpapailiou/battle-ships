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

    override suspend fun findByUid(uid: String): DataResult<Player> {
        return withContext(ioDispatcher) {
            return@withContext localPlayerDataSource.findByUid(uid)
        }
    }

    override suspend fun save(player: Player): DataResult<String> {
        return withContext(ioDispatcher) {
            return@withContext localPlayerDataSource.insert(player)
        }
    }

//    override suspend fun createIfNotExists(uid: String): DataResult<Long> {
//        return withContext(ioDispatcher) {
//            val result = localPlayerDataSource.findByUid(uid)
//            if (result is DataResult.Success) {
//                return@withContext DataResult.Success(result.data.id)
//            }
//            val player = Player(uid,)
//
//            return@withContext localPlayerDataSource.
//        }
//    }
}
