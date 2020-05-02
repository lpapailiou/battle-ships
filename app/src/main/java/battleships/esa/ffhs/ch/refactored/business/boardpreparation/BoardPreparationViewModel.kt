package battleships.esa.ffhs.ch.refactored.business.boardpreparation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import battleships.esa.ffhs.ch.old.model.BOARD_SIZE
import battleships.esa.ffhs.ch.old.model.BOT_PLAYER_ID
import battleships.esa.ffhs.ch.old.model.GameState
import battleships.esa.ffhs.ch.refactored.business.board.Cell
import battleships.esa.ffhs.ch.refactored.business.ship.DirectionLogic
import battleships.esa.ffhs.ch.refactored.business.ship.ShipModel
import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.board.Board
import battleships.esa.ffhs.ch.refactored.data.board.BoardRepository
import battleships.esa.ffhs.ch.refactored.data.game.Game
import battleships.esa.ffhs.ch.refactored.data.game.GameRepository
import battleships.esa.ffhs.ch.refactored.data.player.Player
import battleships.esa.ffhs.ch.refactored.data.player.PlayerRepository
import battleships.esa.ffhs.ch.refactored.data.ship.Direction
import battleships.esa.ffhs.ch.refactored.data.ship.Ship
import battleships.esa.ffhs.ch.refactored.data.ship.ShipRepository
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

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board> = _board

    private val _ships = MutableLiveData<List<ShipModel>>()
    val ships: LiveData<List<ShipModel>> = _ships


    fun start(playerId: String) {
        if (_player.value != null) {

            return
        }
        loadPlayer(playerId)
        loadBot()
        initializeShips()
        randomizeShipPositions()
    }

    private fun loadPlayer(playerId: String) {
        viewModelScope.launch {
            val result = playerRepository.findByPlayerId(playerId)
            if (result is DataResult.Success) {
                _player.value = result.data
            } else if (result is DataResult.Error) {
                throw result.exception
            }
        }
    }

    private fun loadBot() {
        viewModelScope.launch {
            val result = playerRepository.findByPlayerId(BOT_PLAYER_ID)
            if (result is DataResult.Success) {
                _bot.value = result.data
            }
        }
    }

    private fun initializeShips() {
        val shipsSizes = arrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1) // TODO: Refactor?
        _ships.value = shipsSizes.map { size ->
            ShipModel(
                0,
                0,
                size,
                Direction.UP,
                0,
                directionLogic
            )
        }
    }

    // TODO: move to BoardModel
    private fun randomizeShipPositions() {
        _ships.value!!.forEach { ship ->
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
        _ships.value = ships.value

    }

    private fun getInvalidlyPositionedShips(): HashSet<ShipModel> {
        val overlappingShips = getOverlappingShips()
        val shipsOutsideOfBoard = getShipsOutsideOfBoard()

        overlappingShips.addAll(shipsOutsideOfBoard)

        return overlappingShips
    }

    private fun getOverlappingShips(): HashSet<ShipModel> {
        val overlappingShips = hashSetOf<ShipModel>()
        for (ship in _ships.value!!) {
            if (overlappingShips.contains(ship)) continue

            for (otherShip in _ships.value!!) {
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
        return _ships.value!!.filter { ship ->
            !ship.isShipCompletelyOnBoard()
        }.toHashSet()
    }

    fun getShipAt(cell: Cell): ShipModel? {
        return _ships.value!!.find { ship ->
            ship.getShipCells().contains(cell)
        }
    }

    fun rotateAround(ship: ShipModel, cell: Cell) {
        ship.rotateAround(cell)
        updateShipPositionValidity()
        _ships.value = ships.value
    }

    fun moveShipTo(ship: ShipModel, cell: Cell) {
        val oldX = ship.x
        val oldY = ship.y

        ship.x = cell.x
        ship.y = cell.y

        if (ship.isShipCompletelyOnBoard()) {
            updateShipPositionValidity()
            _ships.value = ships.value
            return
        }
        ship.x = oldX
        ship.y = oldY
    }

    private fun updateShipPositionValidity() {
        val invalidlyPositionedShips = getInvalidlyPositionedShips()
        _ships.value!!.forEach { ship ->
            ship.isPositionValid = !invalidlyPositionedShips.contains(ship)
        }
    }

    fun isBoardInValidState(): Boolean {

        return getInvalidlyPositionedShips().isEmpty()
    }

    fun startGame() {
        createGame()
    }

    fun createGame() {
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
                saveShips(result.data)
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
                saveShips(result.data)
            }
        }
    }

    private fun saveShips(boardId: Long) = viewModelScope.launch {
        _ships.value!!.forEach { shipModel ->
            val ship = Ship(
                shipModel.x,
                shipModel.y,
                shipModel.size,
                shipModel.direction,
                boardId
            )
            shipRepository.insert(ship)
        }
    }

}
