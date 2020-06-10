package ch.ffhs.esa.battleships.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import ch.ffhs.esa.battleships.R
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    companion object {
        var skipLogin: Boolean = false
        var navUid: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.main_activity)

            val navController = Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )

            setupActionBar(navController)

            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            navUid = auth.currentUser?.uid ?: ""

            if (!hasWifi()) {
                skipLogin = true
            }

        } catch (e: Exception) {
            println(e.stackTrace)
        }
    }

    open fun hasWifi(): Boolean {
        val connManager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifi: NetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return mWifi.isConnected
    }

    // ----------------------------- navigation -----------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val item: MenuItem? = menu?.getItem(2)
        if (item != null) {
//            mainViewModel.getCurrentGame().observe(this, Observer {           // remove 'current game' menu item if there is no current game
//                item.setVisible(it != null)
//                invalidateOptionsMenu()     // TODO: not clean yet, menu item is still visible when game was ended
//            })
        }

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.addAuthStateListener {
            val item: MenuItem? = menu?.getItem(0)
            if (auth.currentUser != null) {
                item?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_wifi_24)
            } else {
                item?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_wifi_off_24)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        try {
            super.onBackPressed()
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, container)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

