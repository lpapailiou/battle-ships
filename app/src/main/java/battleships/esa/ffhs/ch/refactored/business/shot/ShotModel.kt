package battleships.esa.ffhs.ch.refactored.business.shot

class ShotModel(
    val id: Long = 0,
    val x: Int,
    val y: Int,
    val boardId: Long,
    val isHit: Boolean
) {

}
