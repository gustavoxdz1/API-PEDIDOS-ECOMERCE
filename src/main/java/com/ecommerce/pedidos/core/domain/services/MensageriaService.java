package com.ecommerce.pedidos.core.domain.services;

import com.ecommerce.pedidos.core.domain.entities.Pedido;

public interface MensageriaService {
    void publicarPedidoCriado(Pedido pedido);
    void publicarEventoPedidoCriado(Pedido pedidoSalvo);
}
