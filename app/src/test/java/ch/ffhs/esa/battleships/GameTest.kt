package ch.ffhs.esa.battleships


import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.game.GameViewModel
import ch.ffhs.esa.battleships.business.ship.DirectionLogic
import ch.ffhs.esa.battleships.data.board.BoardDataSource
import ch.ffhs.esa.battleships.data.board.BoardRepository
import ch.ffhs.esa.battleships.data.game.GameRepository
import ch.ffhs.esa.battleships.data.player.PlayerRepository
import ch.ffhs.esa.battleships.data.ship.ShipRepository
import ch.ffhs.esa.battleships.data.shot.ShotRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class GameTest {

    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = -1
        private const val UNDEF = 0
    }

    private var gameResult = UNDEF

    @Mock
    private lateinit var gameViewModel: GameViewModel

    @Before
    fun setUp() {
        //MockitoAnnotations.initMocks(this)
        val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        val gameRepository = Mockito.mock(GameRepository::class.java)
        val playerRepository = Mockito.mock(PlayerRepository::class.java)
        val boardDataSource = Mockito.mock(BoardDataSource::class.java)
        val remoteBoardDataSource = Mockito.mock(BoardDataSource::class.java)
        val boardRepository = BoardRepository(boardDataSource, remoteBoardDataSource, ioDispatcher)
        val shipRepository = Mockito.mock(ShipRepository::class.java)
        val shotRepository = Mockito.mock(ShotRepository::class.java)
        val directionLogic = DirectionLogic()

        gameViewModel = GameViewModel(
            gameRepository, playerRepository, boardRepository,
            shipRepository, shotRepository, directionLogic
        )
    }

    @Test
    fun startGame(){
        val thread : Job = Job()
        Mockito.`when`(gameViewModel.start("1234", BOT_PLAYER_ID, BOT_PLAYER_ID))
            .thenReturn(thread)
        gameViewModel.start("1234", BOT_PLAYER_ID, BOT_PLAYER_ID)
        assert(gameResult == SUCCESS)
        }
}
