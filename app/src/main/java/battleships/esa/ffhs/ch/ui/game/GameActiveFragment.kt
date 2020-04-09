package battleships.esa.ffhs.ch.ui.game

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.main.MainActivity
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.activeGame


class GameActiveFragment : Fragment() {

    var screenShot: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.game_active_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGameFragments()
    }


    private fun initGameFragments() {
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_game_active) != null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_active, GameBoardMineFragment(), "mine").commit()
        }
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_game_inactive) != null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_inactive, GameBoardOpponentFragment(), "other")
                .commit()
        }
    }

    // will be accessed as well from child fragments
    fun switchFragments() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return
        }
        if (activeGame!!.isActivePlayerMe) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_active, GameBoardOpponentFragment(), "other")
                .commit()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_inactive, GameBoardMineFragment(), "mine")
                .commit()
            activeGame!!.isActivePlayerMe = false
        } else {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_active, GameBoardMineFragment(), "mine").commit()
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_game_inactive, GameBoardOpponentFragment(), "other")
                .commit()
            activeGame!!.isActivePlayerMe = true
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
