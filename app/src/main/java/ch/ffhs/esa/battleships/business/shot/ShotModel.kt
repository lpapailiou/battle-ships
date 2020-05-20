package ch.ffhs.esa.battleships.business.shot

class ShotModel(
    val id: Long = 0,
    val x: Int,
    val y: Int,
    val boardId: Long,
    val isHit: Boolean
)
