package battleships.esa.ffhs.ch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import battleships.esa.ffhs.ch.entity.CoordinateEntity
import battleships.esa.ffhs.ch.wrapper.Cell
import battleships.esa.ffhs.ch.wrapper.DirectionHandler
import battleships.esa.ffhs.ch.entity.ShipEntity
import battleships.esa.ffhs.ch.entity.ShotEntity
import battleships.esa.ffhs.ch.wrapper.ShotWrapper
import battleships.esa.ffhs.ch.model.Direction
import battleships.esa.ffhs.ch.model.STRICT_OVERLAP_RULE
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.mainViewModel

class ShipViewModel(shipData: ShipEntity) {

    private var shipCells: MutableList<Cell> = mutableListOf<Cell>()
    private var isSunken = false
    private var ship: ShipEntity
    private var shots: MutableList<ShotEntity> = mutableListOf()

    init {
        ship = shipData
        var shotList = mainViewModel.getOpponentShots().value?.filter { shot ->
            shot.shot_ship_owner_id == shipData.ship_id
        }?.toList()
        shotList?.forEach { shot -> shots.add(shot) }
        updateCells()
        isSunken = shotList?.size == shipCells.size
    }

    fun save() {
        mainViewModel.save(ship)
    }

    fun getBowCoordinate(): CoordinateEntity {
        return ship.bowCoordinate
    }

    fun setBowCoordinate(coordinate: CoordinateEntity) {
        val tempShip = ship
        tempShip.bowCoordinate = coordinate
        ship = tempShip
        save()
    }

    fun getDirection(): Direction {
        return ship.direction
    }

    fun setDirection(direction: Direction) {
        val tempShip = ship
        tempShip.direction = direction
        ship = tempShip
        save()
    }

    fun getObservableShip(): LiveData<ShipEntity> {
        var tempShip = MutableLiveData<ShipEntity>()
        tempShip.value = ship
        return tempShip
    }

    fun getShipSize(): Int {
        return ship.size
    }

    fun addHit(shot: ShotWrapper) {
        /*
        tempShip.value!!.shotCount++
        ship = tempShip
        val newShot = ShotEntity(
            shot.cell.coordinate,
            ship.value!!.owner,
            ship.value!!.ship_id,
            shot.isHit,
            shot.drawable
        )
        var tempShots: List<ShotEntity> = mutableListOf()*/
        //tempShots = shots.value!!
        //tempShots.add(newShot) // TODO: shot handling - shots are not saved to database yet
        //shots = tempShots
    }

    fun getHits(): Int {
        return shots?.size
    }

    fun hide(hide: Boolean) {
        val tempShip = ship
        tempShip.isHidden = hide
        ship = tempShip
    }

    // ----------------------------- ship body location handling -----------------------------

    fun updateCells() {
        shipCells.clear()
        shipCells.add(
            Cell(
                getBowCoordinate().x,
                getBowCoordinate().y
            )
        )
        for (i in 1 until getShipSize()) {
            shipCells.add(
                Cell(
                    getBowCoordinate().x + i * getDirection().x,
                    getBowCoordinate().y + i * getDirection().y
                )
            )
        }
    }

    // ----------------------------- get ship -----------------------------

    fun getShip(cell: Cell): ShipViewModel? {
        if (isCellOnShip(cell)) {
            return this
        }
        return null
    }

    fun isCellOnShip(cellToFind: Cell): Boolean {
        for (shipCell in shipCells) {
            if (cellToFind.equals(shipCell)) {
                return true
            }
        }
        return false
    }

    // ----------------------------- ship location actions -----------------------------

    // TODO: probably better to refactor
    fun setRandomly() {
        do {
            setBowCoordinate(Cell(0, 0).getRandomCoordinate())
            setDirection(
                DirectionHandler(
                    getDirection()
                ).getRandomDirection())
            updateCells()
        } while (!isShipCompletelyOnBoard())
    }

    fun set(cell: Cell) {
        setBowCoordinate(cell.coordinate)
        updateCells()
        mainViewModel.save(ship)
    }

    fun set(cell: Cell, offset: Cell) {
        set(
            Cell(
                cell.coordinate.x - offset.coordinate.x,
                cell.coordinate.y - offset.coordinate.y
            )
        )
    }

    fun rotate(cell: Cell) {
        val index = getIndex(cell)
        setDirection(
            DirectionHandler(
                getDirection()
            ).getNextClockwiseNondiagonalDirection())
        setBowCoordinate(
            CoordinateEntity(
                cell.coordinate.x - (getDirection().x * index),
                cell.coordinate.y - (getDirection().y * index)
            )
        )
        mainViewModel.save(ship)
        updateCells()
    }

    // ----------------------------- hit handling -----------------------------

    fun hit(shot: ShotWrapper) {
        addHit(shot)
        sinkCheck()
    }

    private fun sinkCheck() {
        isSunken = shots.size == shipCells.size
        if (isSunken) {
            if (shots != null) {
                shots.forEach { h ->
                    h.drawable = false
                }
            }   // shots of ships get invisible (as they overlap ship)
            hide(false)                                // ships gets visible again; drawn in red to vizualize it is completely sunk
        }
    }

    fun isSunken(): Boolean {
        return isSunken
    }

    // ----------------------------- positioning and check helper methods -----------------------------

    fun isShipCompletelyOnBoard(): Boolean {
        for (cell in shipCells) {
            if (!cell.isValid()) {
                return false
            }
        }
        return true
    }

    /**
     * Returns all Cells which are occupied / taken by the current ship. These are all cells where
     * there must not be another ship.
     */
    fun getOccupiedCells(): HashSet<Cell> {
        if (STRICT_OVERLAP_RULE) {        // ships must not touch each other
            val occupiedCells: HashSet<Cell> = linkedSetOf()
            occupiedCells.addAll(shipCells)
            shipCells.forEach { shipCell ->
                occupiedCells.addAll(shipCell.getSurroundingCells())
            }
            return occupiedCells
        }
        return shipCells.toHashSet()    // ships can touch each other
    }

    fun isWithinOccupiedAreaOfOtherShip(otherShip: ShipViewModel): Boolean {
        val occupiedArea = otherShip.getOccupiedCells()
        return occupiedArea.intersect(shipCells).isNotEmpty()
    }

    // TODO: check if can be replaced with getOffset method
    fun getIndex(cell: Cell): Int {
        var pos: Cell =
            Cell(
                getBowCoordinate().x,
                getBowCoordinate().y
            )

        for ((index, i) in (0 until getShipSize()).withIndex()) {
            if (pos.equals(cell)) {
                return index
            }
            pos = Cell(
                pos.coordinate.x + getDirection().x,
                pos.coordinate.y + getDirection().y
            )
        }
        return -1
    }

    // used for more intuitive moving
    fun getOffset(cell: Cell): Cell {
        return Cell(
            cell.coordinate.x - getBowCoordinate().x,
            cell.coordinate.y - getBowCoordinate().y
        )
    }

    // ----------------------------- generic getters and setters -----------------------------

    fun isHidden(): Boolean {
        return ship.isHidden
    }

    fun isPositionValid(): Boolean {
        return ship.isPositionValid
    }

    fun isPositionValid(valid: Boolean) {
        val tempShip = ship
        tempShip.isPositionValid = valid
        ship = tempShip
    }

}
