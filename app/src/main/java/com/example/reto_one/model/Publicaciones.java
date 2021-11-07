package com.example.reto_one.model;

import java.util.ArrayList;

public class Publicaciones {

    private ArrayList<Publicacion> publicaciones;

    public Publicaciones(ArrayList<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public ArrayList<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(ArrayList<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }
}
