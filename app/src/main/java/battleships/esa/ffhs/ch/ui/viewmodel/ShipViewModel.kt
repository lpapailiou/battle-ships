package battleships.esa.ffhs.ch.ui.viewmodel

import battleships.esa.ffhs.ch.ui.drawable.Direction
import battleships.esa.ffhs.ch.ui.drawable.Point
import java.lang.Math.abs

class ShipViewModel(val id : Int, var position: Point, val size: Int, var direction: Direction, val hits: Set<Point> = setOf()) {

    private var isPickedUp = false
    private var isPositionValid = true
    var posPoints: MutableList<Point> = mutableListOf<Point>()

    init {
        updatePoints()
    }

    fun getShip(x: Int, y: Int): ShipViewModel? {
        if (isHere(x, y)) {
            pickUp(true)
            return this
        }
        return null
    }

    fun rotate(x: Int, y: Int) {
        var siz = size-1

        println("before: (" + position.col + "," + position.row + "), (" + (position.col + siz*direction.getNextX()) + "," + (position.row+siz*direction.getNextY()) + ")")
        var index = getIndex(x, y)

        direction = direction.getNext()

        var newX = x - (direction.getNextX() * index)
        var newY: Int = y- (direction.getNextY() * index)

        position = Point(newX, newY)
        updatePoints()
    }

    fun getPoints(): MutableList<Point> {
        return posPoints
    }

    fun updatePoints() {
        posPoints.clear()
        posPoints.add(position)
        for (i in 1..size-1) {
            posPoints.add(Point(position.col + i*direction.getNextX(), position.row+i*direction.getNextY()))
        }
    }

    fun getIndex(x: Int, y: Int): Int {
        var xVal = position.col
        var yVal: Int = position.row
        var index: Int = 0

        for (i in 0..size-1) {
            if (xVal == x && yVal == y) {
                return index
            }
            xVal += direction.getNextX()
            yVal += direction.getNextY()
            index++
        }

        return -1
    }

    fun isHere(x: Int, y: Int): Boolean {

        // TODO: check if we have a fuckup mixing col/row
        var xVal = position.col
        var yVal: Int = position.row

        for (i in 0..size-1) {
            if (xVal == x && yVal == y) {
                return true
            }
            xVal += direction.getNextX()
            yVal += direction.getNextY()
        }
        return false
    }

    fun isPickedUp(): Boolean {
        return isPickedUp
    }

    fun pickUp(pick: Boolean) {
        isPickedUp = pick
    }

    fun isPositionValid(): Boolean {
        return isPositionValid
    }

    fun isPositionValid(valid: Boolean) {
        isPositionValid = valid
    }

}
