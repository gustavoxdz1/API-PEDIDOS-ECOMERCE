package com.ecommerce.pedidos.core.usecases;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// Utilizamos 'record' (introduzido no Java 14) para criar classes de dados imutáveis de forma concisa.
public record CriarPedidoCommand(
        UUID usuarioId,
        List<ItemCommand> itens
) {
    public record ItemCommand(UUID produtoId, Integer quantidade, BigDecimal precoUnitario) {}
}

