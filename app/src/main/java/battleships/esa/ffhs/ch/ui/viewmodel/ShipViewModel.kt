package battleships.esa.ffhs.ch.ui.viewmodel

import battleships.esa.ffhs.ch.ui.drawable.Direction
import battleships.esa.ffhs.ch.ui.drawable.Point
import java.lang.Math.abs

class ShipViewModel(val id : Int, var position: Point, val size: Int, var direction: Direction, val hits: Set<Point> = setOf()) {

    private var isPositionValid = true
    var posPoints: MutableList<Point> = mutableListOf<Point>()

    init {
        updatePoints()
    }

    fun getShip(p: Point): ShipViewModel? {
        if (isHere(p)) {
            return this
        }
        return null
    }

    fun set(p: Point) {
        position = p
        updatePoints()
    }

    fun set(p: Point, offset: Point) {
        set(Point(p.col-offset.col, p.row-offset.row))
    }

    fun rotate(p: Point) {
        var siz = size-1
        var index = getIndex(p)

        direction = direction.getNext()
        position = Point(p.col-(direction.getNextX() * index), p.row-(direction.getNextY() * index))
        updatePoints()
    }

    fun getOffset(p: Point):Point {
        return Point(p.col - position.col, p.row - position.row)
    }

    fun getPoints(): MutableList<Point> {
        return posPoints
    }

    fun getPoint(): Point {
        return position
    }

    fun updatePoints() {
        posPoints.clear()
        posPoints.add(position)
        for (i in 1..size-1) {
            posPoints.add(Point(position.col + i*direction.getNextX(), position.row+i*direction.getNextY()))
        }
    }

    fun getIndex(p:Point): Int {
        var pos: Point = position
        var index: Int = 0

        for (i in 0..size-1) {
            if (pos.equals(p)) {
                return index
            }
            pos = Point(pos.col+direction.getNextX(), pos.row+direction.getNextY())
            index++
        }
        return -1
    }

    fun isHere(p: Point): Boolean {
        for (point in posPoints) {
            if (p.equals(point)) {
                return true
            }
        }
        return false
    }

    fun isPositionValid(): Boolean {
        return isPositionValid
    }

    fun isPositionValid(valid: Boolean) {
        isPositionValid = valid
    }

}
