package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario,Integer>{

	// Método para encontrar una película por su título
    Optional<Usuario> findByEmail(String titulo);
}
