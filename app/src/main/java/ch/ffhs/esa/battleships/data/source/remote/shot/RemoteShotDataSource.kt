package ch.ffhs.esa.battleships.data.source.remote.shot

import ch.ffhs.esa.battleships.business.FIREBASE_BOARD_PATH
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.shot.Shot
import ch.ffhs.esa.battleships.data.shot.ShotDataSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RemoteShotDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShotDataSource {

    val database = Firebase.database.reference

    override suspend fun findByBoard(boardUid: String): DataResult<List<Shot>> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(shot: Shot): DataResult<String> =
        withContext(ioDispatcher) {
            if (shot.uid.isEmpty()) {
                return@withContext DataResult.Error(Exception("Shot does not have an Uid assigned"))
            }

            val task = database.child(FIREBASE_BOARD_PATH).child(shot.boardUid).child("shot")
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
                    throw error.toException()
                }
            }

            database.child(FIREBASE_BOARD_PATH).child(boardUid).child("shot")
                .addValueEventListener(callback)

            awaitClose { database.removeEventListener(callback) }

        }
    }

}
