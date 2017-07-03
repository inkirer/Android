package tgh.desapp;

/**
 * Created by alvaro on 16/12/2015.
 */
import android.content.Context;
import android.content.Intent;

import Constants.CLASE_NAME;

public class CLASES {
    public static Intent GetClass(String name, String params, Context context){

        Intent notificationIntent;

        switch (name){
            case CLASE_NAME.Registrar:
                notificationIntent =new Intent(context, Register.class);
                break;
            case CLASE_NAME.Login:
                notificationIntent =new Intent(context, Login.class);
                break;
            case CLASE_NAME.Evento:
                notificationIntent =new Intent(context, Eventos.class);
                break;
            case CLASE_NAME.Home:
                notificationIntent =new Intent(context, Home.class);
                break;
            case CLASE_NAME.CrearEvento:
                notificationIntent =new Intent(context, Create_Event.class);
                break;
            default:
                notificationIntent =new Intent(context, Home.class);
                break;
        }

        try {
            String[] par = params.split(",");
            for (int i = 0; par.length > i; i++) {
                notificationIntent.putExtra(par[i], par[i + 1]);
                i++;
            }
        }
        catch (Exception e){}

        return notificationIntent;
    }
}
