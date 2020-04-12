package battleships.esa.ffhs.ch.data

class GameMockRepository private constructor (private val gameListDao: GameListMockDao) {       // mediator between viewModels and database

    companion object {
        @Volatile private var instance: GameMockRepository? = null

        fun getInstance(gameListDao: GameListMockDao) = instance
            ?: synchronized(this) {
            instance
                ?: GameMockRepository(gameListDao).also { instance = it }
        }
    }

    fun addGame(game: GameMockDao) {
        gameListDao.addGame(game)
    }

    fun getGames() = gameListDao.getGames()

    fun getActiveGame() = gameListDao.getActiveGame()

    fun hasActiveGame() = gameListDao.hasActiveGame()

}