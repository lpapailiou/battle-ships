package ch.ffhs.esa.battleships.data.shot

import ch.ffhs.esa.battleships.data.DataResult

interface ShotDataSource {

    suspend fun findByBoard(boardId: Long): DataResult<List<Shot>>

    suspend fun insert(shot: Shot): DataResult<Long>
}
