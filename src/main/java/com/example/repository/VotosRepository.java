package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.domain.Votos;

@Repository
public interface VotosRepository extends JpaRepository<Votos, Integer>{

}
