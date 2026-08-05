package com.angel.almacen.service.sucurssales;

import com.angel.almacen.dto.productos.ProductoRequest;
import com.angel.almacen.dto.productos.ProductoResponse;
import com.angel.almacen.dto.sucursales.SucursalRequest;
import com.angel.almacen.dto.sucursales.SucursalResponse;

import java.util.List;

public interface SucursalService {

    List<SucursalResponse> listar();

    SucursalResponse obtenerPorId(Long id);

    SucursalResponse registrar (SucursalRequest request);

    SucursalResponse actualizar (SucursalRequest request, Long id);

    void eliminar(Long id);

}
