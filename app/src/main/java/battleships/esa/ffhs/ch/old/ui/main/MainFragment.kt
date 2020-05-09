package battleships.esa.ffhs.ch.old.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.old.ui.main.MainActivity.Companion.isFirstLogin
import battleships.esa.ffhs.ch.old.viewmodel.MainViewModel
import battleships.esa.ffhs.ch.refactored.ui.bridge.BridgeFragment


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // hide NavigationDrawerIcon when we see main fragment, as we can't navigate back anywhere from here
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // initialize child fragments depending on 'do we know you already'
        initMainFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    // ----------------------------- fragment choice (depending on first startup) -----------------------------

    private fun initMainFragment() {
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_main) != null) {
            if (!isFirstLogin) {
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_main,
                        BridgeFragment(), "bridge").commit()
            } else {
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_main, IntroFragment(), "intro").commit()
            }
        }
    }

}
