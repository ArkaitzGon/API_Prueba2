package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValoracionDTO {

	private boolean votado;
	private int puntuacion;
	private int peliculaId;
	
	
	
}
