package ch.ffhs.esa.battleships.data.source.remote.ship

import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.ship.Ship
import ch.ffhs.esa.battleships.data.ship.ShipDataSource
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RemoteShipDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShipDataSource {

    private val database = Firebase.database.reference

    override suspend fun insert(ship: Ship): DataResult<String> =
        withContext(ioDispatcher) {
            if (ship.uid.isEmpty()) {
                return@withContext DataResult.Error(Exception("Ship does not have an Uid assigned"))
            }

            val task = database.child("board").child(ship.boardUid!!).child("ship").child(ship.uid)
                .setValue(ship)
            task.await()

            if (task.isSuccessful) {
                return@withContext DataResult.Success("Success")
            }

            return@withContext DataResult.Error(task.exception!!)
        }

    override suspend fun loadByBoard(boardUid: String): DataResult<List<Ship>> {
        TODO("Not yet implemented")
    }


}
