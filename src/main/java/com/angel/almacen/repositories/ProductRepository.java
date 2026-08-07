package com.angel.almacen.repositories;

import com.angel.almacen.entities.Producto;
import com.angel.almacen.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Producto,Long> {

    @Query("""

            SELECT p FROM Producto p WHERE 
            (:nombre IS NULL OR :nombre = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND
            (:categoria IS NULL OR p.categoria = :categoria) AND
            (:precioMin IS NULL OR p.precio >= :precioMin) AND
            (:precioMax IS NULL OR p.precio <= :precioMax)
           """)


    List<Producto> buscarConFiltros(
                    @Param("nombre") String nombre,
                    @Param("categoria") Categoria categoria,
                    @Param("precioMin") BigDecimal precioMin,
                    @Param("precioMax") BigDecimal precioMax
            );

}
