package com.example.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.example.domain.Cartelera;
import com.example.domain.Cine;
import com.example.domain.Pelicula;
import com.example.dto.CarteleraFecha;
import com.example.dto.CineCartelera;
import com.example.dto.PeliculaCartelera;
import com.example.repository.CarteleraRepository;
import com.example.repository.CineRepository;
import com.example.repository.PeliculaRepository;

import service.CarteleraService;

@RestController
@CrossOrigin(origins = "http://localhost:8100, http://localhost:8101")
@RequestMapping("/api/cartelera")
public class CarteleraController {

	@Autowired
	CarteleraRepository carteleraRepository;
	@Autowired
	PeliculaRepository peliculaRepository;
	@Autowired
	CineRepository cineRepository;
	/*@Autowired
	CarteleraService carteleraService;*/
	
	/*
	@GetMapping({"/",""}) 
	public List <Cartelera> index() {
		return carteleraRepository.findAll();
	}
	*/
	//Devuelve cartelera por ID
	@GetMapping("/{id}")
	public Cartelera showCartelera(@PathVariable("id") int id) { 
		return  carteleraRepository.findById(id).orElse(null);
	}
	
	@GetMapping("")
	public CarteleraFecha index() {
		LocalDate fechaActual = LocalDate.now(); // Guarda la fecha actual
		
		// Formatear a string con el formato YYYY-MM-DD
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);
		
	    return new CarteleraFecha(fechaFormateada, getCartelera()); 
	}
	
	public List<CineCartelera> getCartelera(){
        List<CineCartelera> values = new ArrayList<>();
        List<Cine> cines = cineRepository.findAll();
        cines.forEach(cine ->{
                CineCartelera cineCartelera =  new CineCartelera(
                        cine.getId(),
                        cine.getLatitud(),
                        cine.getLongitud(),
                        cine.getNombre()
                );
                cineCartelera.setPeliculas(this.getPeliculas(cine.getId()));
                values.add(cineCartelera);
        });
        return values;
    }

    public List<PeliculaCartelera> getPeliculas(int cineId){
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);
        
        List<PeliculaCartelera> peliculas = new ArrayList<>();
        List<Cartelera> cartelera = carteleraRepository.findByCineIdAndFecha(
                cineId, fechaFormateada
        );
        if(!cartelera.isEmpty()){
            cartelera.forEach(cart ->{
                Pelicula pelicula = peliculaRepository.findById(cart.getPeliculaId()).orElse(null);
                if(pelicula != null){
                    PeliculaCartelera pc = new PeliculaCartelera(
                            pelicula.getId(),
                            pelicula.getTitulo(),
                            pelicula.getImagen(),
                            pelicula.getAnchoImagen(),
                            pelicula.getAltoImagen()
                            );
                    pc.setHorario(cart.getHorario().split(","));
                    peliculas.add(pc);
                }
            });
        }
        return peliculas;
    }
	
	/********
	 * Devuelve un array de peliculas que estan en cartelera
	 * **/
	@GetMapping("pelisCartel")
	public List<Pelicula> showPeliculasCartelera(){
		//Recogemos todas las carteleras para sacar el id de la pelicula
		List<Cartelera> listaCartelera = carteleraRepository.findAll();
		
		//Recogemos los id de las peliculas
		ArrayList<Integer> listaId = new ArrayList<>();;
		
		for (Cartelera cartelera : listaCartelera) {
			//Comprobamos que el id no se encuentra dentro del array
			if(!listaId.contains(cartelera.getPeliculaId())) {
				listaId.add(cartelera.getPeliculaId());
			}
		}
		
		//Creamos y añadimos las peliculas a la lista que devolvemos
		List<Pelicula> listaPeliculas = new ArrayList<Pelicula>();
		
		for (int id : listaId) {
			Optional<Pelicula> peli = peliculaRepository.findById(id);
			
			if(peli.isPresent()) {
				listaPeliculas.add(peli.get());
			}
		}
		
		return listaPeliculas;
		
	}
	
	//Crear cartelera
	@PostMapping({""})
	@ResponseStatus (HttpStatus.CREATED)
	public Cartelera creaCartelera(@RequestBody Cartelera cartelera) {  
		return carteleraRepository.save(cartelera);
	}
	
	/***
	 * Borramos una cartelera
	 * Le pasamos por parametro el ID
	 * **/
	@DeleteMapping("/{id}")
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public void borraCartelera(@PathVariable("id") int id) {
		carteleraRepository.deleteById(id);
	}
	
	/**
	 * Actualizamos una cartelera dependiendo de su id
	 * **/
	@PutMapping("/{id}")
	@ResponseStatus (HttpStatus.CREATED)
	public Cartelera actualizaCartelera(@RequestBody Cartelera cartelera, @PathVariable("id") int id) {
		Cartelera actuCartelera = carteleraRepository.findById(id).orElse(null);
		
		actuCartelera.setCineId(cartelera.getCineId());
		actuCartelera.setPeliculaId(cartelera.getPeliculaId());
		actuCartelera.setHorario(cartelera.getHorario());
		actuCartelera.setFecha(cartelera.getFecha());

		
		return carteleraRepository.save(actuCartelera);
	}
}
