package BBDD;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import Constants.UrlsAPI;

/**
 * Created by alvaro on 18/12/2015.
 */
public class CategoriasProvider extends BBDDbase{

    public static JSONObject json;

    public static void GetAllCategorys(final OnJSONResponseCallback callback, String token){
        String url = UrlsAPI.CATEGORIAS;
        AsyncHttpClient Client = new AsyncHttpClient();
        Client.addHeader("Authorization", token);
        Client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    json = new JSONObject(response);
                    callback.onJSONResponse(true, json);

                } catch (JSONException e) {
                    JSONObject jsonEr = new JSONObject();
                    try {
                        jsonEr.put("ERRORES", "Error al parsear");
                        callback.onJSONResponse(true, jsonEr);
                    } catch (JSONException e1) {
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                JSONObject jsonEr = new JSONObject();
                try {
                    jsonEr.put("NAME", statusCode + ": " + content);
                    callback.onJSONResponse(true, jsonEr);
                } catch (JSONException e1) {
                }
            }
        });
    }
}
