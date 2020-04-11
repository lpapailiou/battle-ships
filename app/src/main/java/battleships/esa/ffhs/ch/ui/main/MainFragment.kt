package battleships.esa.ffhs.ch.ui.main

import MainViewModel
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.isFirstLogin
import battleships.esa.ffhs.ch.ui.viewmodel.GameListViewModel


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

    private fun adaptMenuItems() {
        val factory = InjectorUtils.provideGameViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory).get(GameListViewModel::class.java)
        viewModel.getGames().observe(viewLifecycleOwner, Observer { games ->
            if (games.filter { game -> game.isActive()}.count() == 0) {

            }
        })
    }

    // ----------------------------- fragment choice (depending on first startup) -----------------------------

    private fun initMainFragment() {
        if ((activity as MainActivity).findViewById<View>(R.id.fragment_container_main) != null) {
            if (!isFirstLogin) {
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_main, BridgeFragment(), "bridge").commit()
            } else {
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_main, IntroFragment(), "intro").commit()
            }
        }
    }

}
