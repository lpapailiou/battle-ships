package battleships.esa.ffhs.ch.ui.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.Shot
import battleships.esa.ffhs.ch.ui.game.BoardPreparationFragment
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.isGameFinished
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.isGameStarted
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.opponentBoard
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.preparedShips
import battleships.esa.ffhs.ch.ui.viewmodel.ShipViewModel
import kotlinx.android.synthetic.main.board_preparation_fragment.view.*


class BoardOpponent  (
    context: Context, attributes: AttributeSet
) : Board(context, attributes) {


    init {
        if (preparedShips.isEmpty()) {
            ships = initShips()
            setShipsRandomly()          // set ships randomly on board
        } else {
            ships = preparedShips
            opponentBoard = this
        }
        currentShip = null
    }

    fun shoot(shot: Shot): Boolean {
        if (isGameFinished) {
            return true
        }
        var refresh: Boolean = false
        var pointerPosition: Point = shot.point
        println("opponent board shot at " + pointerPosition.toString())
        if (shots.filter{s -> s.point.equals(pointerPosition)}.count() > 0) {
            return false
        }

        for (ship in ships) {
            if (ship.isHere(pointerPosition)) {
                currentShip = ship.getShip(pointerPosition)
                if (currentShip != null) {
                    hit(currentShip!!, shot)
                    currentShip = null
                }
                refresh = true

            } else {
                hit(shot)
                refresh = true
            }


        }
        if (refresh) {
            refreshView(refresh)
            return true
        }
        return false
    }

    // ----------------------------- jump to game validation -----------------------------

    open fun validateStart(): List<ShipViewModel> {
        if (ships.filter{s -> !s.isPositionValid()}.count() > 0) {
            return listOf()
        }
        return ships
    }

    // ----------------------------- ship positioning handling -----------------------------

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null || isGameStarted) {
            return false
        }

        var refresh: Boolean = false
        if (currentShip != null) {
            refresh = true
        }

        var xTouch: Float = event.getX(0)
        var yTouch: Float = event.getY(0)

        var pointerPosition: Point = Point((xTouch/gridWidth).toInt(), (yTouch/gridWidth).toInt())

        for (ship in ships) {
            if (ship.isHere(pointerPosition) && currentShip == null) {
                currentShip = ship.getShip(pointerPosition)
                offset = currentShip!!.getOffset(pointerPosition)
                refresh = true
            }
        }

        when (event.getActionMasked()) {
            MotionEvent.ACTION_MOVE -> {            // move ship around
                clickCounter++
                if (currentShip != null && clickCounter > CLICK_LIMIT) {
                    var oldPos = currentShip!!.getPoint()
                    currentShip!!.set(pointerPosition, offset)
                    if (!currentShip!!.isShipOnBoard()) {
                        currentShip!!.set(oldPos)
                    }
                }
                refreshView(refresh)
                return true
            }
            MotionEvent.ACTION_UP -> {              // rotate ship
                if (currentShip != null && clickCounter <= CLICK_LIMIT) {
                    currentShip!!.rotate(pointerPosition)
                }
                refreshView(refresh)
                clickCounter = 0
                offset = Point(0,0)
                currentShip = null
            }
        }
        return super.onTouchEvent(event) || refresh
    }

    override fun refreshView(refresh: Boolean) {     // refresh canvas if necessary
        if (shipInvalidPositionValidityCheck() || refresh) {
            invalidate()
        }
    }

    fun shipInvalidPositionValidityCheck(): Boolean {
        var changed = false
        var overlapList = getOverlappingShips()
        for (ship in ships) {
            var isPosValid = true

            if (!ship.isShipOnBoard() || overlapList.contains(ship)) {
                isPosValid = false
            }

            if (isPosValid != ship.isPositionValid()) {
                ship.isPositionValid(isPosValid)
                changed = true
            }
        }
        return changed
    }


}
