package com.example.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.server.ResponseStatusException;

import com.example.domain.Lista;
import com.example.domain.Usuario;
import com.example.repository.ListaRepository;
import com.example.repository.UsuarioRepository;

@RestController
@CrossOrigin(origins = "http://localhost:8100, http://localhost:8101")
@RequestMapping("/api/lista")
public class ListaController {

	@Autowired
	ListaRepository listaRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	/*
	@GetMapping({"/",""}) 
	public List <Lista> index() {
		return listaRepository.findAll();
	}
	*/
	//Devuelve pelicula por ID
	@GetMapping("/{id}")
	public Lista show(@PathVariable("id") int id) { 
		return listaRepository.findById(id).orElse(null);
	}
	
	
	//Metodo para devolver las listas de un usuario
	//Devuelve una lista de listas
	@GetMapping("")
	public List <Lista> index(Principal principal) {
		/*System.out.println(principal.getName());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		System.out.println(currentPrincipalName);*/
		Optional<Usuario> usuario = usuarioRepository.findByEmail(principal.getName());
		
		
		if(usuario.isPresent()) {
			Optional<List <Lista>> listas = listaRepository.findByUsuario(usuario.get());
			if(listas.isPresent()) {
				return listas.get();
			}
		}
		return new ArrayList();
	}
	
	//Crear pelicula
	/*@PostMapping({"crea"})
	@ResponseStatus (HttpStatus.CREATED)
	public Lista creaLista(@RequestBody Lista lista) {  
		return listaRepository.save(lista);
	}*/
	
	/*****
	 * Crea una lista
	 * Le pasamos el parametro Principal que trae la informacion del usuario
	 */
	@PostMapping("")
	@ResponseStatus (HttpStatus.CREATED)
	public List<Lista> creaLista(@RequestBody Lista lista, Principal principal){
		Optional<Usuario> usuario = usuarioRepository.findByEmail(principal.getName());
		
		if(usuario.isPresent()) {
			listaRepository.save(lista);
			
			Optional<List <Lista>> listas = listaRepository.findByUsuario(usuario.get());
			
			if(listas.isPresent()) {
				return listas.get();
			}
		}
		
		return new ArrayList<Lista>();
	}
	
	/***
	 * Borramos una lista
	 * Le pasamos por parametro el ID y un Principal con informacion del usuario
	 * **/
	@DeleteMapping("/{id}")
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public List<Lista> borraLista(@PathVariable("id") int id, Principal principal) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(principal.getName());
		
		if (usuario.isPresent()){
			listaRepository.deleteById(id);
			
			Optional<List <Lista>> listas = listaRepository.findByUsuario(usuario.get());
			
			if(listas.isPresent()) {
				return listas.get();
			}else {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lista no encontrada o no pertenece al usuario");
	        }
		}
		return new ArrayList<Lista>();
	}
	
	/**
	 * Actualizamos una lista dependiendo de su id
	 * **/
	@PutMapping("/{id}")
	@ResponseStatus (HttpStatus.CREATED)
	public Lista actualizaLista(@RequestBody Lista lista, @PathVariable("id") int id) {
		Lista actuLista = listaRepository.findById(id).orElse(null);
		
		actuLista.setUsuario(lista.getUsuario());
		actuLista.setNombre(lista.getNombre());
		
		return listaRepository.save(actuLista);
	}
}
