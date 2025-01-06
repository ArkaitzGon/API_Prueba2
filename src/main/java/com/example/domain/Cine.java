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
@Table(name="CINE")
public class Cine {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de ID
    private int id;
	@Column(name="LATITUD")
	private String latitud;
	@Column(name="LONGITUD")
	private String longitud;
	@Column(name="URL")
	private String url;
	@Column(name="NOMBRE")
	private String nombre;
}
