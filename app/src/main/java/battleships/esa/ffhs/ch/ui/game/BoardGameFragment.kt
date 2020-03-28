package battleships.esa.ffhs.ch.ui.game

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.drawable.BoardMine
import battleships.esa.ffhs.ch.ui.main.MainActivity
import kotlinx.android.synthetic.main.board_game_fragment.*
import kotlinx.android.synthetic.main.board_mine_fragment.*


class BoardGameFragment : Fragment() {

    var screenShot: ImageView? = null
    var fragmentBoardMine: BoardMineFragment? = null
    var fragmentBoardOther: BoardOtherFragment? = null
    var activeFragment: BoardGameChildFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.board_game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize child fragments depending on 'do we know you already'
        initGameFragments()
        switchBoard.setOnClickListener {
            switchFragments()
        }
    }

    // fragment_container_game_active
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    private fun initGameFragments() {
        fragmentBoardMine = BoardMineFragment()
        fragmentBoardOther = BoardOtherFragment()
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_game_active) != null) {
            childFragmentManager.beginTransaction().replace(R.id.fragment_container_game_active, fragmentBoardMine!!, "mine").commit()
        }
        activeFragment = fragmentBoardMine
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_game_inactive) != null) {
            childFragmentManager.beginTransaction().replace(R.id.fragment_container_game_inactive, fragmentBoardOther!!, "other").commit()
        }
    }

    // will be accessed as well from child fragments
    fun switchFragments() {
        println("switch fragment")
        if (activeFragment == fragmentBoardMine) {
            println("switch fragment if")
            boardMine.translationX
            boardMine.scaleX = 20f
            activeFragment = fragmentBoardOther
        } else {
            println("switch fragment else")
            childFragmentManager.beginTransaction().replace(R.id.fragment_container_game_active, fragmentBoardMine!!, "mine").commit()
            childFragmentManager.beginTransaction().replace(R.id.fragment_container_game_inactive, fragmentBoardOther!!, "other").commit()
            activeFragment = fragmentBoardMine
        }
    }

    fun screenShot(view: View) {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        screenShot?.setImageBitmap(bitmap)
        //switchBoard.background = setImageDrawable(screenShot) // somehow set screenshot of other board as button
    }

}
