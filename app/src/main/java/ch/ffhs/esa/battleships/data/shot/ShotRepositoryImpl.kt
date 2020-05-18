package ch.ffhs.esa.battleships.data.shot

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.di.AppModule.LocalShotDataSource
import javax.inject.Inject

class ShotRepositoryImpl @Inject constructor(
    @LocalShotDataSource private val localShotDataSource: ShotDataSource
) : ShotRepository {
    override suspend fun findByBoard(boardId: Long): DataResult<List<Shot>> {
        return localShotDataSource.findByBoard(boardId)
    }

    override suspend fun insert(shot: Shot): DataResult<Long> {
        return localShotDataSource.insert(shot)
    }

}
