package battleships.esa.ffhs.ch.refactored.data.shot

import battleships.esa.ffhs.ch.refactored.data.DataResult

interface ShotRepository {
    suspend fun findByBoard(boardId: Long): DataResult<List<Shot>>

    suspend fun insert(shot: Shot): DataResult<Long>
}