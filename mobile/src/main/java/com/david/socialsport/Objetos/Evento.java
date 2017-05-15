package com.david.socialsport.Objetos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by david on 03/04/2017.
 */

public class Evento {

    private String deporte, localizacion, ubicacionEvento, tipoLugar, comentario;
    Date fecha_hora;
    Float precio;


    public Evento() {
        //Es obligatorio incluir constructor por defecto
    }

    public Evento(String deporte, String localizacion, String ubicacionEvento, String tipoLugar, Date fecha_hora, Float precio) {
        this.deporte = deporte;
        this.localizacion = localizacion;
        this.ubicacionEvento = ubicacionEvento;
        this.tipoLugar = tipoLugar;
        this.fecha_hora = fecha_hora;
        this.precio = precio;
    }

    public Evento(String deporte, String localizacion, String ubicacionEvento, String tipoLugar, String comentario, Date fecha_hora, Float precio) {
        this.deporte = deporte;
        this.localizacion = localizacion;
        this.ubicacionEvento = ubicacionEvento;
        this.tipoLugar = tipoLugar;
        this.comentario = comentario;
        this.fecha_hora = fecha_hora;
        this.precio = precio;
    }



    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getUbicacionEvento() {
        return ubicacionEvento;
    }

    public void setUbicacionEvento(String ubicacionEvento) {
        this.ubicacionEvento = ubicacionEvento;
    }

    public String getTipoLugar() {
        return tipoLugar;
    }

    public void setTipoLugar(String tipoLugar) {
        this.tipoLugar = tipoLugar;
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

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

}
