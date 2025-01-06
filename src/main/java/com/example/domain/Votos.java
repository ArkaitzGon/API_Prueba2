package com.example.domain;

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
@Entity // declaramos la clase como entidad
@Table(name="VOTOS")
public class Votos {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de ID
    private int id;
	@Column(name="PELICULAID")
	private int peliculaId;
	@Column(name="USUARIOID")
	private int usuarioId;
	@Column(name="PUNTUACION")
	private int puntuacion;
	
	public Votos(int peliculaID, int usuarioId, int puntuacion) {
		this.peliculaId = peliculaID;
		this.usuarioId = usuarioId;
		this.puntuacion = puntuacion;
	}
}
