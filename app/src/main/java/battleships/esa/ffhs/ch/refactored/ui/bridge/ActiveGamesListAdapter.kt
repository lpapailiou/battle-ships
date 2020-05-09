package battleships.esa.ffhs.ch.refactored.ui.bridge

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import battleships.esa.ffhs.ch.refactored.business.bridge.BridgeViewModel
import battleships.esa.ffhs.ch.refactored.data.game.Game

class ActiveGamesListAdapter(private val viewModel: BridgeViewModel) : ListAdapter<GameModel> {

    override fun isEmpty(): Boolean {

    }

}
