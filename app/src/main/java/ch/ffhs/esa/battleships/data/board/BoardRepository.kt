package ch.ffhs.esa.battleships.data.board

import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID
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
            if (playerUid == BOT_PLAYER_ID || playerUid == OFFLINE_PLAYER_ID) {
                return@withContext localBoardDataSource.findByGameAndPlayer(gameUid, playerUid)
            }
            val remoteResult = remoteBoardDataSource.findByGameAndPlayer(gameUid, playerUid)

            if (remoteResult is DataResult.Error) {
                return@withContext remoteResult
            }

            if (remoteResult !is DataResult.Success) {
                throw Exception("this should not happen")
            }

            val localInsert = localBoardDataSource.insert(remoteResult.data)

            if (localInsert !is DataResult.Success) {
                throw Exception("this should not happen")
            }

            return@withContext remoteResult
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
