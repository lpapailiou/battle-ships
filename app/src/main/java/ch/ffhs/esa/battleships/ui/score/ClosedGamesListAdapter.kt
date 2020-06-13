package ch.ffhs.esa.battleships.ui.score

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.ffhs.esa.battleships.business.score.ScoreViewModel
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import ch.ffhs.esa.battleships.databinding.ClosedGameItemBinding
import ch.ffhs.esa.battleships.ui.bridge.GameDiffCallback

class ClosedGamesListAdapter(
    private val viewModel: ScoreViewModel,
    private val itemClickCallback: (game: GameWithPlayerInfo) -> Unit
) :
    ListAdapter<GameWithPlayerInfo, ClosedGamesListAdapter.ViewHolder>(GameDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item, itemClickCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(private val binding: ClosedGameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            viewModel: ScoreViewModel,
            item: GameWithPlayerInfo,
            itemClickCallback: (game: GameWithPlayerInfo) -> Unit
        ) {

            binding.viewModel = viewModel
            binding.gameWithPlayerInfo = item
            binding.executePendingBindings()
            itemView.setOnClickListener {
                itemClickCallback(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ClosedGameItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}
