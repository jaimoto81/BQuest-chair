package co.bquest.silla.messages;

import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import co.bquest.silla.R;
import co.bquest.silla.messages.c2dm.C2DMBaseReceiver;
import co.bquest.silla.messages.main.activities.MessagesActivity;
import co.bquest.silla.messages.main.definitions.Consts;

/**
 * Broadcast receiver that handles Android Cloud to Data Messaging (AC2DM) messages, initiated
 * by the JumpNote App Engine server and routed/delivered by Google AC2DM servers. The
 * only currently defined message is 'sync'.
 */
public class C2DMReceiver extends C2DMBaseReceiver {
    static final String LOG_TAG = "C2DMRECEIVER";

    public C2DMReceiver() {
        super(Consts.GSM_SENDER);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.e( LOG_TAG, "onError: "+errorId );
        Toast.makeText(context, "Messaging registration error: " + errorId,
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String message = intent.getExtras().getString("message");
        

        Log.d(LOG_TAG, "onMessage: "+message );


        // post notification
        Intent intent2 = new Intent(this, MessagesActivity.class);
        intent2.putExtra("message", message);
        NotificationManager mManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_notification,
                "La Silla Vacia", System.currentTimeMillis());
        notification.setLatestEventInfo(context,"La Silla",message, PendingIntent.getActivity(this.getBaseContext(), 0,
                intent2, PendingIntent.FLAG_CANCEL_CURRENT));
        notification.defaults |= Notification.DEFAULT_SOUND;
        mManager.notify(0, notification);
        //notification.flags = Notification.FLAG_AUTO_CANCEL;
        // show message  on text view
        if(MessagesActivity.getInstance() != null){
            final String msg = message;
            MessagesActivity.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MessagesActivity.getInstance().retrieveMessage(msg);
                }
            });
        }
    }

    @Override
    public void onRegistered(Context context, final String registrationId) throws IOException {
        Log.d(LOG_TAG, "onRegistered() registrationId is "+registrationId);

        // ================= QuickBlox ===== Step 4 =================
        // Create push token
        if(MessagesActivity.getInstance() != null){
            MessagesActivity.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MessagesActivity.getInstance().createPushToken(registrationId);
                }
            });
        }
    }

    @Override
    public void onUnregistered(Context context) {
        Log.d(LOG_TAG, "onUnregistered");
    }
}
