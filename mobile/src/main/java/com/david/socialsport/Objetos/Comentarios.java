package com.david.socialsport.Objetos;


import com.google.firebase.database.Exclude;

import java.util.Date;

/**
 * Created by david on 20/06/2017.
 */

public class Comentarios {
    private String idUsuarioRemitente;
    private String idUsuarioRecibe;
    private String idComentario;
    private String comentario;
    private Boolean peticion;
    private Date fecha_hora;

    public Comentarios() {
    }



    public Comentarios(String idUsuarioRemitente,String comentario, Date fecha_hora) {
        this.idUsuarioRemitente = idUsuarioRemitente;
        this.comentario = comentario;
        this.fecha_hora=fecha_hora;
    }

    public Comentarios(String idUsuarioRemitente, String idUsuarioRecibe,String comentario, Date fecha_hora, Boolean peticion) {
        this.idUsuarioRemitente = idUsuarioRemitente;
        this.idUsuarioRecibe = idUsuarioRecibe;
        this.comentario = comentario;
        this.fecha_hora=fecha_hora;
        this.peticion=peticion;
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

    public String getIdUsuarioRemitente() {
        return idUsuarioRemitente;
    }

    public void setIdUsuarioRemitente(String idUsuarioRemitente) {
        this.idUsuarioRemitente = idUsuarioRemitente;
    }

    public String getIdUsuarioRecibe() {
        return idUsuarioRecibe;
    }

    public void setIdUsuarioRecibe(String idUsuarioRecibe) {
        this.idUsuarioRecibe = idUsuarioRecibe;
    }

    public Boolean getPeticion() {
        return peticion;
    }

    public void setPeticion(Boolean peticion) {
        this.peticion = peticion;
    }
}
