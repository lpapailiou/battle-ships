package battleships.esa.ffhs.ch.entity

class GameRepository private constructor (private val gameListDao: GameListDao) {       // mediator between viewModels and database

    companion object {
        @Volatile private var instance: GameRepository? = null

        fun getInstance(gameListDao: GameListDao) = instance ?: synchronized(this) {
            instance?: GameRepository(gameListDao).also { instance = it }
        }
    }

    fun addGame(game: GameDao) {
        gameListDao.addGame(game)
    }

    fun getGames() = gameListDao.getGames()

    fun getActiveGame() = gameListDao.getActiveGame()

    fun hasActiveGame() = gameListDao.hasActiveGame()

}