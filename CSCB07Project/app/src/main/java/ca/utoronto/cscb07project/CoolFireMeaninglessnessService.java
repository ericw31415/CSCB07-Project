package ca.utoronto.cscb07project;

import android.app.Service;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CoolFireMeaninglessnessService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Message received: " + remoteMessage.getData());

        // Add your logic to handle the received message
        // You can log or process the message as needed
    }
}


