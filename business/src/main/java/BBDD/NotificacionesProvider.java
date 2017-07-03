package BBDD;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import Constants.UrlsAPI;

/**
 * Created by alvaro on 15/12/2015.
 */
public class NotificacionesProvider extends BBDDbase {

    public static JSONObject json;

    public static void EnviarNotificacion(final OnJSONResponseCallback callback, String mensage, String REG_ID,
                                          String intent, String titulo, String params) throws JSONException {
        String url = UrlsAPI.NOTIFICACION;
        AsyncHttpClient Client = new AsyncHttpClient();
        //Client.addHeader("Authorization", "key=AIzaSyA5UlFtzGHloPq-bdgGRa6mJQoMRnraNTQ");
        //Client.addHeader("Content-Type", "application/json; charset=utf-8");

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("REG_ID", REG_ID);
            jsonParams.put("MENSAGE", mensage);
            jsonParams.put("TITULO", titulo);
            jsonParams.put("INTENT", intent);
            jsonParams.put("PARAMS", params);
        } catch (JSONException e) {}

        StringEntity entity;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            entity = null;
        }

        Client.post(null, url, entity, "application/json", new AsyncHttpResponseHandler() {
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
