package com.angel.almacen.mappers;

import com.angel.almacen.dto.ventas.DetalleVentaResponse;
import com.angel.almacen.dto.ventas.VentaRequest;
import com.angel.almacen.dto.ventas.VentaResponse;
import com.angel.almacen.entities.Sucursal;
import com.angel.almacen.entities.Venta;
import com.angel.almacen.enums.EstadoVenta;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class VentaMapper {

    private SucursalMapper sucursalMapper;
    private DetalleVentaMapper detalleVentaMapper;

    public Venta requestAEntidad(VentaRequest request, Sucursal sucursal){

        if (request == null) return null;

        return Venta.builder()
                .sucursal(sucursal)
                .estado(EstadoVenta.REGISTRADA)
                .fechaVenta(LocalDate.now())
                .build();


    }

    public VentaResponse entidadAResponse(Venta venta) {

        if (venta == null) return null;

        List<DetalleVentaResponse> detallesResponse = venta.getDetalleVentas().stream()
                .map(detalleVentaMapper::entidadAResponse)
                .toList();

        BigDecimal total = detallesResponse.stream()
                .map(DetalleVentaResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new VentaResponse(
                venta.getId(),
                venta.getFechaVenta().toString(),
                venta.getEstado().getDescripcion(),
                sucursalMapper.entidadAResponse(venta.getSucursal()),
                detallesResponse,
                total
        );
    }




}
