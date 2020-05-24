package ch.ffhs.esa.battleships.business.boardpreparation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.business.BOARD_SIZE
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
import ch.ffhs.esa.battleships.data.ship.Ship
import ch.ffhs.esa.battleships.data.ship.ShipRepository
import ch.ffhs.esa.battleships.event.Event
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

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


    fun start(playerId: String) {
        if (_player.value != null) {

            return
        }

        loadPlayer(playerId)
        loadBot()
    }

    private fun loadPlayer(uid: String) {
        viewModelScope.launch {
            val result = playerRepository.findByUID(uid)
            if (result is DataResult.Success) {
                _player.value = result.data
                _board.value = BoardModel(0, 0, result.data.id)
                initializeShips()
            } else if (result is DataResult.Error) {
                throw result.exception
            }
        }
    }

    private fun loadBot() {
        viewModelScope.launch {
            val result = playerRepository.findByUID(BOT_PLAYER_ID)
            if (result is DataResult.Success) {
                _bot.value = result.data // TODO show error dialog
            }
        }
    }

    private fun initializeShips() {
        createShips()
        randomizeShipPositions()
        showShips()
    }

    private fun createShips() {
//        val shipsSizes = arrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1) // TODO: Refactor?
        val shipsSizes = mutableListOf(4, 4) // TODO: Refactor?
        _board.value!!.ships.value = shipsSizes.map { size ->
            ShipModel(
                0,
                0,
                size,
                Direction.UP,
                0,
                false,
                directionLogic
            )
        }.toMutableList()
    }

    private fun randomizeShipPositions() {
        _board.value!!.ships.value!!.forEach { ship ->
            ship.x = Random.nextInt(BOARD_SIZE)
            ship.y = Random.nextInt(BOARD_SIZE)
            ship.direction = directionLogic.getRandomDirection()
        }

        var invalidlyPositionedShips = getInvalidlyPositionedShips()
        while (invalidlyPositionedShips.isNotEmpty()) {
            invalidlyPositionedShips.first().x = Random.nextInt(BOARD_SIZE)
            invalidlyPositionedShips.first().y = Random.nextInt(BOARD_SIZE)
            invalidlyPositionedShips = getInvalidlyPositionedShips()
        }

        updateShipPositionValidity()
        _board.value = _board.value

    }

    private fun showShips() {
        _board.value!!.ships.value!!.forEach {
            it.isVisible = true
        }
    }

    private fun getInvalidlyPositionedShips(): HashSet<ShipModel> {
        val overlappingShips = getOverlappingShips()
        val shipsOutsideOfBoard = getShipsOutsideOfBoard()

        overlappingShips.addAll(shipsOutsideOfBoard)

        return overlappingShips
    }

    private fun getOverlappingShips(): HashSet<ShipModel> {
        val overlappingShips = hashSetOf<ShipModel>()
        for (ship in _board.value!!.ships.value!!) {
            if (overlappingShips.contains(ship)) continue

            for (otherShip in _board.value!!.ships.value!!) {
                if (ship == otherShip) continue

                if (ship.isShipWithinOccupiedRangeOfOtherShip(otherShip)) {
                    overlappingShips.add(ship)
                    overlappingShips.add(otherShip)
                }
            }
        }

        return overlappingShips
    }

    private fun getShipsOutsideOfBoard(): HashSet<ShipModel> {
        return _board.value!!.ships.value!!.filter { ship ->
            !ship.isShipCompletelyOnBoard()
        }.toHashSet()
    }

    fun getShipAt(cell: Cell): ShipModel? {
        return _board.value!!.ships.value!!.find { ship ->
            ship.getShipCells().contains(cell)
        }
    }

    fun rotateAround(ship: ShipModel, cell: Cell) {
        ship.rotateAround(cell)
        updateShipPositionValidity()
        _board.value = _board.value
    }

    fun moveShipTo(ship: ShipModel, cell: Cell) {
        val oldX = ship.x
        val oldY = ship.y

        ship.x = cell.x
        ship.y = cell.y

        if (ship.isShipCompletelyOnBoard()) {
            updateShipPositionValidity()
            _board.value = _board.value
            return
        }
        ship.x = oldX
        ship.y = oldY
    }

    private fun updateShipPositionValidity() {
        val invalidlyPositionedShips = getInvalidlyPositionedShips()
        _board.value!!.ships.value!!.forEach { ship ->
            ship.isPositionValid = !invalidlyPositionedShips.contains(ship)
        }
    }

    fun isBoardInValidState(): Boolean {

        return getInvalidlyPositionedShips().isEmpty()
    }

    fun startGame() {
        val game = Game(
            Date.from(Calendar.getInstance().toInstant()),
            GameState.ACTIVE,
            _player.value!!.id,
            _bot.value!!.id,
            _player.value!!.id,
            null
        )
        saveGame(game)
    }


    private fun saveGame(game: Game) = viewModelScope.launch {
        val result = gameRepository.saveGame(game)
        if (result is DataResult.Success) {
            game.id = result.data
            _game.value = game
            onGameSaved()
        }
    }

    private fun onGameSaved() {
        createOwnBoard()
        createBotPlayerBoard()
    }

    private fun createOwnBoard() {
        val board = Board(_game.value!!.id, _player.value!!.id)
        viewModelScope.launch {
            val result = boardRepository.saveBoard(board)
            if (result is DataResult.Success) {
                saveShips(result.data, false)
            }
        }
    }

    private fun createBotPlayerBoard() {
        val board = Board(_game.value!!.id, _bot.value!!.id)
        viewModelScope.launch {
            val result = boardRepository.saveBoard(board)
            if (result is DataResult.Success) {
                initializeShips()
                randomizeShipPositions()
                saveShips(result.data, true)
            }
        }
    }

    private fun saveShips(boardId: Long, fireGameReadyEvent: Boolean) = viewModelScope.launch {
        _board.value!!.ships.value!!.forEach { shipModel ->
            val ship = Ship(
                shipModel.x,
                shipModel.y,
                shipModel.size,
                shipModel.direction,
                boardId
            )
            shipRepository.insert(ship)
        }

        if (fireGameReadyEvent) {
            _gameReadyEvent.value = Event(Unit)
        }
    }
}
