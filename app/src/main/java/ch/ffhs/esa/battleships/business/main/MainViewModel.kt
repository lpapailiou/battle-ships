package ch.ffhs.esa.battleships.business.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.game.GameRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

}
