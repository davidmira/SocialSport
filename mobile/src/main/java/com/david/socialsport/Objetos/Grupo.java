package com.david.socialsport.Objetos;

import java.util.HashMap;
import java.util.List;

/**
 * Created by david on 10/07/2017.
 */

public class Grupo {
    private String id;
    private String nombre;
    private String imagen;
    private HashMap<String,Usuario> usuarios;

    private List idUsuarios;
    public Grupo(){

    }


    public Grupo(String id, String nombre, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
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

    public HashMap<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(HashMap<String, Usuario> usuarios) {
        this.usuarios = usuarios;
    }


    public List getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(List idUsuarios) {
        this.idUsuarios = idUsuarios;
    }
}
