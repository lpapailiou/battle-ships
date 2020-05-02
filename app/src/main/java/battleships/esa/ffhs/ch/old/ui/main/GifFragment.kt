package battleships.esa.ffhs.ch.old.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import battleships.esa.ffhs.ch.R

class GifFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.gif_fragment, container, false)
    }

    // not sure yet where/how to use gif. therefore, temporarily used as child fragment
}
