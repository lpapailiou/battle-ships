package ch.ffhs.esa.battleships.data.source.remote.shot

import android.util.Log
import ch.ffhs.esa.battleships.business.FIREBASE_BOARD_PATH
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.shot.Shot
import ch.ffhs.esa.battleships.data.shot.ShotDataSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Thread.sleep


class RemoteShotDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val firebaseDatabase: FirebaseDatabase
) : ShotDataSource {

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override suspend fun findByBoard(boardUid: String): DataResult<List<Shot>> =
        withContext(ioDispatcher) {
            val flow = callbackFlow<List<Shot>> {
                val callback = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val games = dataSnapshot.children
                            .mapNotNull { it.getValue(Shot::class.java) }


                        offer(games)
                        channel.close()
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Log.e(null, "remote shot data source cancelled - findByBoard")
                        throw p0.toException()
                    }
                }

                firebaseDatabase.reference.child(FIREBASE_BOARD_PATH).child(boardUid).child("shot")
                    .addListenerForSingleValueEvent(callback)

                awaitClose { firebaseDatabase.reference.removeEventListener(callback) }

                return@callbackFlow
            }

            var shots: List<Shot> = listOf()
            flow.collect(object : FlowCollector<List<Shot>> {
                override suspend fun emit(value: List<Shot>) {

                    shots = value
                }
            })

            return@withContext DataResult.Success(shots)
        }

    override suspend fun insert(shot: Shot): DataResult<String> =
        withContext(ioDispatcher) {
            if (shot.uid.isEmpty()) {
                return@withContext DataResult.Error(Exception("Shot does not have an Uid assigned"))
            }

            val task = firebaseDatabase.reference.child(FIREBASE_BOARD_PATH).child(shot.boardUid)
                .child("shot")
                .child(shot.uid)
                .setValue(shot)
            task.await()

            if (task.isSuccessful) {
                return@withContext DataResult.Success("Success")
            }

            return@withContext DataResult.Error(task.exception!!)
        }

    @ExperimentalCoroutinesApi
    override suspend fun observe(boardUid: String): Flow<List<Shot>> = withContext(ioDispatcher) {
        return@withContext callbackFlow {
            val callback = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val shots = dataSnapshot.children.mapNotNull { it.getValue(Shot::class.java) }

                    offer(shots)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(null, "remote shot data source cancelled - observe")
                    throw error.toException()
                }
            }

            firebaseDatabase.reference.child(FIREBASE_BOARD_PATH).child(boardUid).child("shot")
                .addValueEventListener(callback)

            awaitClose { firebaseDatabase.reference.removeEventListener(callback) }

        }
    }

}
