package ch.ffhs.esa.battleships.data.source.local.game

import androidx.room.*
import ch.ffhs.esa.battleships.data.game.Game
import ch.ffhs.esa.battleships.data.game.GameWithPlayerInfo

@Dao
interface GameDao {

    @Query("SELECT * from game where uid = :uid")
    fun findByUid(uid: String): Game?

    @Query(
        "select g.uid as gameUid, attacker.name as attackerName, " +
                "defender.name as defenderName, playerAtTurn.name as playerAtTurnName, " +
                "g.lastChangedAt as lastChangedAt, attacker.uid as attackerUid, " +
                "defender.uid as defenderUid, " +
                "g.winnerUid as winnerUid " +
                "from Game as g " +
                "inner join Player as defender on defender.uid = g.defenderUid " +
                "inner join Player as playerAtTurn on playerAtTurn.uid = g.playerAtTurnUid " +
                "left join Player as attacker on attacker.uid = g.attackerUid " +
                "where attacker.uid = :uid or defender.uid = :uid " +
                "and g.winnerUid is NULL " +
                "order by g.lastChangedAt desc"
    )
    fun findAllWithPlayerInfo(uid: String): List<GameWithPlayerInfo>

    @Query(
        "select g.uid as gameUid, attacker.name as attackerName, " +
                "defender.name as defenderName, playerAtTurn.name as playerAtTurnName, " +
                "g.lastChangedAt as lastChangedAt, attacker.uid as attackerUid, " +
                "defender.uid as defenderUid, " +
                "g.winnerUid as winnerUid " +
                "from Game as g " +
                "inner join Player as defender on defender.uid = g.defenderUid " +
                "inner join Player as playerAtTurn on playerAtTurn.uid = g.playerAtTurnUid " +
                "left join Player as attacker on attacker.uid = g.attackerUid " +
                "where attacker.uid = :uid or defender.uid = :uid " +
                "and g.winnerUid is not NULL " +
                "order by g.lastChangedAt desc"
    )
    fun findAllClosedWithPlayerInfo(uid: String): List<GameWithPlayerInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(game: Game)

    @Update
    suspend fun update(game: Game)
}
