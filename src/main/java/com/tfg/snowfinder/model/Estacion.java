package com.tfg.snowfinder.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estaciones")
public class Estacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String pais;
    private Double latitud;
    private Double longitud;
    private Integer kmPistas;
    private String descripcion;
    private Integer altitud;

    @OneToMany(mappedBy = "estacion", cascade = CascadeType.ALL)
    private List<Imagen> imagenes = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public Integer getKmPistas() { return kmPistas; }
    public void setKmPistas(Integer kmPistas) { this.kmPistas = kmPistas; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getAltitud() { return altitud; }
    public void setAltitud(Integer altitud) { this.altitud = altitud; }
    public List<Imagen> getImagenes() { return imagenes; }
    public void setImagenes(List<Imagen> imagenes) { this.imagenes = imagenes; }
}