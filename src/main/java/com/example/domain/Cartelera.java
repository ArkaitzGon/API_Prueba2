package com.example.domain;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CARTELERA")
public class Cartelera {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	/*
    @ManyToOne
    @JoinColumn(name = "CINEID", nullable = false, referencedColumnName = "id")
    private Cine cine;

    @ManyToOne
    @JoinColumn(name = "PELICULAID", nullable = false, referencedColumnName = "id")
    private Pelicula pelicula;
    */
	@Column(name="CINEID")
    private int cineId;
	@Column(name="PELICULAID")
    private int peliculaId;
    @Column(name="HORARIO")
    private String horario;
    @Column(name="FECHA")
    private String fecha;
}
