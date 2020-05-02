//package battleships.esa.ffhs.ch.old.ui.game
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import battleships.esa.ffhs.ch.R
//import battleships.esa.ffhs.ch.old.model.GameState
//import battleships.esa.ffhs.ch.old.ui.main.MainActivity
//import battleships.esa.ffhs.ch.old.viewmodel.GameViewModel
//import battleships.esa.ffhs.ch.refactored.ui.boardpreparation.BoardPreparationFragment
//
//
//class GameFragment : Fragment() {
//
//    companion object {
//        lateinit var currentGame: GameViewModel
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.game_fragment, container, false)
//    }
//
//    override fun onPause() {
//        super.onPause()
////        mainViewModel.save(currentGame)
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // initialize child fragments depending on 'are you already logged in'
//        initBoardFragment()
//    }
//
//
//    // check if the current game is ready to play or if the 'game preparation area' should be loaded
//    private fun initBoardFragment() {
//        if ((activity as MainActivity).findViewById<View>(R.id.game_fragment_container) != null) {
//            if (currentGame.notEqualsState(GameState.ACTIVE)) {
//                childFragmentManager.beginTransaction()
//                    .replace(R.id.game_fragment_container,
//                        BoardPreparationFragment(), "prep")
//                    .commit()
//            } else {
//                switchToGameFragment()
//            }
//        }
//    }
//
//    // will be accessed as well from child fragment to start a game (GamePreparationFragment to GameActiveFragment)
//    fun switchToGameFragment() {
//        childFragmentManager.beginTransaction()
//            .replace(R.id.game_fragment_container, GameActiveFragment(), "game").commit()
//    }
//}
