package com.ecommerce.pedidos.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataPedidoRepository extends JpaRepository<PedidoEntity, UUID> {

}
