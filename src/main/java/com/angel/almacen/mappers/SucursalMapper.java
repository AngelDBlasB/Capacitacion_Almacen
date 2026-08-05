package com.angel.almacen.mappers;

import com.angel.almacen.dto.sucursales.SucursalRequest;
import com.angel.almacen.dto.sucursales.SucursalResponse;
import com.angel.almacen.entities.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {

    public Sucursal requestAEntidad(SucursalRequest request){

        if (request == null) return null;

        return Sucursal.builder()
                .nombre(request.nombre().trim())
                .direccion(request.direccion().trim())
                .build();

    }

    public SucursalResponse entidadAResponse(Sucursal sucursal){
        if (sucursal == null) return null;

        return new SucursalResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion()
        );
    }

}
