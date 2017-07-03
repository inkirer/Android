package BBDD;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import Constants.UrlsAPI;

/**
 * Created by alvaro on 25/11/2015.
 */
public class EventosProvider extends BBDDbase {

    public static JSONObject json;

    public static void GetAllEventos(final OnJSONResponseCallback callback, String token, String id_cat){
        String url = UrlsAPI.EVENTO + "/cat/"+ id_cat;
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

    public static void GetEventoByID(final OnJSONResponseCallback callback, String id_evento, String token){
        String url = UrlsAPI.EVENTO + "/" + id_evento;
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

    public static void InsertarEvento(final OnJSONResponseCallback callback, String nombre, String fecha,
                                       String tipo_pago, String id_categoria, int id_provincia, String num_participantes,
                                       String descripcion, String precio, String hora, String user_id, String token) {
        AsyncHttpClient Client = new AsyncHttpClient();

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("NOM_EVENTO", nombre);
            jsonParams.put("FECHA_INICIO", fecha);
            jsonParams.put("TIPO_PAGO", tipo_pago);
            jsonParams.put("ID_CATEGORIA", id_categoria);
            jsonParams.put("ID_PROVINCIA", id_provincia);
            jsonParams.put("NUM_PARTICIPANTES", num_participantes);
            jsonParams.put("DESCRIPCION", descripcion);
            jsonParams.put("PRECIO", precio);
            jsonParams.put("USER_ID", user_id);
            jsonParams.put("HORA_INICIO", hora);
        } catch (JSONException e) {

        }
        StringEntity entity;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            entity = null;
        }
        Client.addHeader("Authorization", token);
        Client.post(null, UrlsAPI.EVENTO, entity, "application/json", new AsyncHttpResponseHandler() {
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
