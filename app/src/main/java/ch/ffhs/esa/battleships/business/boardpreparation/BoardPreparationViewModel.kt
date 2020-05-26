package ch.ffhs.esa.battleships.business.boardpreparation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.board.BoardModel
import ch.ffhs.esa.battleships.business.board.Cell
import ch.ffhs.esa.battleships.business.game.GameState
import ch.ffhs.esa.battleships.business.ship.DirectionLogic
import ch.ffhs.esa.battleships.business.ship.ShipModel
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.board.Board
import ch.ffhs.esa.battleships.data.board.BoardRepository
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.player.Player
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import ch.ffhs.esa.battleships.data.ship.Direction
import ch.ffhs.esa.battleships.data.ship.ShipRepository
import ch.ffhs.esa.battleships.event.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BoardPreparationViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository,
    private val boardRepository: BoardRepository,
    private val shipRepository: ShipRepository,
    private val directionLogic: DirectionLogic
) : ViewModel() {

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    private val _bot = MutableLiveData<Player>()
    val bot: LiveData<Player> = _bot

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _board = MutableLiveData<BoardModel>()
    val board: LiveData<BoardModel> = _board

    private val _gameReadyEvent = MutableLiveData<Event<Game?>>()
    val gameReadyEvent: LiveData<Event<Game?>> = _gameReadyEvent

    private val botBoardModel = BoardModel(null, null, BOT_PLAYER_ID)

    private var isBotGame: Boolean = true


    fun start(ownPlayerUid: String, isBotGame: Boolean) {
        if (_player.value != null) {

            return
        }

        this.isBotGame = isBotGame

        val boardModel = BoardModel(null, null, ownPlayerUid)
        loadPlayer(ownPlayerUid)
        initializeShips(boardModel)
        boardModel.revealShips()
        _board.value = boardModel

        if (isBotGame) {
            loadBot()
            initializeShips(botBoardModel)
        }
    }

    private fun loadPlayer(uid: String) = viewModelScope.launch {
        val result = playerRepository.findByUid(uid)
        if (result is DataResult.Success) {
            _player.value = result.data
        } else if (result is DataResult.Error) {
            throw result.exception
        }
    }

    private fun loadBot() = viewModelScope.launch {
        val result = playerRepository.findByUid(BOT_PLAYER_ID)
        if (result is DataResult.Success) {
            _bot.value = result.data // TODO show error dialog
        }
    }

    private fun initializeShips(boardModel: BoardModel) {
        val ships = createShips()
        boardModel.ships.value = ships
        boardModel.randomizeShipPositions()
    }

    private fun createShips(): MutableList<ShipModel> {
//        val shipsSizes = arrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1) // TODO: Refactor?
        val shipsSizes = mutableListOf(4, 4) // TODO: Refactor?
        return shipsSizes.map { size ->
            ShipModel(
                0,
                0,
                size,
                Direction.UP,
                null,
                false,
                directionLogic
            )
        }.toMutableList()
    }

    fun getShipAt(cell: Cell): ShipModel? {
        return _board.value!!.ships.value!!.find { ship ->
            ship.getShipCells().contains(cell)
        }
    }

    fun rotateAround(ship: ShipModel, cell: Cell) {
        ship.rotateAround(cell)
        _board.value!!.updateShipPositionValidity()
        _board.value = _board.value
    }

    fun moveShipTo(ship: ShipModel, cell: Cell) {
        val oldX = ship.x
        val oldY = ship.y

        ship.x = cell.x
        ship.y = cell.y

        if (ship.isShipCompletelyOnBoard()) {
            _board.value!!.updateShipPositionValidity()
            _board.value = _board.value
            return
        }
        ship.x = oldX
        ship.y = oldY
    }

    fun startGame() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            launch {
                val game = Game(
                    System.currentTimeMillis(),
                    GameState.ACTIVE,
                    _player.value!!.uid
                )

                if (isBotGame) {
                    game.attackerUid = BOT_PLAYER_ID
                }

                game.playerAtTurnUid = _player.value!!.uid
                saveGame(game)
            }
        }
    }


    private fun saveGame(game: Game) = viewModelScope.launch {
        val result = gameRepository.save(game)
        if (result is DataResult.Success) {
            _game.value = game
            saveBoard(_board.value!!)
            if (isBotGame) {
                saveBoard(botBoardModel)
                _gameReadyEvent.value = Event(_game.value!!)
            }
            findOpenGame()
        }
    }

    private suspend fun saveBoard(boardModel: BoardModel) = withContext(Dispatchers.IO) {
        val board = Board(_game.value!!.uid, boardModel.playerUid!!)
        val result = boardRepository.saveBoard(board)
        if (result is DataResult.Success) {
            saveShipsToBoard(board.uid, boardModel.ships.value!!)
        }
    }

    private suspend fun saveShipsToBoard(boardUid: String, shipModels: MutableList<ShipModel>) =
        withContext(Dispatchers.IO) {
            shipModels
                .map { it.toShip() }
                .forEach {
                    it.boardUid = boardUid
                    shipRepository.insert(it)
                }
        }

    fun isBoardInValidState(): Boolean {
        return _board.value!!.isBoardInValidState()
    }

    fun findOpenGame() = viewModelScope.launch {
        Log.e("viewmodel", "before repo find")
        val result = gameRepository.findLatestGameWithNoOpponent(_player.value!!.uid)
        if (result is DataResult.Success) {
            val game = result.data
            game?.attackerUid = _player.value!!.uid
            _gameReadyEvent.value = Event(game)
        }
        Log.e("viewmodel", "after repo find")
    }
}
