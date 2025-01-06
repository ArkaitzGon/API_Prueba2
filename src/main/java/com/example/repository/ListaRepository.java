package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.domain.Lista;
import com.example.domain.Usuario;

@Repository
public interface ListaRepository extends JpaRepository<Lista, Integer>{

	// Método para encontrar una película por su título
    Optional<List <Lista>> findByUsuario(Usuario usuario);
}
