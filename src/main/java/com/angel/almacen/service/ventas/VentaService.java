package com.angel.almacen.service.ventas;

import com.angel.almacen.dto.ventas.VentaRequest;
import com.angel.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentaService {

    List<VentaResponse> listarActvias();

    List<VentaResponse> listarCanceladas();

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar(VentaRequest request);

    VentaResponse cancelar(Long id);


}
