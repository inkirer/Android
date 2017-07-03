package tgh.desapp;

public interface Config {
    // CONSTANTS
    String YOUR_SERVER_URL = "http://des.thegeekhammer.com/api/gcm";

    // Google project id
    String GOOGLE_SENDER_ID = "333494503321";

    /**
     * Tag used on log messages.
     */
    String TAG = "GCM Android Example";

    String DISPLAY_MESSAGE_ACTION = "tgh.desapp.DISPLAY_MESSAGE";

    String EXTRA_MESSAGE = "message";
}