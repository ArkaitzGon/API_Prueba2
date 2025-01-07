package com.example.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Cine;
import com.example.repository.CineRepository;

import com.example.service.CarteleraService;

@RestController
@CrossOrigin(origins = "http://localhost:8100, http://localhost:8101")
@RequestMapping("/api/cine")
public class CineController {

	@Autowired
	CineRepository cineRepository;
	@Autowired
	CarteleraService carteleraService; // Para cargar los cines en la bbdd
	
	@GetMapping({"/",""}) 
	public List <Cine> index() {
		return cineRepository.findAll();
	}
	
	//Devuelve pelicula por ID
	@GetMapping("id/{id}")
	public Cine showCine(@PathVariable("id") int id) { 
		return cineRepository.findById(id).orElse(null);
	}
	
	//Crear cartelera
	@PostMapping({"crea"})
	@ResponseStatus (HttpStatus.CREATED)
	public Cine creaCine(@RequestBody Cine cine) {  
		return cineRepository.save(cine);
	}
	
	/***
	 * Borramos una cartelera
	 * Le pasamos por parametro el ID
	 * **/
	@DeleteMapping("borra/{id}")
	@ResponseStatus (HttpStatus.NO_CONTENT)
	public void borraCine(@PathVariable("id") int id) {
		cineRepository.deleteById(id);
	}
	
	/**
	 * Actualizamos una cartelera dependiendo de su id
	 * **/
	@PutMapping("actualiza/{id}")
	@ResponseStatus (HttpStatus.CREATED)
	public Cine actualizaCine(@RequestBody Cine cine, @PathVariable("id") int id) {
		Cine actuCine = cineRepository.findById(id).orElse(null);
		
		actuCine.setId(cine.getId());
		actuCine.setLatitud(cine.getLatitud());
		actuCine.setLongitud(cine.getLongitud());

		
		return cineRepository.save(actuCine);
	}
	
	/********
	 * Actualiza el fichera de cines con la fecha del dia y la lista de cines
	 * **/
	@GetMapping({"fichero"}) 
	public void actualizaFichero() {
		LocalDate fechaActual = LocalDate.now(); // Guarda la fecha actual
		
		// Formatear a string con el formato YYYY-MM-DD
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);
		System.out.println("La fecha actual es: " + fechaFormateada);
		
		List<Cine> listaCines = cineRepository.findAll();
		
		// Ruta del fichero a escribir
        String rutaFichero = "src/main/resources/cines.json";
        
        try (FileWriter escritor = new FileWriter(rutaFichero, false)) { // `false` sobreescribe el archivo
            // Crear contenido del fichero
            StringBuilder contenido = new StringBuilder();
            contenido.append("{\n");
            contenido.append("  \"fecha\": \"").append(fechaFormateada).append("\",\n");
            contenido.append("  \"cines\": [\n");

            for (int i = 0; i < listaCines.size(); i++) {
                Cine cine = listaCines.get(i);
                contenido.append("    {\n");
                contenido.append("      \"id\": ").append(cine.getId()).append(",\n");
                contenido.append("      \"nombre\": \"").append(cine.getNombre()).append("\",\n");
                contenido.append("      \"url\": \"").append(cine.getUrl()).append("\",\n");
                contenido.append("      \"latitud\": \"").append(cine.getLatitud()).append("\",\n");
                contenido.append("      \"longitud\": \"").append(cine.getLongitud()).append("\"\n");
                contenido.append("    }");
                if (i < listaCines.size() - 1) {
                    contenido.append(","); // Coma solo si no es el último elemento
                }
                contenido.append("\n");
            }

            contenido.append("  ]\n");
            contenido.append("}\n");

            // Escribir contenido al fichero
            escritor.write(contenido.toString());
            System.out.println("Fichero actualizado correctamente: " + rutaFichero);

        } catch (IOException e) {
            System.err.println("Error al escribir el fichero: " + e.getMessage());
        }

	}
	
	/***
	 * Metodo que carga los cines en la base de datos
	 * Usa un metodo de CarteleraService
	 * **/
	@GetMapping({"cargaCines"}) 
	public void cargaCines() {
		carteleraService.cargarCines("cinesAlava.json");
		carteleraService.cargarCines("cinesBizkaia.json");
		carteleraService.cargarCines("cinesGipuzkoa.json");
		System.out.println("Cines cargados con exito");
	}
	
}
