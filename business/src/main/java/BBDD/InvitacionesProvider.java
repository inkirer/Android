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
public class InvitacionesProvider extends BBDDbase {

    public static JSONObject json;

    public static void InsertarInvitacion(final OnJSONResponseCallback callback, int id_evento, int invitado,
                                      int invitador, String token, String reg_id) {
        String url = UrlsAPI.INVITACIONES;
        AsyncHttpClient Client = new AsyncHttpClient();

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("ID_EVENTO", id_evento);
            jsonParams.put("INVITADO", invitado);
            jsonParams.put("INVITADOR", invitador);
            jsonParams.put("REG_ID", reg_id);
        } catch (JSONException e) {

        }
        StringEntity entity;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            entity = null;
        }
        Client.addHeader("Authorization", token);
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
                    jsonEr.put("ERRORES", statusCode + ": "+ content);
                    callback.onJSONResponse(true, jsonEr);
                } catch (JSONException e1) {
                }
            }
        });
    }
}
