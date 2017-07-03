package tgh.desapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    private Controller aController = null;

    public GCMIntentService() {
        // Call extended class Constructor GCMBaseIntentService
        super(Config.GOOGLE_SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        //Get Global Controller Class object (see application tag in AndroidManifest.xml)
        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Device registered: regId = " + registrationId);
        aController.displayMessageOnScreen(context,
                "Your device registred with GCM");
        Log.d("NAME", Register.apodo);
        //aController.register(context, Register.apodo,
        //        Register.email, registrationId);
    }

    /**
     * Method called on device unregistred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        if(aController == null)
            aController = (Controller) getApplicationContext();
        Log.i(TAG, "Device unregistered");
        aController.displayMessageOnScreen(context,
                getString(R.string.gcm_unregistered));
        //aController.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message from GCM server
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        if(aController == null)
            aController = (Controller) getApplicationContext();
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("msg");
        String intent1 = intent.getExtras().getString("intent");
        String params = intent.getExtras().getString("params");
        String titulo = intent.getExtras().getString("titulo");
        //aController.displayMessageOnScreen(context, message);
        // notifies user
        generateNotification(context, message, intent1, titulo, params);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        aController.displayMessageOnScreen(context, message);
        // notifies user
        //generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "Received error: " + errorId);
        aController.displayMessageOnScreen(context,
                getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        if(aController == null)
            aController = (Controller) getApplicationContext();
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        aController.displayMessageOnScreen(context,
                getString(R.string.gcm_recoverable_error,
                        errorId));
        return super.onRecoverableError(context, errorId);
    }


    /**
     * Create a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message, String intent, String titulo, String params) {

        Intent notificationIntent = CLASES.GetClass(intent, params, context);
        /*
        try {
            String[] par = params.split(",");
            for (int i = 0; par.length > i; i++) {
                notificationIntent.putExtra(par[i], par[i + 1]);
                i++;
            }
        }
        catch (Exception e){}
        */
        PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(titulo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }
}