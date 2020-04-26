package battleships.esa.ffhs.ch.refactored.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import battleships.esa.ffhs.ch.refactored.data.DataResult
import battleships.esa.ffhs.ch.refactored.data.board.Board
import battleships.esa.ffhs.ch.refactored.data.board.BoardRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class BoardViewModel @Inject constructor(private val boardRepository: BoardRepository) :
    ViewModel() {

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board> = _board


    fun start(boardId: Int) {
        loadBoard(boardId)
    }


    private fun loadBoard(boardId: Int) {
        viewModelScope.launch {
            val result = boardRepository.findById(boardId)
            if (result is DataResult.Success) {
                _board.value = result.data
            }
        }
    }
}
