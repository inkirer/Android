package Entities;

import android.graphics.Bitmap;

/**
 * Created by alvaro on 13/11/2015.
 */
public class UsuariosCls {

    private int user_id;
    private String nombre;
    private String apodo;
    private String email;
    private String token;
    private String error;
    private String gcm_id;
    private Bitmap imagen;

    public UsuariosCls() {

    }
    public UsuariosCls(int user_id, String nombre, String apodo,String gcm_id, Bitmap imagen) {
        this.user_id = user_id;
        this.nombre = nombre;
        this.apodo = apodo;
        this.gcm_id = gcm_id;
        this.imagen = imagen;
    }

    public UsuariosCls(int user_id, String nombre, String apodo, String email, String token, String error, String gcm_id, Bitmap imagen) {
        this.user_id = user_id;
        this.nombre = nombre;
        this.apodo = apodo;
        this.email = email;
        this.token = token;
        this.error = error;
        this.gcm_id = gcm_id;
        this.imagen = imagen;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
