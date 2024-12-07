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
import jakarta.persistence.OneToMany;
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
@Table(name="USUARIO")
public class Usuario {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática de ID
    private int id;
	@Column(name="EMAIL")
	private String email;
	//@JsonProperty(access)
	@Column(name="PASSWORD")
	private String password;
	@Column(name="NOMBRE")
	private String nombre;
	@Column(name="TOKEN")
	private String token;
	
	//@JsonManagedReference // Decimos que la parte ONE es la parte REFERENCE (hijo)
	@OneToMany (mappedBy = "usuario",cascade = CascadeType.ALL) // Esta es la parte ONE (un team puede tener varios ciclistas). MappedBy significa que la relacion esta siendo gestionada por el atributo team de la clase cyclist
	List <Lista> listas = new ArrayList<>(); // Al ser la parte ONE guardamos los MANY en una list de objetos padre(MANY)
}
