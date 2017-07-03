package Constants;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

public class PreferenciasManager {

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USER_ID = "user_id";
    public static final String TOKEN = "token";

    public static void GuardarPreferencia(String Key, String Value, Context context){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(Key, encrypt(Value))
                .commit();
    }

    public static String ObtenerPreferencia(String Key, Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return decrypt(pref.getString(Key, null));
    }

    public static void BorrarPreferencia(String Key, Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().remove(Key).commit();
    }

    public static String encrypt(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
}
