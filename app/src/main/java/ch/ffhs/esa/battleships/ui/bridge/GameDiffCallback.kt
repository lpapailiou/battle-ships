package ch.ffhs.esa.battleships.ui.bridge

import androidx.recyclerview.widget.DiffUtil
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo

class GameDiffCallback : DiffUtil.ItemCallback<GameWithPlayerInfo>() {
    override fun areItemsTheSame(
        oldItem: GameWithPlayerInfo,
        newItem: GameWithPlayerInfo
    ): Boolean {
        return oldItem.gameUid == newItem.gameUid
    }

    override fun areContentsTheSame(
        oldItem: GameWithPlayerInfo,
        newItem: GameWithPlayerInfo
    ): Boolean {
        return oldItem == newItem
    }
}
