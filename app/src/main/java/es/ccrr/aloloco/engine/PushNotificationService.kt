package es.ccrr.aloloco.engine

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class PushNotificationService: FirebaseMessagingService() {
    val TAG: String = PushNotificationService::class.java.simpleName


    override fun handleIntent(var1: Intent) {
        var1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        super.handleIntent(var1)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Called when the App is in the Foreground
        Log.d(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data.
        if (remoteMessage.data.isNotEmpty()) Log.d(TAG, "Message data: " + remoteMessage.data)

        // Check if message contains a notification.
        if (remoteMessage.notification != null)
            Log.d(TAG, "Message Notification click_action = " + remoteMessage.notification!!.clickAction + " ; body: " + remoteMessage.notification!!.body)

//        if (remoteMessage.getData().size() > 0) {
//            Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
//            intent.putExtra(Constants.PUSH_ACTION, remoteMessage.getData().get("click_action"));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            startActivity(intent);
//        }
    }
}