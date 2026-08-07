package com.angel.almacen.repositories;

import com.angel.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.angel.almacen.entities.Venta;
import com.angel.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta,Long> {

    List<Venta> findByEstado(EstadoVenta estadoVenta);

    Optional<Venta> findByIdAndEstado(Long id, EstadoVenta estadoVenta);

    @Query("""
    SELECT new com.angel.almacen.dto.ventas.ReporteVentasSucursalResponse(
        s.id,
        s.nombre,
        SUM(d.precioProducto * d.cantidadProducto),
        SUM(d.cantidadProducto)
    )
    FROM DetalleVenta d
    JOIN d.venta v
    JOIN v.sucursal s
    WHERE v.estado = com.angel.almacen.enums.EstadoVenta.REGISTRADA
    GROUP BY s.id, s.nombre
    """)
    List<ReporteVentasSucursalResponse> reporteVentasSucursal();

}
