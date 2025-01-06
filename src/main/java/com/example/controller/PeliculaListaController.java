package com.example.controller;

import java.security.Principal;
import java.util.ArrayList;
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

import com.example.domain.Lista;
import com.example.domain.PeliculaLista;
import com.example.domain.PeliculaListaId;
import com.example.domain.Usuario;
import com.example.repository.ListaRepository;
import com.example.repository.PeliculaListaRepository;
import com.example.repository.UsuarioRepository;

@RestController
@CrossOrigin(origins = "http://localhost:8100, http://localhost:8101")
@RequestMapping("/api/pelicula_lista")
public class PeliculaListaController {

	@Autowired
	PeliculaListaRepository peliculaListaRepository;
	@Autowired
	ListaRepository listaRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping({"/",""}) 
	public List <PeliculaLista> index() {
		return peliculaListaRepository.findAll();
	}
	
	//Devuelve pelicula por ID
	@GetMapping("/id/{listaId}/{peliculaId}")
	public PeliculaLista show(@PathVariable("usuarioId") int listaId, @PathVariable("peliculaId") int peliculaId) { 
		PeliculaListaId id = new PeliculaListaId(listaId, peliculaId);
		return peliculaListaRepository.findById(id).orElse(null);
	}
	
	//Crear pelicula
	// Y devuelve una lista de listas del usuario
	@PostMapping({""})
	@ResponseStatus (HttpStatus.CREATED)
	public List<Lista> creaPeliculaLista(@RequestBody PeliculaLista peliculaLista, Principal principal) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(principal.getName());
		
		if(usuario.isPresent()) {
			peliculaListaRepository.save(peliculaLista);
			
			Optional<List <Lista>> listas = listaRepository.findByUsuario(usuario.get());
			if(listas.isPresent()) {
				return listas.get();
			}
		}
		return new ArrayList<Lista>();
	}
	
	/***
	 * Borramos una lista
	 * Le pasamos por parametro el ID
	 * **/
	@DeleteMapping("/{listaId}/{peliculaId}")
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public List<Lista> borraLista(@PathVariable("usuarioId") int listaId, @PathVariable("peliculaId") int peliculaId, Principal principal) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(principal.getName());
		
		if(usuario.isPresent()) {
			PeliculaListaId id = new PeliculaListaId(listaId, peliculaId);
			peliculaListaRepository.deleteById(id);
			
			Optional<List <Lista>> listas = listaRepository.findByUsuario(usuario.get());
			if(listas.isPresent()) {
				return listas.get();
			}
		}
		return new ArrayList<Lista>();
	}
	
	/***
	 * Borramos una lista
	 * Le pasamos por parametro el ID
	 * **/
	/*@DeleteMapping("/{listaId}/{peliculaId}")
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public void borraLista(@PathVariable("usuarioId") int listaId, @PathVariable("peliculaId") int peliculaId) {
		PeliculaListaId id = new PeliculaListaId(listaId, peliculaId);
		peliculaListaRepository.deleteById(id);
	}*/
	
	/**
	 * Actualizamos una lista dependiendo de su id
	 * **/
	@PutMapping("/{listaId}/{peliculaId}")
	@ResponseStatus (HttpStatus.CREATED)
	public PeliculaLista actualizaLista(@RequestBody PeliculaLista peliculaLista, @PathVariable("listaId") int listaId, @PathVariable("peliculaId") int peliculaId) {
		PeliculaListaId id = new PeliculaListaId(listaId, peliculaId);
		PeliculaLista actuPeliLista = peliculaListaRepository.findById(id).orElse(null);
		
		actuPeliLista.setPeliculaId(peliculaLista.getPeliculaId());
		actuPeliLista.setListaId(peliculaLista.getListaId());
		
		return peliculaListaRepository.save(actuPeliLista);
	}
}
