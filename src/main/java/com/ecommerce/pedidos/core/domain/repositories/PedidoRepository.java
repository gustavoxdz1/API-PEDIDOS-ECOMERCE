package com.ecommerce.pedidos.core.domain.repositories;

import com.ecommerce.pedidos.core.domain.entities.Pedido;

import java.util.Optional;
import java.util.UUID;

public interface PedidoRepository {
    Pedido salvar(Pedido pedido);
    Optional<Pedido> buscarPorId(UUID id);
}
