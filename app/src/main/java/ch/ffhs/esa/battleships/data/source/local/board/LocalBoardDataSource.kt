package ch.ffhs.esa.battleships.data.source.local.board

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.board.Board
import ch.ffhs.esa.battleships.data.board.BoardDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalBoardDataSource internal constructor(
    private val boardDao: BoardDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BoardDataSource {

    override suspend fun findByUid(uid: String): DataResult<Board> =
        withContext(ioDispatcher) {
            try {
                val board = boardDao.findByUid(uid)
                if (board != null) {
                    return@withContext DataResult.Success(board)
                } else {
                    return@withContext DataResult.Error(Exception("Player not found!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun findByGameAndPlayer(
        gameUid: String,
        playerUid: String
    ): DataResult<Board> =
        withContext(ioDispatcher) {
            try {
                val board = boardDao.findByGameAndPlayer(gameUid, playerUid)
                if (board != null) {
                    return@withContext DataResult.Success(board)
                } else {
                    return@withContext DataResult.Error(Exception("Board not found!"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun insert(board: Board): DataResult<String> =
        withContext(ioDispatcher) {
            try {

                boardDao.insert(board)

                return@withContext DataResult.Success("Success")
            } catch (e: Exception) {

                return@withContext DataResult.Error(e)
            }
        }
}
