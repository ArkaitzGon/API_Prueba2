package com.example.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CineCartelera {
    private int id;
    private String latitud;
    private String longitud;
    private String nombre;
    private List<PeliculaCartelera> peliculas;

    public CineCartelera(int id, String latitud, String longitud, String nombre) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
    }
}
