package ch.ffhs.esa.battleships.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class ConnectivityListener @Inject constructor(
    private val database: FirebaseDatabase
) {

    var connected = false;

    fun observeConnectivity() {

        val connectedRef = database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    Log.d("BattleShips/ConnectivityListener", "connected")
                } else {
                    Log.d("BattleShips/ConnectivityListener", "disconnected")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("BattleShips/ConnectivityListener", "Listener was cancelled")
            }
        })
    }

}
