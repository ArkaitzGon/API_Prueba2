package com.example.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Table(name="LISTA")
public class Lista {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de ID
    private int id;
	//@Column(name="USUARIOID")
	//private int usuarioId;
	@Column(name="NOMBRE")
	private String nombre;
	
	// El JSONBAck... es la diferencia de SpringWeb
	//@JsonBackReference // Decimos que la parte MANY es BACKREFERNECE (padre)
	@ManyToOne // un ciclista tiene un equipo, un equipo varios ciclistas. ESTA ES LA PARTE MANY
	@JoinColumn (name = "usuarioid") // Se une mediante la columna team_id de la parte ONE
	private Usuario usuario;
	
	//Añade propiedad teamName a JSON 
	/*@JsonProperty("usuarioID")
	public int getUsuarioID() {
	    return usuario != null ? usuario.getId() : null;
		}*/
	
	// Relación Many-to-Many con la entidad Pelicula
    //@JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "PELICULALISTA", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "listaid"), // Columna que hace referencia a Lista
            inverseJoinColumns = @JoinColumn(name = "peliculaid") // Columna que hace referencia a Pelicula
    )
    private List<Pelicula> peliculas = new ArrayList<>();
	
}
