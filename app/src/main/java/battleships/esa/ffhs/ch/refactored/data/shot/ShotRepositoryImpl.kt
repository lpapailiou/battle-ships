package battleships.esa.ffhs.ch.refactored.data.shot

import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.di.AppModule.LocalShotDataSource
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
