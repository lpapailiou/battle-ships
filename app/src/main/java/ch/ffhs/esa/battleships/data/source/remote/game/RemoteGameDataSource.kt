package ch.ffhs.esa.battleships.data.source.remote.game

import android.util.Log
import ch.ffhs.esa.battleships.business.GAME_WITHOUT_ATTACKERS_PATH
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.DataResult.Error
import ch.ffhs.esa.battleships.data.DataResult.Success
import ch.ffhs.esa.battleships.data.game.FirebaseGame
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.game.GameDataSource
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
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


            val childUpdates = HashMap<String, Any>()
            if (game.attackerUid == null) {
                childUpdates["%s/%s".format(GAME_WITHOUT_ATTACKERS_PATH, game.uid)] = game
            } else {
                childUpdates["/player/%s/game/%s".format(game.attackerUid, game.uid)] = game
            }

            childUpdates["/player/%s/game/%s".format(game.defenderUid, game.uid)] = game

            val task = database.updateChildren(childUpdates)
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

    @ExperimentalCoroutinesApi
    override suspend fun findLatestGameWithNoOpponent(ownPlayerUid: String) = callbackFlow<Game?> {
        val callback = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("onDataChange", "before dataSnapshot")
                val games = dataSnapshot.children
                    .map { it.getValue(FirebaseGame::class.java) }
                    .filter { it!!.defenderUid != ownPlayerUid }
                    .map { it!!.toGame() }
                    .maxBy { it.lastChangedAt!! }
                Log.e("onDataChange", "before offer")
                offer(games)
                channel.close()
                Log.e("onDataChange", "after offer")

            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("onCancelled", "oi mate")
            }
        }

        Log.e("callbackflow", "before registering")
        database.child(GAME_WITHOUT_ATTACKERS_PATH)
            .addListenerForSingleValueEvent(callback)
        Log.e("callbackflow", "after registering")

        Log.e("callbackflow", "before awaitclose")
        awaitClose { Log.e("awaitClose", "inside") }
        Log.e("callbackflow", "after awaitclose")


        return@callbackFlow
    }
}
