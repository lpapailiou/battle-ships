package ch.ffhs.esa.battleships.ui.bridge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.ffhs.esa.battleships.databinding.ActiveGameItemBinding
import ch.ffhs.esa.battleships.business.bridge.BridgeViewModel
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import ch.ffhs.esa.battleships.ui.bridge.ActiveGamesListAdapter.ViewHolder

class ActiveGamesListAdapter(private val viewModel: BridgeViewModel) :
    ListAdapter<GameWithPlayerInfo, ViewHolder>(GameDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ActiveGameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: BridgeViewModel, item: GameWithPlayerInfo) {

            binding.viewModel = viewModel
            binding.gameWithPlayerInfo = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ActiveGameItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}