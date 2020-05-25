package ch.ffhs.esa.battleships.ui.bridge

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<GameWithPlayerInfo>) {
    (listView.adapter as ActiveGamesListAdapter).submitList(items)
}
