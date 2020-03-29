package battleships.esa.ffhs.ch.ui.viewmodel

import battleships.esa.ffhs.ch.ui.drawable.Shot
import battleships.esa.ffhs.ch.ui.drawable.Direction
import battleships.esa.ffhs.ch.ui.drawable.Point
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.strictOverlapRule

class ShipViewModel(val id : Int, var position: Point, val size: Int, var direction: Direction, val hits: MutableSet<Shot> = mutableSetOf()) {

    private var isPositionValid = true
    private var isHidden = false
    var posPoints: MutableList<Point> = mutableListOf<Point>()

    init {
        updatePoints()
    }

    fun hide() {
        isHidden = true
    }

    fun hit (shot: Shot) {
        hits.add(shot)
        isSunk()
    }
    fun isHidden(): Boolean {
        return isHidden
    }
    fun isSunk(): Boolean {
        var isSunk = hits.size == posPoints.size
        if (isSunk) {
            hits.forEach { h -> h.undraw() }
            isHidden = false
        }
        return isSunk
    }

    fun setRandomly() {
        position = position.getRandom()
        direction = direction.getRandom()
        updatePoints()
        if (!isShipOnBoard()) {
            setRandomly()
        }
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

    fun getOverlapArea(): MutableList<Point> {
        if (strictOverlapRule) {
            var posAreaPoints: MutableList<Point> = mutableListOf<Point>()
            var dirs: Array<Direction> = Direction.values()
            posAreaPoints.addAll(posPoints)
            posPoints.forEach { p ->
                dirs.forEach { d ->
                    var point: Point = Point(p.col+d.getNextX(), p.row+d.getNextY())
                    if (!posAreaPoints.contains(point)) {
                        posAreaPoints.add(point)
                    }
                }
            }
            return posAreaPoints
        }
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

    fun isShipOnBoard(): Boolean {
        var isOnBoard = true
        for (p in posPoints) {
            if (!p.isValid()) {
                isOnBoard = false
            }
        }
        return isOnBoard
    }

    fun isPositionValid(): Boolean {
        return isPositionValid
    }

    fun isPositionValid(valid: Boolean) {
        isPositionValid = valid
    }

}
