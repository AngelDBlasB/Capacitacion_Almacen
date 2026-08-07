package com.angel.almacen.service.ventas;

import com.angel.almacen.dto.ventas.DetalleVentaRequest;
import com.angel.almacen.dto.ventas.ReporteVentasSucursalResponse;
import com.angel.almacen.dto.ventas.VentaRequest;
import com.angel.almacen.dto.ventas.VentaResponse;
import com.angel.almacen.entities.DetalleVenta;
import com.angel.almacen.entities.Producto;
import com.angel.almacen.entities.Sucursal;
import com.angel.almacen.entities.Venta;
import com.angel.almacen.enums.EstadoVenta;
import com.angel.almacen.exceptions.RecursoNoEncontradoException;
import com.angel.almacen.mappers.DetalleVentaMapper;
import com.angel.almacen.mappers.VentaMapper;
import com.angel.almacen.repositories.ProductRepository;
import com.angel.almacen.repositories.SucursalRepository;
import com.angel.almacen.repositories.VentaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional// Si ponen trans envolvera todo los metodos publicos con override porque pertenece a una interfaz, y hace un rollback automatico
@Slf4j
public class VentaServiceImpl implements VentaService{

    private final VentaRepository ventaRepository;
    private final SucursalRepository sucursalRepository;
    private final ProductRepository productRepository;

    private final VentaMapper ventaMapper;
    private final DetalleVentaMapper detalleVentaMapper;

    @Override
    public List<VentaResponse> listar() {
        log.info("Listando ventas");

        return ventaRepository.findAll().stream()
                .map(ventaMapper::entidadAResponse).toList();
    }

    @Override
    public List<VentaResponse> listarActivas() {
        log.info("Listando ventas activas (REGISTRADA)...");

        return ventaRepository.findByEstado(EstadoVenta.REGISTRADA).stream()
                .map(ventaMapper::entidadAResponse).toList();

    }

    @Override
    public List<VentaResponse> listarCanceladas() {

        log.info("Listando ventas canceladas...");

        return ventaRepository.findByEstado(EstadoVenta.CANCELADA).stream()
                .map(ventaMapper::entidadAResponse).toList();
    }

    @Override
    public VentaResponse obtenerPorIdActiva(Long id) {
        log.info("Buscando venta activa con id: {}", id);
        Venta venta = ventaRepository.findByIdAndEstado(id, EstadoVenta.REGISTRADA)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta activa no encontrada con id: " + id));
        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse registrar(VentaRequest request) {

        log.info("Registrando nueva venta...");

        Sucursal sucursal = obtenerSucursalException(request.idSucursal());

        Venta venta = ventaMapper.requestAEntidad(request, sucursal);

        for (DetalleVentaRequest detalleVentaRequest : request.productos()) {

            Producto producto = obtenerPrdoctoException(detalleVentaRequest.idProducto());

            if (producto.getCantidad()<detalleVentaRequest.cantidad())
                throw new IllegalArgumentException("La cantidad del producto con id: " +
                        producto.getId() +  " debe ser menor o igual a" + producto.getCantidad());
            producto.descontarCantidad(detalleVentaRequest.cantidad());

            DetalleVenta detallesVenta = detalleVentaMapper.requestAEntidad(detalleVentaRequest, producto,venta);

            venta.agregarDetalle(detallesVenta);
        }

        ventaRepository.save(venta);

        log.info("Venta registrada con éxito ID: {}", venta.getId());

        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse cancelar(Long id) {

        log.info("Cancelando venta con ID: {}", id);

        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la venta con ID: " + id));

        // 2. Validar explícitamente si ya se encuentra cancelada
        if (venta.getEstado() == EstadoVenta.CANCELADA) {
            throw new IllegalStateException("La venta con ID " + id + " ya se encuentra cancelada.");
        }

        venta.cancelar();

        log.info("Venta con ID {} cancelada exitosamente", venta.getId());

        return ventaMapper.entidadAResponse(venta);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasSucursalResponse> reporteVentasSucursal() {
        return ventaRepository.reporteVentasSucursal();
    }

    private Sucursal obtenerSucursalException(Long idSucursal) {
        return sucursalRepository.findById(idSucursal)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("Sucursal no encontrada con id: " + idSucursal));
    }

    private Producto obtenerPrdoctoException(Long idProducto) {
        return productRepository.findById(idProducto)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("Sucursal no encontrada con id: " + idProducto));
    }
}
