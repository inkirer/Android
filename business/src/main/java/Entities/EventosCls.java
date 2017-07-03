package Entities;

/**
 * Created by alvaro on 26/11/2015.
 */
public class EventosCls {
    private int id_evento;
    private int id_categoria;
    private String nombre;
    private String descripcion;
    private String hora;
    private String fecha;
    private int num_participantes;
    private int participantes;
    private String categoria;

    public EventosCls() {

    }

    public EventosCls(int id_evento, String nombre, String descripcion, String hora, String fecha){
        this.id_evento = id_evento;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.hora = hora;
        this.fecha = fecha;
    }

    public EventosCls(int id_evento, int id_categoria, String nombre, String descripcion, String hora, String fecha, int num_participantes, int participantes, String categoria) {
        this.id_evento = id_evento;
        this.id_categoria = id_categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.hora = hora;
        this.fecha = fecha;
        this.num_participantes = num_participantes;
        this.participantes = participantes;
        this.categoria = categoria;
    }

    public int getId_evento() {
        return id_evento;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getNum_participantes() {
        return num_participantes;
    }

    public void setNum_participantes(int num_participantes) {
        this.num_participantes = num_participantes;
    }

    public int getParticipantes() {
        return participantes;
    }

    public void setParticipantes(int participantes) {
        this.participantes = participantes;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }


}
