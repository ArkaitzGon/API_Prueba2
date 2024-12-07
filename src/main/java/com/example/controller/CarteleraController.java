package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Cartelera;
import com.example.domain.Usuario;
import com.example.repository.CarteleraRepository;

@RestController
@RequestMapping("/api/cartelera")
public class CarteleraController {

	@Autowired
	CarteleraRepository carteleraRepository;
	
	@GetMapping({"/",""}) 
	public List <Cartelera> index() {
		return carteleraRepository.findAll();
	}
	
	//Devuelve cartelera por ID
	@GetMapping("/id/{id}")
	public Cartelera showCartelera(@PathVariable("id") int id) { 
		return  carteleraRepository.findById(id).orElse(null);
	}
	
	//Crear cartelera
	@PostMapping({"crea"})
	@ResponseStatus (HttpStatus.CREATED)
	public Cartelera creaCartelera(@RequestBody Cartelera cartelera) {  
		return carteleraRepository.save(cartelera);
	}
	
	/***
	 * Borramos una cartelera
	 * Le pasamos por parametro el ID
	 * **/
	@DeleteMapping("borra/{id}")
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public void borraCartelera(@PathVariable("id") int id) {
		carteleraRepository.deleteById(id);
	}
	
	/**
	 * Actualizamos una cartelera dependiendo de su id
	 * **/
	@PutMapping("actualiza/{id}")
	@ResponseStatus (HttpStatus.CREATED)
	public Cartelera actualizaCartelera(@RequestBody Cartelera cartelera, @PathVariable("id") int id) {
		Cartelera actuCartelera = carteleraRepository.findById(id).orElse(null);
		
		actuCartelera.setCineid(cartelera.getCineid());
		actuCartelera.setPeliculaid(cartelera.getPeliculaid());
		actuCartelera.setHorario(cartelera.getHorario());

		
		return carteleraRepository.save(actuCartelera);
	}
}
