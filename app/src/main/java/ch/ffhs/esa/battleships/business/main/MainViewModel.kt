package ch.ffhs.esa.battleships.business.main

import androidx.lifecycle.ViewModel
import ch.ffhs.esa.battleships.data.game.GameRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

}
