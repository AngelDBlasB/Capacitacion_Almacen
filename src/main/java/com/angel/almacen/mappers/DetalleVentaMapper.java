package com.angel.almacen.mappers;

import com.angel.almacen.dto.ventas.DetalleVentaRequest;
import com.angel.almacen.dto.ventas.DetalleVentaResponse;
import com.angel.almacen.entities.DetalleVenta;
import com.angel.almacen.entities.Producto;
import com.angel.almacen.entities.Venta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DetalleVentaMapper {

    public DetalleVenta requestAEntidad(DetalleVentaRequest request, Producto producto,Venta venta){

        if (request == null) return null;

        return DetalleVenta.builder()
                .producto(producto)
                .venta(venta)
                .cantidadProducto(request.cantidad())
                .precioProducto(producto.getPrecio())
                .build();

    }

    public DetalleVentaResponse entidadAResponse(DetalleVenta detalleVenta){

        if (detalleVenta == null) return null;

        return new DetalleVentaResponse(
                detalleVenta.getProducto().getId(),
                detalleVenta.getProducto().getNombre(),
                detalleVenta.getCantidadProducto(),
                detalleVenta.getPrecioProducto(),
                detalleVenta.getSubtotal()
        );

    }

}
