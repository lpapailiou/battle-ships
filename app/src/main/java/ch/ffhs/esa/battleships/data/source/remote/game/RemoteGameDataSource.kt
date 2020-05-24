package ch.ffhs.esa.battleships.data.source.remote.game

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.game.GameDataSource
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteGameDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameDataSource {

    private val database = Firebase.database

    override suspend fun findById(gameId: Long): DataResult<Game> =
        withContext(ioDispatcher) {
            TODO("Not implemented yet")
        }

    override suspend fun insert(game: Game): DataResult<Long> =
        withContext(ioDispatcher) {
            database.getReference("game").setValue(game)
            TODO()
        }

    override suspend fun update(game: Game): DataResult<Int> =
        withContext(ioDispatcher) {
            TODO("Not implemented yet")
        }

    override suspend fun findActiveGames(): DataResult<List<GameWithPlayerInfo>> =
        withContext(ioDispatcher) {
            TODO("Not implemented yet")
        }
}
