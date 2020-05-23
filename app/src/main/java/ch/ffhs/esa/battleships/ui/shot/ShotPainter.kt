package ch.ffhs.esa.battleships.ui.shot

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toRect
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.BOARD_SIZE
import ch.ffhs.esa.battleships.business.shot.ShotModel

class ShotPainter(
    context: Context, attributes: AttributeSet
) : View(context, attributes) {

    private val shipShotImage: Drawable? = context.getDrawable(R.drawable.ship_shot);
    private val waterShotImage: Drawable? = context.getDrawable(R.drawable.water_shot);

    // TODO: Move heavy work out of draw loop
    fun draw(canvas: Canvas, shot: ShotModel) {

        val gridWidth = canvas.width.toFloat() / BOARD_SIZE.toFloat()

        val startX = gridWidth * shot.x
        val startY = gridWidth * shot.y

        val endX = startX + gridWidth
        val endY = startY + gridWidth

        if (shot.isHit) {
            drawShipHit(startX, endX, startY, endY, canvas)
        } else {
            drawWaterHit(startX, endX, startY, endY, canvas)
        }
    }

    private fun drawShipHit(
        startX: Float,
        endX: Float,
        startY: Float,
        endY: Float,
        canvas: Canvas
    ) {
        val imageBounds = RectF(startX, startY, endX, endY)

        shipShotImage!!.bounds = imageBounds.toRect()
        shipShotImage.draw(canvas)
    }

    private fun drawWaterHit(
        startX: Float,
        endX: Float,
        startY: Float,
        endY: Float,
        canvas: Canvas
    ) {
        val imageBounds = RectF(startX, startY, endX, endY)

        waterShotImage!!.bounds = imageBounds.toRect()
        waterShotImage.draw(canvas)
    }
}
