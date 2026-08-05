package com.angel.almacen.enums;

import com.angel.almacen.exceptions.RecursoNoEncontradoException;
import com.angel.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum EstadoVenta {

    REGISTRADA(1L,"Registrada"),
    CANCELADA(0L,"Cancelada");

    private final Long codigo;

    private final String descripcion;

    public static EstadoVenta obtenerEstadoPorDescripcion(String descripcion){

        StringCustomUtils.validarNoVacio(descripcion,"La descripción es requerida");

        String descripcionNormalizada = StringCustomUtils.quitarAcentos(descripcion);

        for(EstadoVenta estadoVenta : values()){
            if (StringCustomUtils.quitarAcentos(estadoVenta.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return estadoVenta;
        }

        throw new RecursoNoEncontradoException("No existe un estado venta con la descripción: " + descripcion);

    }

    public static EstadoVenta obtenerEstadoVentaPorCodigo(Long codigo){

        if (codigo == null || codigo < 0)
            throw new IllegalArgumentException("El código es requerido y debe ser positivo o 0");

        for (EstadoVenta estadoVenta : values()){
            if (Objects.equals(estadoVenta.codigo, codigo))
                return estadoVenta;
        }

        throw new RecursoNoEncontradoException("No existe un estado venta con el código: " + codigo);
    }

}
