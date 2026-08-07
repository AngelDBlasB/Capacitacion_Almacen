package com.angel.almacen.dto.ventas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleVentaRequest(

        @NotNull(message = "El ID del producto es requerido")
        @Positive(message = "El ID del producto debe ser positivo")
        Long idProducto,

        @NotNull(message = "La cantidad del producto del producto es requerido")
        @Positive(message = "La cantida del producto debe ser positivo")
        Integer cantidad

) {
}
