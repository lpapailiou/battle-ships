package ch.ffhs.esa.battleships.ui.main

import android.R
import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.isThisOnForeGround
import ch.ffhs.esa.battleships.ui.main.MainActivity.Companion.navOwnPlayerId
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService


class FirebaseListener(paramActivity: Activity): FirebaseMessagingService() {

    private val activity = paramActivity
    private val title = "Ahoy!"
    private val text = "Captain! The enemy may have moved!"

    fun listen() {

        var database  = FirebaseDatabase.getInstance()
        var usersRef  = database.getReference("player").child(navOwnPlayerId);
        usersRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("firebaseListener", "a game was changed in firebase")
                if (!isThisOnForeGround) {
                    NotificationUtil().createNotification(activity, title, text)
                } else {
                    val toast = Toast.makeText(activity.applicationContext, text, Toast.LENGTH_LONG)
                    toast.view.setBackgroundColor(Color.parseColor("#FA021F"))
                    toast.view.findViewById<TextView>(R.id.message).setTextColor(Color.WHITE)
                    toast.show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebaseListener", "could not read from firebase", error.toException())
            }
        })
    }

}