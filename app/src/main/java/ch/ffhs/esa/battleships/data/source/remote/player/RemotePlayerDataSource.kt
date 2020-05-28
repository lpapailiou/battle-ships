package ch.ffhs.esa.battleships.data.source.remote.player

import android.util.Log
import ch.ffhs.esa.battleships.business.FIREBASE_PLAYER_PATH
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.player.Player
import ch.ffhs.esa.battleships.data.player.PlayerDataSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RemotePlayerDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlayerDataSource {

    val database = Firebase.database.reference

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override suspend fun findByUid(uid: String): DataResult<Player> =
        withContext(ioDispatcher) {
            val flow = callbackFlow<Player?> {
                val callback = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val player = dataSnapshot.children
                            .map { it.getValue(Player::class.java) }
                            .firstOrNull { it!!.uid == uid }

                        offer(player)
                        channel.close()

                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                }

                database.child(FIREBASE_PLAYER_PATH).addListenerForSingleValueEvent(callback)

                awaitClose { database.removeEventListener(callback) }

                return@callbackFlow
            }

            var player: Player? = null

            flow.collect(object : FlowCollector<Player?> {
                override suspend fun emit(value: Player?) {
                    player = value
                }
            })

            if (player == null) {
                return@withContext DataResult.Error(Exception("Player not found in remote data source!"))
            }

            return@withContext DataResult.Success(player!!)
        }

    override suspend fun insert(player: Player): DataResult<String> =
        withContext(ioDispatcher) {
            val task = database.child(FIREBASE_PLAYER_PATH).child(player.uid).setValue(player)
            task.await()

            if (task.isSuccessful) {
                return@withContext DataResult.Success("Successfully created player in firebase")
            }

            return@withContext DataResult.Error(task.exception!!)
        }
}
