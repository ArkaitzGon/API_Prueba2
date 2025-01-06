package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.domain.Pelicula;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Integer>{

	
		//Año y genero
		// Búsqueda de películas por género (como subcadena) y año de estreno
	   /* @Query("SELECT p FROM Pelicula p WHERE " +
	           "LOWER(p.genero) LIKE LOWER(CONCAT('%', :genero, '%')) " +
	           "AND p.fechaEstreno = :fechaEstreno")
		List<Pelicula> findByGeneroAndFechaEstreno(String genero, int fechaEstreno);*/
	
		/*@Query("SELECT p FROM Pelicula p WHERE " +
			       "LOWER(CONCAT(',', p.genero, ',')) LIKE LOWER(CONCAT('%,', :genero, ',%')) " +
			       "AND p.fechaEstreno = :fechaEstreno")
		List<Pelicula> findByGeneroAndFechaEstreno(String genero, int fechaEstreno);*/

		
		// Método para encontrar una película por su título
	    /*List<Pelicula> findByTitulo(String titulo);
	    

	    @Query("SELECT p FROM Pelicula p WHERE " +
	    	       "LOWER(p.reparto) LIKE LOWER(CONCAT('%', :reparto, '%')) " +
	    	       "AND p.fechaEstreno = :fechaEstreno")
	    List<Pelicula> findByFechaEstrenoAndReparto(String reparto, int fechaEstreno);
	    
	    @Query("SELECT p FROM Pelicula p WHERE " +
	    	       "LOWER(p.genero) LIKE LOWER(CONCAT('%', :genero, '%')) " +
	    	       "AND LOWER(p.reparto) LIKE LOWER(CONCAT('%', :reparto, '%')) " +
	    	       "AND p.fechaEstreno = :fechaEstreno")
	    List<Pelicula> findByFechaEstrenoGeneroAndReparto(String genero, String reparto, int fechaEstreno);

	    
	    @Query("SELECT p FROM Pelicula p WHERE " +
	    	       "(:genero IS NULL OR LOWER(p.genero) LIKE LOWER(CONCAT('%', :genero, '%'))) " +
	    	       "AND (:reparto IS NULL OR LOWER(p.reparto) LIKE LOWER(CONCAT('%', :reparto, '%'))) " +
	    	       "AND (:fechaEstreno IS NULL OR p.fechaEstreno = :fechaEstreno)")
	    List<Pelicula> findByGeneroRepartoAndFechaEstreno(
	            @Param("genero") String genero,
	            @Param("reparto") String reparto,
	            @Param("fechaEstreno") Integer fechaEstreno);

	    */
	    //Con este metodo podemos hacer consultas con uno o hasta 4 parametros
	   /* @Query("SELECT p FROM Pelicula p WHERE (:genero IS NULL OR LOWER(p.genero) LIKE LOWER(CONCAT('%', :genero, '%'))) " +
	    	       "AND (:reparto IS NULL OR LOWER(p.reparto) LIKE LOWER(CONCAT('%', :reparto, '%'))) " +
	    	       "AND (:fechaEstreno IS NULL OR p.fechaEstreno = :fechaEstreno) " +
	    	       "AND (:titulo IS NULL OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))")
	    	List<Pelicula> findByGeneroRepartoFechaEstrenoYTitulo(@Param("genero") String genero, 
	    	                                                      @Param("reparto") String reparto, 
	    	                                                      @Param("fechaEstreno") Integer fechaEstreno, 
	    	                                                      @Param("titulo") String titulo);
*/
	@Query("SELECT p FROM Pelicula p WHERE " +
		       "(:genero IS NULL OR LOWER(p.genero) LIKE LOWER(CONCAT('%', :genero, '%'))) " +
		       "AND (:reparto IS NULL OR LOWER(p.reparto) LIKE LOWER(CONCAT('%', :reparto, '%'))) " +
		       "AND (:fechaEstreno IS NULL OR p.fechaEstreno = :fechaEstreno) " +
		       "AND (:titulo IS NULL OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) ")
		List<Pelicula> findByGeneroRepartoFechaEstrenoYTitulo(
		    @Param("genero") String genero,
		    @Param("reparto") String reparto,
		    @Param("fechaEstreno") Integer fechaEstreno,
		    @Param("titulo") String titulo);

	Optional<Pelicula> findByTituloAndFechaEstreno(String titulo, int fechaEstreno);


}