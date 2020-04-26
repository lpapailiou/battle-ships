//package battleships.esa.ffhs.ch.old.viewmodel
//
//import battleships.esa.ffhs.ch.old.model.GameState
//import battleships.esa.ffhs.ch.old.ui.game.GameFragment.Companion.currentGame
//import battleships.esa.ffhs.ch.old.ui.main.MainActivity.Companion.mainViewModel
//import battleships.esa.ffhs.ch.old.wrapper.Cell
//import battleships.esa.ffhs.ch.old.wrapper.ShotWrapper
//import battleships.esa.ffhs.ch.old.ui.game.BoardViewModel
//
//class BoardOpponentViewModel(activeGame: GameViewModel) : BoardViewModel(0, "") {
//
//    init {
//        updateShips()
//        initializeShots()
//    }
//
//    fun updateShips() {         // TODO: very much malfunctioning at the moment because of async stuff
//        var shipList = mainViewModel.getOpponentShips().value
//        if (shipList != null) {
//            ships.value = shipList.map { ship ->
//                ShipViewModel(ship)
//            }.toMutableList()
//            if (ships.value!!.filter { !it.isPositionValid() }.count() > 0) {
////                setShipsRandomly()
//            }
//        }
//    }
//
//    private fun initializeShots() {
//        var shotList = mainViewModel.getOpponentShots().value
//        if (shotList != null) {
//            shots.value = shotList.map { shot -> ShotWrapper(shot) }.toMutableList()
//        }
//    }
//
//    // ----------------------------- handle click action from UI -----------------------------
//
//    override fun repeatClickCheck(pointerPosition: Cell): Boolean {
//        return false
//    }
//
//    override fun identifyShip(pointerPosition: Cell): Boolean {
//        if (currentShip != null) {
//            return true
//        }
//        for (ship in getShips()) {
//            if (ship.isCellOnShip(pointerPosition) && currentShip == null) {
//                currentShip = ship.getShip(pointerPosition)
//                offset = currentShip!!.getOffset(pointerPosition)
//                return true
//            }
//        }
//        return false
//    }
//
//    override fun clickAction(pointerPosition: Cell): Boolean {
//        currentShip!!.rotate(pointerPosition)
//        shipInvalidPositionValidityCheck()
//        currentShip = null
//        return true
//    }
//
//    override fun moveAction(pointerPosition: Cell): Boolean {
//        var oldPos = currentShip!!.getBowCoordinate()
//        currentShip!!.set(pointerPosition, offset)
//        if (!currentShip!!.isShipCompletelyOnBoard()) {
//            currentShip!!.set(
//                Cell(
//                    oldPos.x,
//                    oldPos.y
//                )
//            )
//        }
//        shipInvalidPositionValidityCheck()
//        return true
//    }
//
//    // ----------------------------- take a shot on opponents board -----------------------------
//
//    fun shoot(shot: ShotWrapper): Boolean {
//        if (currentGame.equalsState(GameState.ENDED)) {
//            return true
//        }
//        var refresh: Boolean = false
//        var pointerPosition: Cell = shot.cell
//        if (getShots().filter { s -> s.cell == pointerPosition }.count() > 0) {
//            return false
//        }
//
//        for (ship in getShips()) {
//            if (ship.isCellOnShip(pointerPosition)) {
//                currentShip = ship.getShip(pointerPosition)
//                if (currentShip != null) {
//                    hit(currentShip!!, shot)
//                    currentShip = null
//                }
//                refresh = true
//
//            } else {
//                hit(shot)
//                refresh = true
//            }
//        }
//        if (refresh) {
//            endGameCheck()
//            return true
//        }
//        return false
//    }
//
//    override fun endGameCheck(): Boolean {
//        val gameEnded = super.endGameCheck()
//        if (gameEnded && currentGame.notEqualsState(GameState.ENDED)) {
//            val tempGame = currentGame
//            tempGame.data.value!!.state = GameState.ENDED
//            currentGame = tempGame
//            return true
//        }
//        return false
//    }
//
//    fun shipInvalidPositionValidityCheck(): Boolean {
//        var changed = false
//        val overlapList = getOverlappingShips()
//        for (ship in getShips()) {
//            var isPosValid = true
//
//            if (!ship.isShipCompletelyOnBoard() || overlapList.contains(ship)) {
//                isPosValid = false
//            }
//
//            if (isPosValid != ship.isPositionValid()) {
//                ship.isPositionValid(isPosValid)
//                changed = true
//            }
//        }
//        return changed
//    }
//
//}
