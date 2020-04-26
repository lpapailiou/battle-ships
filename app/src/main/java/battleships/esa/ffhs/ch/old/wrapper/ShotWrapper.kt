//package battleships.esa.ffhs.ch.old.wrapper
//
//import battleships.esa.ffhs.ch.refactored.data.shot.Shot
//
//class ShotWrapper() {       // TODO: not bound to ShotEntity yet
//
//    lateinit var cell: Cell
//    var isHit = false
//    var drawable: Boolean = true
//
//    constructor (cell: Cell) : this() {
//        this.cell = cell
//    }
//
//    constructor (shot: Shot) : this() {
//        this.cell = Cell(shot.coordinate.x, shot.coordinate.y)
//        isHit = shot.boardId != 0
////        drawable = shot.drawable
//    }
//
//    // indicator if hit or water should be drawn
//    fun isHit(hit: Boolean) {
//        isHit = hit
//    }
//
//    // ----------------------------- visibility handling -----------------------------
//    // concepts: shots are invisble when whole ship is sunk, because the ship will be
//    // painted red. this means, additional overlapping shots would be redundant and less pretty
//
//    fun undraw() {
//        drawable = false
//    }
//
//    fun isDrawable(): Boolean {
//        return drawable
//    }
//}
