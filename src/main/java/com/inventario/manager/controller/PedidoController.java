package com.inventario.manager.controller;

import com.inventario.manager.model.DetallePedido;
import com.inventario.manager.model.Pedido;
import com.inventario.manager.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // 1. Listar todos los pedidos con sus detalles
    @GetMapping
    public List<Pedido> listar() {
        return pedidoService.listarTodos();
    }

    // 2. Obtener un pedido por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Consultar pedidos por nombre del cliente
    @GetMapping("/cliente/{nombre}")
    public List<Pedido> buscarPorCliente(@PathVariable String nombre) {
        return pedidoService.buscarPorCliente(nombre);
    }

    // 4. Consultar únicamente los detalles/productos comprados de un pedido
    @GetMapping("/{id}/detalles")
    public List<DetallePedido> obtenerDetalles(@PathVariable Long id) {
        return pedidoService.obtenerDetallesPorPedido(id);
    }

    // 5. Registrar un nuevo pedido con sus productos comprados
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Pedido pedido) {
        try {
            Pedido nuevoPedido = pedidoService.crearPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6. Eliminar un pedido (borra sus detalles en cascada)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (pedidoService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
