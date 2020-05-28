package ch.ffhs.esa.battleships.data.source.remote.board

import android.util.Log
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.board.Board
import ch.ffhs.esa.battleships.data.board.BoardDataSource
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RemoteBoardDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BoardDataSource {

    val database = Firebase.database.reference

    override suspend fun findByUid(uid: String): DataResult<Board> {
        TODO("Not yet implemented")
    }

    override suspend fun findByGameAndPlayer(
        gameUid: String,
        playerUid: String
    ): DataResult<Board> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(board: Board): DataResult<String> =
        withContext(ioDispatcher) {
            if (board.uid.isEmpty()) {
                return@withContext DataResult.Error(Exception("Board does not have an Uid assigned"))
            }


            val task = database.child("board").child(board.uid).setValue(board)


            task.await()


            if (task.isSuccessful) {
                return@withContext DataResult.Success("Success")
            }


            return@withContext DataResult.Error(task.exception!!)
        }
}
