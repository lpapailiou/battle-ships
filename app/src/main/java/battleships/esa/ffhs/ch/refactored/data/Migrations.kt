package battleships.esa.ffhs.ch.refactored.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import battleships.esa.ffhs.ch.old.model.BOT_PLAYER_ID
import battleships.esa.ffhs.ch.old.model.OFFLINE_PLAYER_ID

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "INSERT INTO Player values(null, :offlinePlayerId, 'You')",
            arrayOf(OFFLINE_PLAYER_ID)
        )
        database.execSQL(
            "INSERT INTO Player values(null, :botPlayerId, 'BOT')",
            arrayOf(BOT_PLAYER_ID)
        )
    }
}


