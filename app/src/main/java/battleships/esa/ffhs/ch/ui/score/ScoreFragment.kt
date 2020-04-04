package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R

class ScoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.score_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setList()
    }

    // ----------------------------- list view for currently active games -----------------------------

    // temporary handler for static gui testing
    fun setList() {
        val itemsAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                (activity as MainActivity),
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.staticscoredata)
            )
        val listView: ListView =
            (activity as MainActivity).findViewById(R.id.scoreListId) as ListView
        listView.adapter = itemsAdapter
    }


}
