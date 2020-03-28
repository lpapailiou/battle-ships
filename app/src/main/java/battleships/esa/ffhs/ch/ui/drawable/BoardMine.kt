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


class BoardMine(
    context: Context, attributes: AttributeSet
) : Board(context, attributes) {

    init {
        //Game(this, opponentBoard!!)
        ships = initShips()
        setShipsRandomly()
        ships.forEach {it.hide()}
        currentShip = null
    }

    // ----------------------------- ship handling -----------------------------

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null || isGameFinished) {
            return false
        }
        var refresh: Boolean = false

        var xTouch: Float = event!!.getX(0)
        var yTouch: Float = event!!.getY(0)

        var pointerPosition: Point = Point((xTouch/gridWidth).toInt(), (yTouch/gridWidth).toInt())

        // TODO: check if shot was already done
        if (shots.filter{s -> s.point.equals(pointerPosition)}.count() > 0) {
            return false
        }

        var shot = Shot(pointerPosition, null)
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
        refreshView(refresh)
        if (refresh) {
            randomShot()
        }
        return super.onTouchEvent(event) || refresh
    }

    fun randomShot(): Boolean {
        if (opponentBoard != null && !isGameFinished) {
            var success = opponentBoard!!.shoot(Shot(Point(0,0).getRandom(), null))
            if (!success) {
                randomShot()
            } else {
                return true
            }
        } else {
            return true
        }
        return false
    }


}
