package ch.ffhs.esa.battleships.ui.bridge

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.ffhs.esa.battleships.R
import ch.ffhs.esa.battleships.business.bridge.BridgeViewModel
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo
import ch.ffhs.esa.battleships.databinding.ActiveGameItemBinding
import ch.ffhs.esa.battleships.ui.bridge.ActiveGamesListAdapter.ViewHolder
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navOwnPlayerId

class ActiveGamesListAdapter(
    private val viewModel: BridgeViewModel,
    private val itemClickCallback: (game: GameWithPlayerInfo) -> Unit
) :
    ListAdapter<GameWithPlayerInfo, ViewHolder>(GameDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item, itemClickCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(private val binding: ActiveGameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            viewModel: BridgeViewModel,
            item: GameWithPlayerInfo,
            itemClickCallback: (game: GameWithPlayerInfo) -> Unit
        ) {

            binding.viewModel = viewModel
            binding.gameWithPlayerInfo = item
            binding.executePendingBindings()
            itemView.setOnClickListener {
                itemClickCallback(item)
            }

            val uName = navOwnPlayerId
            if ((item.attackerUid != null && item.defenderUid != null) && ((uName.equals(item.attackerUid) && item.playerAtTurnName.equals(item.attackerName)) || (uName.equals(item.defenderUid) && item.playerAtTurnName.equals(item.defenderName)) || item.playerAtTurnName.equals("You"))) {
                val turnText = itemView.findViewById<TextView>(R.id.turn)
                turnText.text = "(your turn)"
                turnText.setTextColor(Color.parseColor("#FA021F"))
            }
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
