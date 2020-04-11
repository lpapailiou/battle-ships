package battleships.esa.ffhs.ch.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GameListDao {
    // Dao = Data Access Object
    private val gameList = mutableListOf<GameInstance>()
    private val games = MutableLiveData<List<GameInstance>>()

    init {
        games.value = gameList
    }

    fun addGame(game: GameInstance) {
        gameList.add(0, game)                 // add at index 0 so we get a reverse sort order automatically
        games.value = gameList                      // triggers observers
    }

    fun getGames() = games as LiveData<List<GameInstance>>      // do not change live data outside of this class

    fun getActiveGame(): MutableLiveData<GameInstance> {                        // not sure if good idea to return game instance here
        val activeGames = games.value?.filter { game ->  game.isActive() }?.toList()
        if (activeGames != null) {
            if (!activeGames.isEmpty()) {
                val foundGame = MutableLiveData<GameInstance>()
                foundGame.value = activeGames.first()
                return foundGame
            }
        }
        return MutableLiveData<GameInstance>()
    }
}