package battleships.esa.ffhs.ch.ui.drawable

import android.app.AlertDialog
import android.content.Context
import battleships.esa.ffhs.ch.R

class CustomDialog {

    fun showEndGameDialog(context: Context, youWin: Boolean) {
        var dialog: AlertDialog.Builder = AlertDialog.Builder(context, R.style.AppDialogTheme)
        dialog.setTitle("THE WAR IS OVER")
        if (youWin) {
            dialog.setMessage("You are the best general!")
        } else {
            dialog.setMessage("Your wife will be very mad at you.")
        }
        dialog.show()
    }

}
