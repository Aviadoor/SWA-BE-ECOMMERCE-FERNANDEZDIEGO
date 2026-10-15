package com.inventario.manager.repository;

import com.inventario.manager.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // 1. Buscar pedidos por nombre de cliente (búsqueda parcial sin importar mayúsculas)
    List<Pedido> findByClienteContainingIgnoreCase(String cliente);

    // 2. Buscar pedidos realizados dentro de un rango de fechas
    List<Pedido> findByFechaCompraBetween(LocalDateTime inicio, LocalDateTime fin);

    // 3. Consulta JPQL: Obtener pedidos cuyo monto total supere cierto valor
    @Query("SELECT p FROM Pedido p WHERE p.montoTotal >= :montoMinimo ORDER BY p.fechaCompra DESC")
    List<Pedido> buscarPedidosMayoresA(@Param("montoMinimo") BigDecimal montoMinimo);
}