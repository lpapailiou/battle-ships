//package battleships.esa.ffhs.ch.old.wrapper
//
//import battleships.esa.ffhs.ch.old.model.BOARD_SIZE
//import battleships.esa.ffhs.ch.refactored.data.ship.Direction
//import java.util.*
//
//
//class Cell(x: Int, y: Int) {
//
//    val coordinate =
//        Coordinate(x, y)
//
//    fun isValid(): Boolean {
//        if (coordinate.x < 0 || coordinate.x >= BOARD_SIZE || coordinate.y < 0 || coordinate.y >= BOARD_SIZE) {
//            return false
//        }
//        return true
//    }
//
//    fun getRandomCell(): Cell {
//        return Cell(
//            (0 until BOARD_SIZE).random(),
//            (0 until BOARD_SIZE).random()
//        )
//    }
//
//    fun getRandomCoordinate(): Coordinate {
//        return Coordinate(
//            (0 until BOARD_SIZE).random(),
//            (0 until BOARD_SIZE).random()
//        )
//    }
//
//}
