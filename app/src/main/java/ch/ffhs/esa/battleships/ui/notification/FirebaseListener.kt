package ch.ffhs.esa.battleships.ui.notification

import android.R
import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.isThisOnForeGround
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navOwnPlayerId
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService


class FirebaseListener: FirebaseMessagingService() {

    private val title = "Ahoy!"
    private val text = "Captain! The enemy may have moved!"

    final fun listen(activity: Activity) {

        var database  = FirebaseDatabase.getInstance()
        var ref  = database.getReference("player").child(navOwnPlayerId).child("game")
        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d("firebaseListener", "a game was changed in firebase")
                if (!isThisOnForeGround) {
                    NotificationUtil()
                        .createNotification(activity, title, text)
                } else {
                    if (p0.child("playerAtTurnUid").getValue().toString()
                            .equals(navOwnPlayerId)
                    ) {
                        val toast =
                            Toast.makeText(activity.applicationContext, text, Toast.LENGTH_LONG)
                        toast.view.setBackgroundColor(Color.parseColor("#FA021F"))
                        toast.view.findViewById<TextView>(R.id.message)
                            .setTextColor(Color.WHITE)
                        toast.show()
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                // nothing should happen
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                // nothing should happen
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                // nothing should happen
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebaseListener", "could not read from firebase", error.toException())
            }
        })
    }

}