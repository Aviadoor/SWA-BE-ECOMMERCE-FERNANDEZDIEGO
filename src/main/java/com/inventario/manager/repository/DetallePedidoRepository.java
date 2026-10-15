package com.inventario.manager.repository;

import com.inventario.manager.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    // 1. Obtener todos los ítems/detalles de un pedido específico
    List<DetallePedido> findByPedidoId(Long pedidoId);

    // 2. Obtener el historial de ventas de un producto específico
    List<DetallePedido> findByProductoId(Long productoId);

    // 3. Consulta JPQL: Calcular el total de unidades vendidas de un producto
    @Query("SELECT COALESCE(SUM(d.cantidad), 0) FROM DetallePedido d WHERE d.producto.id = :productoId")
    Integer obtenerTotalUnidadesVendidasPorProducto(@Param("productoId") Long productoId);
}