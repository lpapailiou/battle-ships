package ch.ffhs.esa.battleships.data.source.remote.board

import ch.ffhs.esa.battleships.business.FIREBASE_BOARD_PATH
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.board.Board
import ch.ffhs.esa.battleships.data.board.BoardDataSource
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

class RemoteBoardDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BoardDataSource {

    val database = Firebase.database.reference

    override suspend fun findByUid(uid: String): DataResult<Board> {
        TODO("Not yet implemented")
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override suspend fun findByGameAndPlayer(
        gameUid: String,
        playerUid: String
    ): DataResult<Board> =
        withContext(ioDispatcher) {
            val flow = callbackFlow<Board?> {
                val callback = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val board = dataSnapshot.getValue(Board::class.java)

                        offer(board)
                        channel.close()

                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                }

                database.child(FIREBASE_BOARD_PATH).child("%s_%s".format(gameUid, playerUid))
                    .addListenerForSingleValueEvent(callback)

                awaitClose { database.removeEventListener(callback) }

                return@callbackFlow
            }

            var board: Board? = null

            flow.collect(object : FlowCollector<Board?> {
                override suspend fun emit(value: Board?) {
                    board = value
                }
            })

            if (board == null) {
                return@withContext DataResult.Error(Exception("Board not found in remote data source!"))
            }

            return@withContext DataResult.Success(board!!)
        }


    override suspend fun insert(board: Board): DataResult<String> =
        withContext(ioDispatcher) {
            if (board.uid.isEmpty()) {
                return@withContext DataResult.Error(Exception("Board does not have an Uid assigned"))
            }

            val task = database.child(FIREBASE_BOARD_PATH).child(board.uid).setValue(board)
            task.await()

            if (task.isSuccessful) {
                return@withContext DataResult.Success("Success")
            }

            return@withContext DataResult.Error(task.exception!!)
        }
}
