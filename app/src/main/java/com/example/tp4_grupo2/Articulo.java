package com.example.tp4_grupo2;

public class Articulo {
    private int id;
    private String nombre;
    private int stock;
    private int idCategoria;

    public Articulo(int id, String nombre, int stock, int idCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStock() {
        return stock;
    }

    public int getIdCategoria() {
        return idCategoria;
    }
}
