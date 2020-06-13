package ch.ffhs.esa.battleships.ui.bridge

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import ch.ffhs.esa.battleships.ui.score.ClosedGamesListAdapter

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<GameWithPlayerInfo>) {
    if (listView.adapter is ActiveGamesListAdapter) {
        (listView.adapter as ActiveGamesListAdapter).submitList(items)
    } else if (listView.adapter is ClosedGamesListAdapter) {
        (listView.adapter as ClosedGamesListAdapter).submitList(items)
    }
}
