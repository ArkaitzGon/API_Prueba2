package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.domain.Cine;

@Repository
public interface CineRepository extends JpaRepository <Cine,Integer>{

}
