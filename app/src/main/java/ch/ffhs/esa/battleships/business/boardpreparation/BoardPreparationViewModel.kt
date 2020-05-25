package ch.ffhs.esa.battleships.business.boardpreparation

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
import kotlinx.coroutines.launch
import java.util.*
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

    private val _gameReadyEvent = MutableLiveData<Event<Unit>>()
    val gameReadyEvent: LiveData<Event<Unit>> = _gameReadyEvent


    fun start(ownPlayerUid: String) {
        if (_player.value != null) {

            return
        }

        loadPlayer(ownPlayerUid)
        loadBot()
    }

    private fun loadPlayer(uid: String) {
        viewModelScope.launch {
            val result = playerRepository.findByUid(uid)
            if (result is DataResult.Success) {
                _player.value = result.data
                _board.value = BoardModel(null, null, result.data.uid)
                initializeShips()
            } else if (result is DataResult.Error) {
                throw result.exception
            }
        }
    }

    private fun loadBot() {
        viewModelScope.launch {
            val result = playerRepository.findByUid(BOT_PLAYER_ID)
            if (result is DataResult.Success) {
                _bot.value = result.data // TODO show error dialog
            }
        }
    }

    private fun initializeShips() {
        val ships = createShips()
        _board.value!!.ships.value = ships
        randomizeShipPositions()
        showShips()
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

    private fun randomizeShipPositions() {
        _board.value!!.ships.value!!.forEach { it.randomizePosition() }

        var invalidlyPositionedShips = _board.value!!.getInvalidlyPositionedShips()
        while (invalidlyPositionedShips.isNotEmpty()) {
            invalidlyPositionedShips.first().randomizePosition()
            invalidlyPositionedShips = _board.value!!.getInvalidlyPositionedShips()
        }

        _board.value!!.updateShipPositionValidity()
        _board.value = _board.value

    }

    private fun showShips() {
        _board.value!!.ships.value!!.forEach {
            it.isVisible = true
        }
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

    fun startGame() {
        val game = Game(
            Date.from(Calendar.getInstance().toInstant()),
            GameState.ACTIVE,
            _player.value!!.uid
        )
        game.attackerUid = _bot.value!!.uid
        game.playerAtTurnUid = _player.value!!.uid
        saveGame(game)
    }


    private fun saveGame(game: Game) = viewModelScope.launch {
        val result = gameRepository.save(game)
        if (result is DataResult.Success) {
            _game.value = game
            onGameSaved()
        }
    }

    private fun onGameSaved() {
        createOwnBoard()
        createBotPlayerBoard()
    }

    private fun createOwnBoard() {
        val board = Board(_game.value!!.uid, _player.value!!.uid)
        viewModelScope.launch {
            val result = boardRepository.saveBoard(board)
            if (result is DataResult.Success) {
                saveShipsToBoard(board.uid, false)
            }
        }
    }

    private fun createBotPlayerBoard() {
        val board = Board(_game.value!!.uid, _bot.value!!.uid)
        viewModelScope.launch {
            val result = boardRepository.saveBoard(board)
            if (result is DataResult.Success) {
                initializeShips()
                randomizeShipPositions()
                saveShipsToBoard(board.uid, true)
            }
        }
    }

    private fun saveShipsToBoard(boardUid: String, fireGameReadyEvent: Boolean) =
        viewModelScope.launch {
            val ships = _board.value!!.ships.value!!
                .map { it.toShip() }

            ships.forEach {
                it.boardUid = boardUid
                shipRepository.insert(it)
            }

            if (fireGameReadyEvent) {
                _gameReadyEvent.value = Event(Unit)
            }
        }

    fun isBoardInValidState(): Boolean {
        return _board.value!!.isBoardInValidState()
    }
}
