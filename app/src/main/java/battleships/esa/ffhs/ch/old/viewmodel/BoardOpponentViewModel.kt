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
//    private fun initializeShots() {
//        var shotList = mainViewModel.getOpponentShots().value
//        if (shotList != null) {
//            shots.value = shotList.map { shot -> ShotWrapper(shot) }.toMutableList()
//        }
//    }
//
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
//
//}
