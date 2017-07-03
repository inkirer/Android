package BBDD;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alvaro on 25/11/2015.
 */
public class BBDDbase {
    public interface OnJSONResponseCallback {
        void onJSONResponse(boolean success, JSONObject response) throws JSONException;
    }
}
