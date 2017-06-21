package com.david.socialsport.Objetos;


import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * Created by david on 20/06/2017.
 */

public class Comentarios {
    private String idUsuario;
    private String idComentario;
    private String comentario;
    Date fecha_hora;

    public Comentarios() {
    }



    public Comentarios(String idUsuario,String comentario, Date fecha_hora) {
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.fecha_hora=fecha_hora;
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

    public Date getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Date fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

}
