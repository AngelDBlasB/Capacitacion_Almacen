package com.angel.almacen.controllers;

import com.angel.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.angel.almacen.dto.ventas.VentaRequest;
import com.angel.almacen.dto.ventas.VentaResponse;
import com.angel.almacen.service.ventas.VentaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@AllArgsConstructor
@Validated
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar() {
        return ResponseEntity.ok(ventaService.listar());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<VentaResponse>> listarActivas() {
        return ResponseEntity.ok(ventaService.listarActivas());
    }

    @GetMapping("/canceladas")
    public ResponseEntity<List<VentaResponse>> listarCanceladas() {
        return ResponseEntity.ok(ventaService.listarCanceladas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerPorIdActiva(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(ventaService.obtenerPorIdActiva(id));
    }

    @PostMapping
    public ResponseEntity<VentaResponse> registrar(
            @Valid @RequestBody VentaRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.registrar(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VentaResponse> cancelar(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(ventaService.cancelar(id));
    }

    @GetMapping("/reporte")
    public ResponseEntity<List<ReporteVentasSucursalResponse>> reporteVentasSucursal() {
        return ResponseEntity.ok(ventaService.reporteVentasSucursal());
    }
}
