package ch.ffhs.esa.battleships.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ch.ffhs.esa.battleships.business.BOT_PLAYER_ID
import ch.ffhs.esa.battleships.business.OFFLINE_PLAYER_ID

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "INSERT INTO Player values(:offlinePlayerUid, 'You')",
            arrayOf(OFFLINE_PLAYER_ID)
        )
        database.execSQL(
            "INSERT INTO Player values(:botPlayerUid, 'BOT')",
            arrayOf(BOT_PLAYER_ID)
        )
    }
}


