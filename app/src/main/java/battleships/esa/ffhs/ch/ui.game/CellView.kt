package battleships.esa.ffhs.ch.ui.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.components.Direction

class SquareCell: View {

    private val borderPaint: Paint

    private val rect: Rect = Rect()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        borderPaint = initPaint(R.color.colorAccent, Paint.Style.STROKE, 2f)
    }

    private fun initPaint(@ColorRes color: Int, style: Paint.Style = Paint.Style.FILL, width: Float? = null, cap: Paint.Cap? = null): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = ContextCompat.getColor(context, color)
            this.style = style
            width?.let { this.strokeWidth = it }
            cap?.let { this.strokeCap = it }
        }
    }

    override fun onDraw(canvas: Canvas) {
        rect.left = 0
        rect.top = 0
        rect.bottom = canvas.height
        rect.right = canvas.width

        canvas.drawRect(rect, borderPaint)
    }
}