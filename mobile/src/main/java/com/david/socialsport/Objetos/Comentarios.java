package com.david.socialsport.Objetos;


/**
 * Created by david on 20/06/2017.
 */

public class Comentarios {
    private String idUsuario;
    private String idComentario;
    private String comentario;

    public Comentarios() {
    }



    public Comentarios(String idUsuario,String comentario) {
        this.idUsuario = idUsuario;
        this.comentario = comentario;

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
