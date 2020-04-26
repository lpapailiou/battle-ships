package battleships.esa.ffhs.ch.old.ui.rules

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.old.ui.main.MainActivity


class RulesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.rules_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setText()
    }

    // TODO: maybe change to html view so images / screenshots could be embedded easily
    // TODO: must be scrollable
    // TODO: replace text (now: Llorem ipusm)
    fun setText() {
        val string: String = getString(R.string.rulesText)
        val TextView: TextView = (activity as MainActivity).findViewById(R.id.rulesT) as TextView
        TextView.text = Html.fromHtml(string)
    }
}
