package ch.ffhs.esa.battleships.data.source.remote.game

import ch.ffhs.esa.battleships.business.FIREBASE_GAME_WITHOUT_ATTACKERS_PATH
import ch.ffhs.esa.battleships.business.FIREBASE_PLAYER_PATH
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.DataResult.Error
import ch.ffhs.esa.battleships.data.DataResult.Success
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
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

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
                childUpdates["%s/%s".format(FIREBASE_GAME_WITHOUT_ATTACKERS_PATH, game.uid)] = game
            } else {
                childUpdates["/player/%s/game/%s".format(game.attackerUid, game.uid)] = game
            }

            childUpdates["/player/%s/game/%s".format(game.defenderUid, game.uid)] = game

            val task = database.updateChildren(childUpdates)
            task.await()

            if (task.isSuccessful) {
                return@withContext Success(game.uid)
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

    override suspend fun findAllGamesByPlayer(playerUid: String): DataResult<List<Game>> =
        withContext(ioDispatcher) {
        val flow = callbackFlow<List<Game>> {
            val callback = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    throw p0.toException()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.map { it.getValue() }
                }
            }}
            TODO()
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override suspend fun findLatestGameWithNoOpponent(ownPlayerUid: String): DataResult<Game?> =
        withContext(ioDispatcher) {
            val flow = callbackFlow<Game?> {
                val callback = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val games = dataSnapshot.children
                            .map { it.getValue(FirebaseGame::class.java) }
                            .filter { it!!.defenderUid != ownPlayerUid }
                            .map { it!!.toGame() }
                            .maxBy { it.lastChangedAt!! }

                        offer(games)
                        channel.close()
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        throw p0.toException()
                    }
                }

                database.child(FIREBASE_GAME_WITHOUT_ATTACKERS_PATH)
                    .addListenerForSingleValueEvent(callback)

                awaitClose { database.removeEventListener(callback) }

                return@callbackFlow
            }

            var game: Game? = null
            flow.collect(object : FlowCollector<Game?> {
                override suspend fun emit(value: Game?) {

                    game = value
                }
            })

            return@withContext Success(game)
        }

    override suspend fun removeFromOpenGames(game: Game): DataResult<Game> =
        withContext(ioDispatcher) {
            if (game.uid.isEmpty()) {
                return@withContext Error(Exception("Game does not have an Uid assigned"))
            }

            val task = database.child(
                FIREBASE_GAME_WITHOUT_ATTACKERS_PATH
            ).child(game.uid).removeValue()
            task.await()

            if (task.isSuccessful) {
                return@withContext Success(game)
            }

            return@withContext Error(task.exception!!)

        }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override suspend fun findByPlayer(playerUid: String): DataResult<List<Game>> =
        withContext(ioDispatcher) {
            val flow = callbackFlow<List<Game>> {
                val callback = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val games = dataSnapshot.children
                            .map { it.getValue(FirebaseGame::class.java) }
                            .map { it!!.toGame() }

                        offer(games)
                        channel.close()
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        throw p0.toException()
                    }
                }

                database.child(FIREBASE_PLAYER_PATH).child(playerUid).child("game")
                    .addListenerForSingleValueEvent(callback)

                awaitClose { database.removeEventListener(callback) }

                return@callbackFlow
            }

            var games: List<Game> = listOf()
            flow.collect(object : FlowCollector<List<Game>> {
                override suspend fun emit(value: List<Game>) {

                    games = value
                }
            })

            return@withContext Success(games)
        }
}
