package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.domain.Lista;
import com.example.domain.Pelicula;
import com.example.domain.Usuario;
import com.example.domain.Votos;

@Repository
public interface VotosRepository extends JpaRepository<Votos, Integer>{

	// Método para encontrar una película por su título
    Optional<List <Votos>> findByPeliculaId(int peliculaId);
}
