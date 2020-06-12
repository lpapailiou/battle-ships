package ch.ffhs.esa.battleships.data.source.remote.ship

import android.util.Log
import ch.ffhs.esa.battleships.business.FIREBASE_BOARD_PATH
import ch.ffhs.esa.battleships.data.DataResult
import ch.ffhs.esa.battleships.data.ship.Ship
import ch.ffhs.esa.battleships.data.ship.ShipDataSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RemoteShipDataSource internal constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val firebaseDatabase: FirebaseDatabase
) : ShipDataSource {

    override suspend fun insert(ship: Ship): DataResult<String> =
        withContext(ioDispatcher) {
            if (ship.uid.isEmpty()) {
                return@withContext DataResult.Error(Exception("Ship does not have an Uid assigned"))
            }

            val task = firebaseDatabase.reference.child(FIREBASE_BOARD_PATH).child(ship.boardUid!!)
                .child("ship")
                .child(ship.uid)
                .setValue(ship)
            task.await()

            if (task.isSuccessful) {
                return@withContext DataResult.Success("Success")
            }

            return@withContext DataResult.Error(task.exception!!)
        }

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override suspend fun loadByBoard(boardUid: String): DataResult<List<Ship>> =
        withContext(ioDispatcher) {

            val flow = callbackFlow<List<Ship>> {
                val callback = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val games = dataSnapshot.children
                            .mapNotNull { it.getValue(Ship::class.java) }


                        offer(games)
                        channel.close()
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Log.e(null, "remote shipt data source cancelled - loadByBoard")
                        throw p0.toException()
                    }
                }

                firebaseDatabase.reference.child(FIREBASE_BOARD_PATH).child(boardUid).child("ship")
                    .addListenerForSingleValueEvent(callback)

                awaitClose { firebaseDatabase.reference.removeEventListener(callback) }

                return@callbackFlow
            }

            var ships: List<Ship> = listOf()
            flow.collect(object : FlowCollector<List<Ship>> {
                override suspend fun emit(value: List<Ship>) {

                    ships = value
                }
            })

            return@withContext DataResult.Success(ships)
        }

}
