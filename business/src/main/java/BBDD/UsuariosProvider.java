package BBDD;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import Constants.UrlsAPI;

/**
 * Created by alvaro on 13/11/2015.
 */
public class UsuariosProvider extends BBDDbase {

    public static JSONObject json;

    public static void InsertarUsuario(final OnJSONResponseCallback callback, String nombre, String apodo, String password, String email, String imagen, String GCM_ID) {
        String url = UrlsAPI.USUARIO;
        AsyncHttpClient Client = new AsyncHttpClient();

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("NAME", nombre);
            jsonParams.put("NICKNAME", apodo);
            jsonParams.put("PASSWORD", password);
            jsonParams.put("EMAIL", email);
            jsonParams.put("IMAGEN", imagen);
            jsonParams.put("GCM_ID", GCM_ID);
        } catch (JSONException e) {

        }
        StringEntity entity;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            entity = null;
        }
        Client.post(null, UrlsAPI.USUARIO, entity, "application/json", new AsyncHttpResponseHandler() {
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

    public static void LoginUsuario(final OnJSONResponseCallback callback, String apodo, String password){
        String url = UrlsAPI.USUARIO + "/" + apodo + "/" + password;

        AsyncHttpClient Client = new AsyncHttpClient();

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
                    jsonEr.put("ERRORES", statusCode + ": " + content);
                    callback.onJSONResponse(true, jsonEr);
                } catch (JSONException e1) {
                }
            }
        });
    }

    public static void GetUserData(final OnJSONResponseCallback callback, String token, String user_id){
        String url = UrlsAPI.USUARIO + "/" + user_id;
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
