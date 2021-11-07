package com.example.reto_one.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Publicacion implements Serializable {

    private String nombreEvento, nombreNegocio, fotoNegocio;
    private String ubicaciónLetra;
    private double lat;
    private double lng;

    private String fechaInicio;
    private String fechaFin;

    public Publicacion(String nombreEvento, String nombreNegocio, String fotoNegocio, String ubicaciónLetra, double lat, double lng, String fechaInicio, String fechaFin) {
        this.nombreEvento = nombreEvento;
        this.nombreNegocio = nombreNegocio;
        this.fotoNegocio = fotoNegocio;
        this.ubicaciónLetra = ubicaciónLetra;
        this.lat = lat;
        this.lng = lng;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public String getFotoNegocio() {
        return fotoNegocio;
    }

    public void setFotoNegocio(String fotoNegocio) {
        this.fotoNegocio = fotoNegocio;
    }


    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getUbicaciónLetra() {
        return ubicaciónLetra;
    }

    public void setUbicaciónLetra(String ubicaciónLetra) {
        this.ubicaciónLetra = ubicaciónLetra;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
