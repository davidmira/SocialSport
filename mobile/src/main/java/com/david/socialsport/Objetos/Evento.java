package com.david.socialsport.Objetos;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by david on 03/04/2017.
 */

public class Evento implements Serializable {

    private String deporte, ubicacionEvento, tipoLugar, comentario, id;
    Date fecha_hora;
    Float precio;
    double latitude, longitude;


    public Evento() {
        //Es obligatorio incluir constructor por defecto
    }


    public Evento(String deporte, String ubicacionEvento, LatLng coordenadas, String tipoLugar, Float precio, Date fecha_hora, String comentario) {
        this.deporte = deporte;
        this.ubicacionEvento=ubicacionEvento;if(coordenadas == null){
            latitude = 0;
            longitude = 0;
        }else {
            latitude = coordenadas.latitude;
            longitude = coordenadas.longitude;
        }
        this.tipoLugar = tipoLugar;
        this.precio = precio;
        this.fecha_hora = fecha_hora;
        fecha_hora.setYear(fecha_hora.getYear() + 1900);
        this.comentario = comentario;
    }

    @Exclude
    public LatLng getCoordenadas() {
        return new LatLng(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
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

    @Exclude
    public Date getFecha_hora_menos1900() {
        Date d = new Date();
        d.setTime(fecha_hora.getTime());
        d.setYear(fecha_hora.getYear() - 1900);
        return d;
    }

}
