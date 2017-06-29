package com.david.socialsport.Objetos;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by david on 29/06/2017.
 */

public class Chat {
    private String idChat;
    private String miembros;
    private String mensajes;
    private Date hora;

    public Chat() {
    }

    public Chat(String idChat, String miembros, String mensajes, Date hora) {
        this.idChat = idChat;
        this.miembros = miembros;
        this.mensajes = mensajes;
        this.hora = hora;
    }

    public Chat(String miembros, String mensajes, Date hora) {
        this.miembros = miembros;
        this.mensajes = mensajes;
        this.hora = hora;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getMiembros() {
        return miembros;
    }

    public void setMiembros(String miembros) {
        this.miembros = miembros;
    }

    public String getMensajes() {
        return mensajes;
    }

    public void setMensajes(String mensajes) {
        this.mensajes = mensajes;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }
}
