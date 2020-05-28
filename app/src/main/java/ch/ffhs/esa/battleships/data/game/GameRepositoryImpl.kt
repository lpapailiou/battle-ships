package ch.ffhs.esa.battleships.data.game

import android.util.Log
import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import ch.ffhs.esa.battleships.di.AppModule.LocalGameDataSource
import ch.ffhs.esa.battleships.di.AppModule.RemoteGameDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    @LocalGameDataSource private val localGameDataSource: GameDataSource,
    @RemoteGameDataSource private val remoteGameDataSource: GameDataSource,
    private val playerRepository: PlayerRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameRepository {

    override suspend fun findByUid(uid: String): DataResult<Game> {
        return withContext(ioDispatcher) {
            return@withContext localGameDataSource.findByUid(uid)
        }
    }

    override suspend fun save(game: Game): DataResult<String> {
        return withContext(ioDispatcher) {
            game.lastChangedAt = System.currentTimeMillis()
            if (game.uid.isEmpty()) {
                Log.i("GameRepoImpl#save", "about to create a new game!")
                val dateString = SimpleDateFormat("MMddyyyyHHmmss").format(game.lastChangedAt)
                game.uid = "%s_%s".format(game.defenderUid, dateString)
            }

            val result = remoteGameDataSource.save(game)
            if (result is DataResult.Error) {
                Log.e("GameRepo#save", "Could not save remote game!")
                throw result.exception
            }


            localGameDataSource.save(game)
            val localResult = localGameDataSource.update(game)

            if (localResult is DataResult.Error) {
                Log.e("GameRepoImpl#save", "could not save game locally")
                return@withContext localResult
            }

            return@withContext DataResult.Success(game.uid)
        }
    }


    override suspend fun findActiveGamesFromPlayer(playerUid: String): DataResult<List<GameWithPlayerInfo>> {
        return withContext(ioDispatcher) {
            if (playerUid != BOT_PLAYER_ID) {
                val remoteResult = remoteGameDataSource.findByPlayer(playerUid)

                if (remoteResult is DataResult.Error) {
                    throw remoteResult.exception
                }

                remoteResult as DataResult.Success<List<Game>>
                remoteResult.data.forEach {
                    localGameDataSource.save(it)
                }
            }

            return@withContext localGameDataSource.findActiveGames(playerUid)
        }
    }

    override suspend fun findLatestGameWithNoOpponent(ownPlayerUid: String): DataResult<Game?> {
        return withContext(ioDispatcher) {

            val result = remoteGameDataSource.findLatestGameWithNoOpponent(ownPlayerUid)
            if (result is DataResult.Success) {
                if (result.data == null) {
                    return@withContext result
                }

                val opponentUid = result.data.defenderUid!!
                playerRepository.findByUid(opponentUid) // TODO somewhat dirty. implement a cache method? Or call remote Player data source directly?
            }

            return@withContext result
        }
    }

    override suspend fun removeFromOpenGames(game: Game): DataResult<Game> {
        return withContext(ioDispatcher) {
            return@withContext remoteGameDataSource.removeFromOpenGames(game)
        }
    }
}
