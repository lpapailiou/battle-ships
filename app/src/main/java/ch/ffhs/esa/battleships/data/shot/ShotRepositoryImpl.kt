package ch.ffhs.esa.battleships.data.shot

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.di.AppModule.LocalShotDataSource
import ch.ffhs.esa.battleships.di.AppModule.RemoteShotDataSource
import javax.inject.Inject

class ShotRepositoryImpl @Inject constructor(
    @LocalShotDataSource private val localShotDataSource: ShotDataSource,
    @RemoteShotDataSource private val remoteShotDataSource: ShotDataSource
) : ShotRepository {
    override suspend fun findByBoard(boardUid: String): DataResult<List<Shot>> {
        return localShotDataSource.findByBoard(boardUid)
    }

    override suspend fun insert(shot: Shot): DataResult<String> {
        if (shot.uid.isEmpty()) {
            shot.uid = "%s_%d_%d".format(shot.boardUid, shot.x, shot.y)
        }

        val result = remoteShotDataSource.insert(shot)
        if (result is DataResult.Error) {
            throw result.exception
        }

        return localShotDataSource.insert(shot)
    }

}
