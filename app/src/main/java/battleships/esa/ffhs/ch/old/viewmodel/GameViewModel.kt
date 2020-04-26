//package battleships.esa.ffhs.ch.old.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import battleships.esa.ffhs.ch.refactored.data.game.Game
//import battleships.esa.ffhs.ch.old.model.GameState
//
//class GameViewModel(game: LiveData<Game>) :
//    ViewModel() {    // TODO: not completely thought through yet
//
//    var data: MutableLiveData<Game> = MutableLiveData<Game>()
//    private var opponentBoard: BoardOpponentViewModel
//    private var myBoard: BoardMineViewModel
//
//    init {
//        data.value = game.value
//        opponentBoard = BoardOpponentViewModel(this)
//        myBoard = BoardMineViewModel(this)
//    }
//
//    // ----------------------------- getters and setters -----------------------------
//
//    fun getOpponentBoard(): BoardOpponentViewModel {
//        return opponentBoard
//    }
//
//    fun getMyBoard(): BoardMineViewModel {
//        return myBoard
//    }
//
//    fun equalsState(state: GameState): Boolean {
//        return data.value!!.state == state
//    }
//
//    fun notEqualsState(state: GameState): Boolean {
//        return !equalsState(state)
//    }
//
//    fun setState(state: GameState) {
//        var updatedGame = data
//        updatedGame.value!!.state = state
//        data = updatedGame
//    }
//
//    fun isMyBoardVisible(): Boolean {
//        return true;
////        return data.value!!.isMyBoardVisible
//    }
//
//    fun setMyBoardVisible(isMyBoard: Boolean) {
//        var updatedGame = data
////        updatedGame.value!!.isMyBoardVisible = isMyBoard
//        data = updatedGame
//    }
//
//    fun setActive(active: Boolean) {
//        var updatedGame = data
////        updatedGame.value!!.isCurrentGame = active
//        data = updatedGame
//    }
//
//    fun getObservableState(): LiveData<GameState> {
//        val liveState = MutableLiveData<GameState>()
//        liveState.value = data.value!!.state
//        return liveState
//    }
//
//    fun getResult(): Int {
//        return 1;
////        return data.value!!.result
//    }
//
//}
