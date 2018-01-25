package mukul.authenticationsample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by mukul on 1/25/2018.
 */

public class AuthenticationHelper {

    public static void showDialog(CharSequence title, CharSequence mesg, CharSequence posBtnMesg, Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(mesg);
        builder.setPositiveButton(posBtnMesg, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // close the dialog
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
