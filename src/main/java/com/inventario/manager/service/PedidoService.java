package com.inventario.manager.service;


import com.inventario.manager.model.DetallePedido;
import com.inventario.manager.model.Pedido;
import com.inventario.manager.model.Producto;
import com.inventario.manager.repository.DetallePedidoRepository;
import com.inventario.manager.repository.PedidoRepository;
import com.inventario.manager.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detalleRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         DetallePedidoRepository detalleRepository,
                         ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.detalleRepository = detalleRepository;
        this.productoRepository = productoRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> buscarPorCliente(String cliente) {
        return pedidoRepository.findByClienteContainingIgnoreCase(cliente);
    }

    public List<DetallePedido> obtenerDetallesPorPedido(Long pedidoId) {
        return detalleRepository.findByPedidoId(pedidoId);
    }

    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        BigDecimal totalAcumulado = BigDecimal.ZERO;

        if (pedido.getFechaCompra() == null) {
            pedido.setFechaCompra(LocalDateTime.now());
        }

        // Procesar cada línea del detalle
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto productoDB = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalle.getProducto().getId()));

            if (productoDB.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + productoDB.getNombre());
            }

            // Descontar stock del inventario
            productoDB.setStock(productoDB.getStock() - detalle.getCantidad());
            productoRepository.save(productoDB);

            // Asignar precio unitario actual y calcular subtotal
            BigDecimal precioUnitario = BigDecimal.valueOf(productoDB.getPrecio());
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detalle.getCantidad()));

            detalle.setProducto(productoDB);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);
            detalle.setPedido(pedido); // Vincular llave foránea pedido_id

            totalAcumulado = totalAcumulado.add(subtotal);
        }

        pedido.setMontoTotal(totalAcumulado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
