package Entities;

import android.graphics.Bitmap;

/**
 * Created by alvaro on 18/12/2015.
 */
public class CategoriaCls {

    private int id_categoria;
    private String nom_categoria;
    private Bitmap Imagen;
    private int Numero;

    public CategoriaCls(){}

    public CategoriaCls(int id_categoria, String nom_categoria, Bitmap imagen, int numero) {
        this.id_categoria = id_categoria;
        this.nom_categoria = nom_categoria;
        this.Imagen = imagen;
        Numero = numero;
    }

    public String getNom_categoria() {
        return nom_categoria;
    }

    public void setNom_categoria(String nom_categoria) {
        this.nom_categoria = nom_categoria;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public Bitmap getImagen() {
        return Imagen;
    }

    public void setImagen(Bitmap imagen) {
        Imagen = imagen;
    }

    public int getNumero() {
        return Numero;
    }

    public void setNumero(int numero) {
        Numero = numero;
    }
}
