package battleships.esa.ffhs.ch.refactored.data.board

import battleships.esa.ffhs.ch.refactored.data.DataResult

interface BoardDataSource {
    suspend fun findById(id: Int): DataResult<Board>
}
