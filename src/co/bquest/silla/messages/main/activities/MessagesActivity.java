package co.bquest.silla.messages.main.activities;

import java.util.ArrayList;

import org.apache.cordova.DroidGap;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.bquest.silla.R;
import co.bquest.silla.messages.c2dm.C2DMessaging;
import co.bquest.silla.messages.main.definitions.Consts;

import com.quickblox.core.QBCallback;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.result.Result;
import com.quickblox.internal.core.helper.StringifyArrayList;
import com.quickblox.module.messages.QBMessages;
import com.quickblox.module.messages.model.QBEnvironment;
import com.quickblox.module.messages.model.QBEvent;
import com.quickblox.module.messages.model.QBNotificationType;
import com.quickblox.module.messages.model.QBPushType;
import com.quickblox.module.messages.result.QBSubscribeToPushNotificationsResult;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;
import com.quickblox.module.users.result.QBUserPagedResult;

public class MessagesActivity extends DroidGap {

    static final String LOG_TAG = "MessagesActivity";

    private static MessagesActivity instance;

    ArrayList<QBUser> qbUsersList;
    private QBUser selectedUser;

    private TextView selectedUserLabel;
    private ProgressBar progressBar;
    private EditText messageBody;
    private EditText retrievedMessages;

    // return instances
    public static MessagesActivity getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	super.setIntegerProperty("loadUrlTimeoutValue", 60000);
    	super.loadUrl("file:///android_asset/www/index.html");

        QBMessages.getEvents(new QBCallbackImpl() {
        	@Override
        	public void onComplete(Result result) {
        		System.out.println(result);
        		
        	}
        });  
        
        instance = this;
        // add messages to list
        String message = getIntent().getStringExtra("message");
        if (message != null) {
            retrieveMessage(message);
        }
       
    	
    }

    @Override
    protected void onStart() {
        super.onStart();

        // ================= QuickBlox ===== Step 3 =================
        // Request device push token
        C2DMessaging.register(this, Consts.GSM_SENDER);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unregister for gcm
        C2DMessaging.unregister(this);
    }


    // retrieve message
    public void retrieveMessage(final String message) {
        String text = message + "\n" + retrievedMessages.getText().toString();
        retrievedMessages.setText(text);
        progressBar.setVisibility(View.INVISIBLE);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        super.loadUrl("file:///android_asset/www/spec.html");
        //notificationManager.cancelAll();
    }


    // select user
    public void selectUserButtonClick(View view) {

        if (qbUsersList != null) {
            showAllUsersPicker();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Retrieve all users
        QBUsers.getUsersByTags("moto" ,new QBCallback() {
            @Override
            public void onComplete(Result result) {
                result.toString();
            }

            @Override
            public void onComplete(Result result, Object o) {
            }
        });

        
        // Retrieve all users
        QBUsers.getUsersByTags("moto",new QBCallback() {
            @Override
            public void onComplete(Result result) {
                qbUsersList = ((QBUserPagedResult) result).getUsers();
                showAllUsersPicker();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete(Result result, Object o) {
            }
        });
    }

    private void showAllUsersPicker() {
        ArrayList<CharSequence> usersNames = new ArrayList<CharSequence>();
        for (QBUser qbUser : qbUsersList) {
            String login = qbUser.getLogin();
            usersNames.add(login);
        }

        final CharSequence[] items = usersNames.toArray(new CharSequence[usersNames.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a user");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                selectedUser = qbUsersList.get(item);
                selectedUserLabel.setText(selectedUser.getLogin());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // send message
    public void sendMessageButtonClick(View view) {

        // Send Push: create QuickBlox Push Notification Event
        QBEvent qbEvent = new QBEvent();
        qbEvent.setNotificationType(QBNotificationType.PUSH);
        qbEvent.setPushType(QBPushType.GCM);
        qbEvent.setEnvironment(QBEnvironment.PRODUCTION);
        qbEvent.setMessage(messageBody.getText().toString());
        StringifyArrayList<Integer> userIds = new StringifyArrayList<Integer>();
        userIds.add(selectedUser.getId());
        qbEvent.setUserIds(userIds);
        QBMessages.createEvent(qbEvent, new QBCallback() {
            @Override
            public void onComplete(Result result) {
            	System.out.println(result);

            }

            @Override
            public void onComplete(Result result, Object o) {
            	System.out.println(result);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
    }

    //
    //
    // create Push Token
    public void createPushToken(String registrationID) {
        //Create push token with  Registration Id for Android
        //
        Log.d("createPushToken", "createPushToken");

        String deviceId = ((TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        QBMessages.subscribeToPushNotificationsTask(registrationID, deviceId, QBEnvironment.PRODUCTION, new QBCallbackImpl() {
            @Override
            public void onComplete(Result result) {
                if (result.isSuccess()) {
                    QBSubscribeToPushNotificationsResult subscribeToPushNotificationsResult = (QBSubscribeToPushNotificationsResult) result;
                    System.out.println(">>> subscription created" + subscribeToPushNotificationsResult.getSubscriptions().toString());
                }
            }
        });
    }

}
