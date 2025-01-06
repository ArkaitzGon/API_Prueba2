package com.example.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Usuario;
import com.example.domain.Votos;
import com.example.dto.ValoracionDTO;
import com.example.repository.UsuarioRepository;
import com.example.repository.VotosRepository;

@RestController
@CrossOrigin(origins = "http://localhost:8100, http://localhost:8101")
@RequestMapping("/api/votos")
public class VotosController {

	@Autowired
	VotosRepository votosRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping({"/",""}) 
	public List <Votos> index() {
		return votosRepository.findAll();
	}
	
	//Devuelve pelicula por ID
	@GetMapping("/{id}")
	public Votos show(@PathVariable("id") int id) { 
		return votosRepository.findById(id).orElse(null);
	}
	
	//Crear pelicula
	@PostMapping({""})
	@ResponseStatus (HttpStatus.CREATED)
	public ValoracionDTO creaVotos(@RequestBody ValoracionDTO valoracion, Principal principal) {  
		Optional<Usuario> usuario = usuarioRepository.findByEmail(principal.getName());
		
		if(usuario.isPresent()) {
			Votos voto = new Votos(
					valoracion.getPeliculaId(),
					usuario.get().getId(),
					valoracion.getPuntuacion()
					);
			
			votosRepository.save(voto);
			
			
			Optional<List<Votos>> listaVotos = votosRepository.findByPeliculaId(valoracion.getPeliculaId());
			
			int sumatorio = 0;
			
			if(listaVotos.isPresent()) {
				for (Votos valor : listaVotos.get()) {
					sumatorio += valor.getPuntuacion();
				}
				
				valoracion.setVotado(true);
				valoracion.setPuntuacion(Math.round(sumatorio / listaVotos.get().size()));
				return valoracion;
			}
			
			
		}
		
		valoracion.setPuntuacion(-1);
		return valoracion;
	}
	
	/***
	 * Borramos una lista
	 * Le pasamos por parametro el ID
	 * **/
	@DeleteMapping("/{id}")
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public void borraLista(@PathVariable("id") int id) {
		votosRepository.deleteById(id);
	}
	
	/**
	 * Actualizamos una lista dependiendo de su id
	 * **/
	@PutMapping("/{id}")
	@ResponseStatus (HttpStatus.CREATED)
	public Votos actualizaLista(@RequestBody Votos voto, @PathVariable("id") int id) {
		Votos actuVoto = votosRepository.findById(id).orElse(null);
		
		actuVoto.setPeliculaId(voto.getPeliculaId());
		actuVoto.setUsuarioId(voto.getUsuarioId());
		actuVoto.setPuntuacion(voto.getPuntuacion());
		
		return votosRepository.save(actuVoto);
	}
}
