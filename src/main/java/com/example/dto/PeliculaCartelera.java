package com.example.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class  PeliculaCartelera {
     private int id;
     private String titulo;
     private String imagen;
     private int anchoImagen;
     private int altoImagen;
     private String[] horario;


     public PeliculaCartelera(int id, String titulo, String imagen, int anchoImagen, int altoImagen) {
          this.id = id;
          this.titulo = titulo;
          this.imagen = imagen;
          this.anchoImagen = anchoImagen;
          this.altoImagen = altoImagen;
     }
}
