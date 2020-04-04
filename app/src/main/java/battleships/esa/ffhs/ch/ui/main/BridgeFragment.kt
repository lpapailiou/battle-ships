package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R


class BridgeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bridge_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setList()
    }

    // ----------------------------- list view for currently active games -----------------------------

    // temporary handler for static gui testing
    fun setList() {
        val itemsAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                (activity as MainActivity),
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.staticgamedata)
            )
        val listView: ListView = (activity as MainActivity).findViewById(R.id.listId) as ListView
        listView.adapter = itemsAdapter
    }

}
