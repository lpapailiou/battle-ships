package ch.ffhs.esa.battleships.data.board

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

    suspend fun findByGameAndPlayer(
        gameUid: String,
        playerUid: String,
        isBotGame: Boolean
    ): DataResult<Board> {
        return withContext(ioDispatcher) {
            if (isBotGame) {
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

    suspend fun saveBoard(board: Board, isBotGame: Boolean): DataResult<String> {
        return withContext(ioDispatcher) {
            if (board.uid.isEmpty()) {
                board.uid = "%s_%s".format(board.gameUid, board.playerUid)
            }

            if (!isBotGame) {
                val result = remoteBoardDataSource.insert(board)

                if (result is DataResult.Error) {

                    throw result.exception
                }
            }


            return@withContext localBoardDataSource.insert(board)
        }
    }
}
