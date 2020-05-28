package ch.ffhs.esa.battleships.data.board

import android.util.Log
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.di.AppModule.LocalBoardDataSource
import ch.ffhs.esa.battleships.di.AppModule.RemoteBoardDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BoardRepository @Inject constructor(
    @LocalBoardDataSource private val localBoardDataSource: BoardDataSource,
    @RemoteBoardDataSource private val remoteBoardDataSource: BoardDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun findByUid(uid: String): DataResult<Board> {
        return withContext(ioDispatcher) {
            return@withContext localBoardDataSource.findByUid(uid)
        }
    }

    suspend fun findByGameAndPlayer(gameUid: String, playerUid: String): DataResult<Board> {
        return withContext(ioDispatcher) {
            return@withContext localBoardDataSource.findByGameAndPlayer(gameUid, playerUid)
        }
    }

    suspend fun saveBoard(board: Board): DataResult<String> {
        return withContext(ioDispatcher) {
            if (board.uid.isEmpty()) {
                board.uid = "%s_%s".format(board.gameUid, board.playerUid)
            }


            val result = remoteBoardDataSource.insert(board)

            if (result is DataResult.Error) {

                return@withContext result
            }


            return@withContext localBoardDataSource.insert(board)
        }
    }
}
