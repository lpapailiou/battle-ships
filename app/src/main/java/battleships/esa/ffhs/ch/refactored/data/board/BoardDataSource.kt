package battleships.esa.ffhs.ch.refactored.data.board

import battleships.esa.ffhs.ch.refactored.data.DataResult

interface BoardDataSource {
    suspend fun findById(id: Int): DataResult<Board>

    suspend fun findByGameAndPlayer(gameId: Long, playerId: Long): DataResult<Board>

    suspend fun insert(board: Board): DataResult<Long>
}
