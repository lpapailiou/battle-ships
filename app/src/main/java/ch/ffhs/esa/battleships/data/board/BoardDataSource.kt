package ch.ffhs.esa.battleships.data.board

import ch.ffhs.esa.battleships.data.DataResult

interface BoardDataSource {
    suspend fun findById(id: Int): DataResult<Board>

    suspend fun findByGameAndPlayer(gameId: Long, playerId: Long): DataResult<Board>

    suspend fun insert(board: Board): DataResult<Long>
}
