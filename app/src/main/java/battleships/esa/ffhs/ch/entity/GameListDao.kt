package battleships.esa.ffhs.ch.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GameListDao {
    // Dao = Data Access Object
    private val gameList = mutableListOf<GameDao>()
    private val games = MutableLiveData<List<GameDao>>()

    init {
        games.value = gameList
    }

    fun addGame(game: GameDao) {
        gameList.add(0, game)                 // add at index 0 so we get a reverse sort order automatically
        games.value = gameList                      // triggers observers
    }

    fun getGames() = games as LiveData<List<GameDao>>      // do not change live data outside of this class

    fun getActiveGame(): MutableLiveData<GameDao> {                        // not sure if good idea to return game instance here
        val activeGames = games.value?.filter { game ->  game.isActive().value == true }?.toList()
        if (activeGames != null) {
            if (!activeGames.isEmpty()) {
                val foundGame = MutableLiveData<GameDao>()
                foundGame.value = activeGames.first()
                return foundGame
            }
        }
        return MutableLiveData<GameDao>()
    }
}