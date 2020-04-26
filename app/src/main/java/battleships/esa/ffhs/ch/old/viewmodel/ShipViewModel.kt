//package battleships.esa.ffhs.ch.old.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import battleships.esa.ffhs.ch.old.ui.main.MainActivity.Companion.mainViewModel
//import battleships.esa.ffhs.ch.old.wrapper.Cell
//import battleships.esa.ffhs.ch.old.wrapper.DirectionHandler
//import battleships.esa.ffhs.ch.old.wrapper.ShotWrapper
//import battleships.esa.ffhs.ch.refactored.data.ship.Direction
//import battleships.esa.ffhs.ch.refactored.data.ship.Ship
//import battleships.esa.ffhs.ch.refactored.data.shot.Shot
//
//class ShipViewModel(shipData: Ship) {
//
//    private var shipCells: MutableList<Cell> = mutableListOf<Cell>()
//    private var isSunken = false
//    private var ship: Ship
//    private var shots: MutableList<Shot> = mutableListOf()
//
//    init {
//        ship = shipData
//        var shotList = mainViewModel.getOpponentShots().value?.filter { shot ->
//            shot.boardId == shipData.boardId
//        }?.toList()
//        shotList?.forEach { shot -> shots.add(shot) }
//        updateCells()
//        isSunken = shotList?.size == shipCells.size
//    }
//
//    fun save() {
////        mainViewModel.save(ship)
//    }
//
//    fun getDirection(): Direction {
//        return ship.direction
//    }
//
//    fun setDirection(direction: Direction) {
//        val tempShip = ship
//        tempShip.direction = direction
//        ship = tempShip
//        save()
//    }
//
//    fun getObservableShip(): LiveData<Ship> {
//        var tempShip = MutableLiveData<Ship>()
//        tempShip.value = ship
//        return tempShip
//    }
//
//    fun getShipSize(): Int {
//        return ship.size
//    }
//
//    fun addHit(shot: ShotWrapper) {
//        /*
//        tempShip.value!!.shotCount++
//        ship = tempShip
//        val newShot = ShotEntity(
//            shot.cell.coordinate,
//            ship.value!!.owner,
//            ship.value!!.ship_id,
//            shot.isHit,
//            shot.drawable
//        )
//        var tempShots: List<ShotEntity> = mutableListOf()*/
//        //tempShots = shots.value!!
//        //tempShots.add(newShot) // TODO: shot handling - shots are not saved to database yet
//        //shots = tempShots
//    }
//
//    fun getHits(): Int {
//        return shots?.size
//    }
//
//    fun hide(hide: Boolean) {
//        val tempShip = ship
////        tempShip.isHidden = hide
//        ship = tempShip
//    }
//
//    // ----------------------------- ship body location handling -----------------------------
//
//    fun updateCells() {
////        shipCells.clear()
////        shipCells.add(
////            Cell(
////                getBowCoordinate().x,
////                getBowCoordinate().y
////            )
////        )
////        for (i in 1 until getShipSize()) {
////            shipCells.add(
////                Cell(
////                    getBowCoordinate().x + i * getDirection().x,
////                    getBowCoordinate().y + i * getDirection().y
////                )
////            )
////        }
//    }
//
//    // ----------------------------- get ship -----------------------------
//
//    fun getShip(cell: Cell): ShipViewModel? {
//        if (isCellOnShip(cell)) {
//            return this
//        }
//        return null
//    }
//
//    fun isCellOnShip(cellToFind: Cell): Boolean {
//        for (shipCell in shipCells) {
//            if (cellToFind.equals(shipCell)) {
//                return true
//            }
//        }
//        return false
//    }
//
//    // ----------------------------- ship location actions -----------------------------
//
//    fun set(cell: Cell) {
////        setBowCoordinate(cell.coordinate)
////        updateCells()
////        mainViewModel.save(ship)
//    }
//
//    fun set(cell: Cell, offset: Cell) {
//        set(
//            Cell(
//                cell.coordinate.x - offset.coordinate.x,
//                cell.coordinate.y - offset.coordinate.y
//            )
//        )
//    }
//
//    fun rotate(cell: Cell) {
//        val index = getIndex(cell)
//        setDirection(
//            DirectionHandler(
//                getDirection()
//            ).getNextClockwiseNondiagonalDirection()
//        )
////        setBowCoordinate(
////            Coordinate(
////                cell.coordinate.x - (getDirection().x * index),
////                cell.coordinate.y - (getDirection().y * index)
////            )
////        )
////        mainViewModel.save(ship)
//        updateCells()
//    }
//
//    // ----------------------------- hit handling -----------------------------
//
//    fun hit(shot: ShotWrapper) {
//        addHit(shot)
//        sinkCheck()
//    }
//
//    private fun sinkCheck() {
//        isSunken = shots.size == shipCells.size
//        if (isSunken) {
//            if (shots != null) {
//                shots.forEach { h ->
////                    h.drawable = false
//                }
//            }   // shots of ships get invisible (as they overlap ship)
//            hide(false)                                // ships gets visible again; drawn in red to vizualize it is completely sunk
//        }
//    }
//
//    fun isSunken(): Boolean {
//        return isSunken
//    }
//
//    // ----------------------------- positioning and check helper methods -----------------------------
//
//    fun isShipCompletelyOnBoard(): Boolean {
//        for (cell in shipCells) {
//            if (!cell.isValid()) {
//                return false
//            }
//        }
//        return true
//    }
//
//
//    // TODO: check if can be replaced with getOffset method
//    fun getIndex(cell: Cell): Int {
////        var pos: Cell =
////            Cell(
////                getBowCoordinate().x,
////                getBowCoordinate().y
////            )
////
////        for ((index, i) in (0 until getShipSize()).withIndex()) {
////            if (pos.equals(cell)) {
////                return index
////            }
////            pos = Cell(
////                pos.coordinate.x + getDirection().x,
////                pos.coordinate.y + getDirection().y
////            )
////        }
//        return -1
//    }
//
//    // used for more intuitive moving
////    fun getOffset(cell: Cell): Cell {
////        return Cell(
////            cell.coordinate.x - getBowCoordinate().x,
////            cell.coordinate.y - getBowCoordinate().y
////        )
////    }
//
//    // ----------------------------- generic getters and setters -----------------------------
//
//    fun isHidden(): Boolean {
//        return false
////        return ship.isHidden
//    }
//
//    fun isPositionValid(): Boolean {
//        return true
////        return ship.isPositionValid
//    }
//
//    fun isPositionValid(valid: Boolean) {
//        val tempShip = ship
////        tempShip.isPositionValid = valid
//        ship = tempShip
//    }
//
//}
