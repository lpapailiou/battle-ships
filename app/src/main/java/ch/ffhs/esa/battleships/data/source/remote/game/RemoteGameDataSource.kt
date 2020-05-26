package ch.ffhs.esa.battleships.data.source.remote.game

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.DataResult.Error
import ch.ffhs.esa.battleships.data.DataResult.Success
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.game.GameDataSource
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RemoteGameDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameDataSource {

    private val database = Firebase.database.reference

    override suspend fun findByUid(uid: String): DataResult<Game> =
        withContext(ioDispatcher) {
            TODO("Not implemented yet")
        }

    override suspend fun save(game: Game): DataResult<String> =
        withContext(ioDispatcher) {
            if (game.uid.isEmpty()) {
                return@withContext Error(Exception("Game does not have an Uid assigned"))
            }

            val task = database.child("game").child(game.uid).setValue(game)
            task.await()

            if (task.isSuccessful) {
                return@withContext Success("Success")
            }

            return@withContext Error(task.exception!!)

        }


    override suspend fun findActiveGames(uid: String): DataResult<List<GameWithPlayerInfo>> =
        withContext(ioDispatcher) {
            TODO("Not implemented yet")
        }

    override suspend fun update(game: Game): DataResult<String> {
        TODO("Not yet implemented")
    }
}
