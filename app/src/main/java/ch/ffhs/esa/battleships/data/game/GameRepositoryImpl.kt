package ch.ffhs.esa.battleships.data.game

import android.util.Log
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.di.AppModule.LocalGameDataSource
import ch.ffhs.esa.battleships.di.AppModule.RemoteGameDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    @LocalGameDataSource private val localGameDataSource: GameDataSource,
    @RemoteGameDataSource private val remoteGameDataSource: GameDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameRepository {

    override suspend fun findByUid(uid: String): DataResult<Game> {
        return withContext(ioDispatcher) {
            return@withContext localGameDataSource.findByUid(uid)
        }
    }

    override suspend fun save(game: Game): DataResult<String> {
        return withContext(ioDispatcher) {
            var isNew = false
            if (game.uid.isEmpty()) {
                isNew = true
                val dateString = SimpleDateFormat("MMddyyyyHHmmss").format(game.lastChangedAt)
                game.uid = "%s_%s".format(game.defenderUid, dateString)
            }

            game.lastChangedAt = System.currentTimeMillis()

            val result = remoteGameDataSource.save(game)
            if (result is DataResult.Error) {
                throw result.exception
            }

            if (isNew) {
                localGameDataSource.save(game)
            } else {
                localGameDataSource.update(game)
            }

            return@withContext DataResult.Success(game.uid)
        }
    }


    override suspend fun findActiveGamesFromPlayer(playerUid: String): DataResult<List<GameWithPlayerInfo>> {
        return withContext(ioDispatcher) {
            return@withContext localGameDataSource.findActiveGames(playerUid)
        }
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override suspend fun findLatestGameWithNoOpponent(ownPlayerUid: String): DataResult<Game?> =
        withContext(ioDispatcher) {
            var data: Game? = null
            Log.e("gamerepo", "before ds call")
            remoteGameDataSource.findLatestGameWithNoOpponent(ownPlayerUid)
                .collect(object : FlowCollector<Game?> {
                    override suspend fun emit(value: Game?) {
                        Log.e("gamerepo", "inside collect -> emit")
                        data = value
                    }
                })

            Log.e("gamerepo", "before return")

            return@withContext DataResult.Success(data)
        }

}
