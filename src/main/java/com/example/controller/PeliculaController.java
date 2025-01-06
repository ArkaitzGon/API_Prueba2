package com.example.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Pelicula;
import com.example.repository.PeliculaRepository;

@RestController
@CrossOrigin(origins = "http://localhost:8100, http://localhost:8101")
@RequestMapping("/api/peliculas")
public class PeliculaController {

	@Autowired
	PeliculaRepository peliculaRepository;
	
	/**
	 * Devuelve una con todas las peliculas
	 * Es bastante lento
	 * **/
	@GetMapping({"/",""}) 
	public List <Pelicula> index() {
		return peliculaRepository.findAll();
	}
	
	//Devuelve pelicula por ID
	@GetMapping("/{id}")
	public Pelicula show(@PathVariable("id") int id) { 
		/*
		Pelicula peli = peliculaRepository.findById(id).orElse(null);
		return peli
		*/
		
		return peliculaRepository.findById(id).orElse(null);
	}
	
	@GetMapping("/buscar/genero_reparto_fecha_titulo")
	public List<Pelicula> buscarPorGeneroRepartoFechaEstrenoYNombre(
	        @RequestParam(value = "genero", required = false) String genero,
	        @RequestParam(value = "reparto", required = false) String reparto,
	        @RequestParam(value = "fechaEstreno", required = false) Integer fechaEstreno,
	        @RequestParam(value = "titulo", required = false) String titulo) {
	    // Llama al repositorio con todos los parámetros
	    return peliculaRepository.findByGeneroRepartoFechaEstrenoYTitulo(genero, reparto, fechaEstreno, titulo);
	}
	
	//Crear pelicula
	@PostMapping({""})
	@ResponseStatus (HttpStatus.CREATED)
	public Pelicula creaPelicula(@RequestBody Pelicula peli) {  
		return peliculaRepository.save(peli);
	}

	
	/***
	 * Borramos una pelicula
	 * Le pasamos por parametro el ID
	 * **/
	@DeleteMapping("/{id}")
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public void borraPelicula(@PathVariable("id") int id) {
		peliculaRepository.deleteById(id);
	}
	
	/**
	 * Actualizamos una pelicula dependiendo de su id
	 * **/
	@PutMapping("/{id}")
	@ResponseStatus (HttpStatus.CREATED)
	public Pelicula actualizaPelicula(@RequestBody Pelicula pelicula, @PathVariable("id") int id) {
		Pelicula actuPeli = peliculaRepository.findById(id).orElse(null);
		
		actuPeli.setTitulo(pelicula.getTitulo());
		actuPeli.setImagen(pelicula.getImagen());
		actuPeli.setFechaEstreno(pelicula.getFechaEstreno());
		actuPeli.setGenero(pelicula.getGenero());
		actuPeli.setReparto(pelicula.getReparto());
		actuPeli.setResumen(pelicula.getResumen());
		actuPeli.setAltoImagen(pelicula.getAltoImagen());
		actuPeli.setAnchoImagen(pelicula.getAnchoImagen());

		
		return peliculaRepository.save(actuPeli);
	}



	//Devuelva una lista con todos los generos diferentes
	
	@GetMapping("/generos")
    public List<String> obtenerGeneros() {
        // Obtener todas las películas
        List<Pelicula> peliculas = peliculaRepository.findAll();
        
        // Lista para almacenar los géneros
        List<String> generos = new ArrayList<>();
        
        //Recorremos la lista de peliculas para conseguir los generos
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getGenero() != null && !pelicula.getGenero().isEmpty()) {
            	//Dividimos el string por cada coma para conseguir los generos de la pelicula
                String[] generosArray = pelicula.getGenero().split(",");
                for (String genero : generosArray) {
                	//Conseguimos los generos de cada pelicula
                    String generoTrimmed = genero.trim();
                    //Añade el genero si no esta ya en la lista
                    if (!generos.contains(generoTrimmed)) {
                        generos.add(generoTrimmed);
                    }
                }
            }
        }
        
        return generos;
    }


}
