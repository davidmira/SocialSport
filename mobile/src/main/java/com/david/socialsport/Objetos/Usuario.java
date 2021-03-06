package com.david.socialsport.Objetos;

import java.util.HashMap;

/**
 * Created by david on 03/04/2017.
 */

public class Usuario {
    private String id;
    private String nombre;
    private String imagen;
    private HashMap<String, Boolean> eventos;
    private String admin;
    private String edad;
    private String sexo;
    private String idAmigo;
    private String idEvento;



    public Usuario() {
    }

    public  Usuario(String idAmigo){
        this.idAmigo=idAmigo;
    }
    public Usuario(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }
    public Usuario(String nombre, String imagen, String edad, String sexo) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.edad = edad;
        this.sexo=sexo;
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

    public HashMap<String, Boolean> getEventos() {
        return eventos;
    }

    public void setEventos(HashMap<String, Boolean> eventos) {
        this.eventos = eventos;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(String idAmigo) {
        this.idAmigo = idAmigo;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }
}
