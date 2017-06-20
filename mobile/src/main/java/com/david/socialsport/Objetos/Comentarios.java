package com.david.socialsport.Objetos;


/**
 * Created by david on 20/06/2017.
 */

public class Comentarios {
    private String id;
    private String nombre;
    private String imagen;
    private String comentario;

    public Comentarios() {
    }



    public Comentarios(String nombre, String imagen,String comentario) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.comentario = comentario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
