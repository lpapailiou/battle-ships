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
import battleships.esa.ffhs.ch.refactored.ship.ShipLogic
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class BoardPreparationViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val shipLogic: ShipLogic,
    private val directionLogic: DirectionLogic
) : ViewModel() {

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board> = _board

    private val _ships = MutableLiveData<List<Ship>>()
    val ships: LiveData<List<Ship>> = _ships


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
            Ship(
                0,
                0,
                size,
                Direction.UP,
                0
            )
        }
    }

    private fun randomizeShipPositions() {
        _ships.value!!.forEach { ship ->
            ship.x = Random.nextInt(BOARD_SIZE)
            ship.y = Random.nextInt(BOARD_SIZE)
            ship.direction = directionLogic.getRandomDirection()
        }

        var overlapList = getOverlappingShips()
        while (!overlapList.isEmpty()) {
            overlapList.first().x = Random.nextInt(BOARD_SIZE)
            overlapList.first().y = Random.nextInt(BOARD_SIZE)
            overlapList = getOverlappingShips()
        }
    }

    private fun getOverlappingShips(): HashSet<Ship> {
        val overlappingShips = hashSetOf<Ship>()
        for (ship in _ships.value!!) {
            if (overlappingShips.contains(ship)) continue

            for (otherShip in _ships.value!!) {
                if (ship == otherShip) continue

                if (shipLogic.areShipsWithinOccupiedRangeOfEachOther(ship, otherShip)) {
                    overlappingShips.add(ship)
                    overlappingShips.add(otherShip)
                }
            }
        }
        return overlappingShips
    }

    fun determineShipAt(cell: Cell): Ship? {
        return _ships.value!!.find { ship ->
            shipLogic.determineShipCells(ship).contains(cell)
        }
    }

    fun rotateAround(ship: Ship, cell: Cell) {
        shipLogic.rotateAround(ship, cell)
    }

    fun moveShip(ship: Ship, cell: Cell) {
        ship.x = cell.x
        ship.y = cell.y
    }
}
