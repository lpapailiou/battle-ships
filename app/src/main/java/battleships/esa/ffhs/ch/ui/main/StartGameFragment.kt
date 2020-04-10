package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.entity.GameInstance
import battleships.esa.ffhs.ch.entity.InjectorUtils
import battleships.esa.ffhs.ch.model.Game
import battleships.esa.ffhs.ch.ui.main.MainActivity.Companion.isFirstLogin
import battleships.esa.ffhs.ch.ui.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.intro_fragment.*
import kotlinx.android.synthetic.main.start_game_fragment.*


class StartGameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.start_game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ----------------------------- button change listeners -----------------------------

        offline_button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_boardFragment)
        )

        online_button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_boardFragment)
        )

    }

}
