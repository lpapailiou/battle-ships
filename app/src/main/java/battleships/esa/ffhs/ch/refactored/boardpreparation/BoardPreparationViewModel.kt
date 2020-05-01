package battleships.esa.ffhs.ch.refactored.boardpreparation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import battleships.esa.ffhs.ch.old.model.BOARD_SIZE
import battleships.esa.ffhs.ch.refactored.board.Cell
import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.board.Board
import battleships.esa.ffhs.ch.refactored.data.player.Player
import battleships.esa.ffhs.ch.refactored.data.player.PlayerRepository
import battleships.esa.ffhs.ch.refactored.data.ship.Direction
import battleships.esa.ffhs.ch.refactored.data.ship.Ship
import battleships.esa.ffhs.ch.refactored.ship.DirectionLogic
import battleships.esa.ffhs.ch.refactored.ship.ShipModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class BoardPreparationViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val directionLogic: DirectionLogic
) : ViewModel() {

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board> = _board

    private val _ships = MutableLiveData<List<ShipModel>>()
    val ships: LiveData<List<ShipModel>> = _ships


    fun start(playerId: String) {
        loadPlayer(playerId)
        _board.value =
            Board(0, playerId)
        initializeShips()
        randomizeShipPositions()
    }

    private fun loadPlayer(playerId: String) {
        viewModelScope.launch {
            val result = playerRepository.findById(playerId)
            if (result is DataResult.Success) {
                _player.value = result.data
            }
        }
    }

    private fun initializeShips() {
        val shipsSizes = arrayOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1) // TODO: Refactor?
        _ships.value = shipsSizes.map { size ->
            ShipModel(
                Ship(
                    0,
                    0,
                    size,
                    Direction.UP,
                    0
                ),
                directionLogic
            )
        }
    }

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
}
