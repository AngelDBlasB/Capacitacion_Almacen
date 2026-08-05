package com.angel.almacen.service.sucurssales;

import com.angel.almacen.dto.sucursales.SucursalRequest;
import com.angel.almacen.dto.sucursales.SucursalResponse;
import com.angel.almacen.entities.Producto;
import com.angel.almacen.entities.Sucursal;
import com.angel.almacen.enums.Categoria;
import com.angel.almacen.exceptions.RecursoNoEncontradoException;
import com.angel.almacen.mappers.SucursalMapper;
import com.angel.almacen.repositories.SucursalRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional // Si ponen trans envolvera todo los metodos publicos con override porque pertenece a una interfaz, y hace un rollback automatico
@Slf4j
public class SucursalServiceImpl implements SucursalService{

    private final SucursalRepository sucursalRepository;

    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {
        log.info("Listando todas las sucursales");

        return sucursalRepository.findAll().stream()
                .map(sucursalMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursalOException(id));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {

        log.info("Registrando nueva sucursal...");

        validarDatosUnicos(request);

        Sucursal sucursal = sucursalMapper.requestAEntidad(request);

        sucursalRepository.save(sucursal);

        log.info("Nueva sucursal {} registrado", sucursal.getNombre());
        return sucursalMapper.entidadAResponse(sucursal);

    }

    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {
        Sucursal sucursal = obtenerSucursalOException(id);

        validarCambiosUnicos(request,id);

        log.info("Actualizando sucursal con id: {}",id);

        sucursal.actualizar(
                request.nombre(),
                request.direccion()
        );

        //productRepository.save(sucursal);

        log.info("Sucursal con id {} actualizada",id);

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {

        Sucursal sucursal = obtenerSucursalOException(id);

        log.info("Eliminando sucursal con id {}",id);

        sucursalRepository.delete(sucursal);

        log.info("Sucursal con id {} eliminado",id);

    }

    private Sucursal obtenerSucursalOException(Long id){

        log.info("Buscando sucursal con id: {}",id);

        return sucursalRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Sucursal no encontrado con id: " + id));
    }

    private void validarDatosUnicos(SucursalRequest request){

        log.info("Validando nombre único...");

        if (sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre de: " + request.nombre());
    }

    private void validarCambiosUnicos(SucursalRequest request, Long id){

        log.info("Validando cambio en nombre único...");

        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(),id))
            throw new IllegalArgumentException("Ya existe una sucursal con el nombre de: " + request.nombre());

    }
}
