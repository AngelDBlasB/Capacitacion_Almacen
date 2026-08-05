package com.angel.almacen.service.productos;

import com.angel.almacen.dto.productos.ProductoRequest;
import com.angel.almacen.dto.productos.ProductoResponse;
import com.angel.almacen.entities.Producto;
import com.angel.almacen.enums.Categoria;
import com.angel.almacen.exceptions.RecursoNoEncontradoException;
import com.angel.almacen.mappers.ProductoMapper;
import com.angel.almacen.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional  // Si ponen trans envolvera todo los metodos publicos con override porque pertenece a una interfaz, y hace un rollback automatico
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductRepository productRepository;

    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(
            String nombre, String categoria,
            BigDecimal precioMin, BigDecimal precioMax
    ) {

        log.info("Listando todos los productos");

        return productRepository.findAll().stream()
                .map(productoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(obtenerProductoOException(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {

        log.info("Registrando nuevo producto...");

        Categoria categoria = obtenerCategoriaPorDescripcion(request.categoria());

        Producto producto = productoMapper.requestAEntidad(request,categoria);

        productRepository.save(producto);

        log.info("Nuevo producto {} registrado", producto.getNombre());
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {

        Producto producto = obtenerProductoOException(id);

        Categoria categoria = obtenerCategoriaPorDescripcion(request.categoria());

        log.info("Actualizando producto con id: {}",id);

        producto.actualizar(
                request.nombre(),
                categoria,
                request.precio(),
                request.cantidad()
        );

        //productRepository.save(producto);

        log.info("Producto con id {} actualizado",id);

        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {

        Producto producto = obtenerProductoOException(id);

        log.info("Eliminando producto con id {}",id);

        productRepository.delete(producto);

        log.info("Producto con id {} eliminado",id);

    }

    private Producto obtenerProductoOException(Long id){

        log.info("Buscando producto con id: {}",id);

        return productRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    private Categoria obtenerCategoriaPorDescripcion(String descripcion){
        return Categoria.obtenerCategoriaPorDescripcion(descripcion.trim());
    }
}
