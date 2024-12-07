package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.domain.PeliculaLista;
import com.example.domain.PeliculaListaId;

@Repository
public interface PeliculaListaRepository extends JpaRepository<PeliculaLista, PeliculaListaId>{

}
