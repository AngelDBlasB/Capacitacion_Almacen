package com.angel.almacen.service.ventas;

import com.angel.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.angel.almacen.dto.ventas.VentaRequest;
import com.angel.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentaService {

    List<VentaResponse> listar();

    List<VentaResponse> listarActivas();

    List<VentaResponse> listarCanceladas();

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar(VentaRequest request);

    VentaResponse cancelar(Long id);

    List<ReporteVentasSucursalResponse> reporteVentasSucursal();


}
