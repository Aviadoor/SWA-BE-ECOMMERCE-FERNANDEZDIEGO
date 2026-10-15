package com.inventario.manager.service;

import com.inventario.manager.model.Producto;
import com.inventario.manager.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    public Producto guardar(Producto producto) {
        return repository.save(producto);
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Producto> actualizar(Long id, Producto detalles) {
        return repository.findById(id).map(productoExistente -> {
            productoExistente.setNombre(detalles.getNombre());
            productoExistente.setPrecio(detalles.getPrecio());
            productoExistente.setStock(detalles.getStock());
            productoExistente.setCategoria(detalles.getCategoria());
            return repository.save(productoExistente);
        });
    }

    public boolean eliminar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
