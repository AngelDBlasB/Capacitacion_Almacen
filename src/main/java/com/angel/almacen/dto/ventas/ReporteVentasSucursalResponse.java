package com.angel.almacen.dto.ventas;

import java.math.BigDecimal;

public record ReporteVentasSucursalResponse(

        Long idSucursal,
        String nombreSucursal,
        BigDecimal totalFactura,
        Long totalProducVendidos

) {
}
