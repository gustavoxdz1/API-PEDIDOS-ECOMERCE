package com.ecommerce.pedidos.core.domain.entities;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ItemPedido {
    private final UUID id;
    private final UUID produtoId;
    private final Integer quantidade;
    private final BigDecimal precoUnitario;
    private final LocalDateTime dateTime;

    public ItemPedido(UUID id, UUID produtoId, Integer quantidade, BigDecimal precoUnitario, final LocalDateTime dateTime) {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (precoUnitario == null || precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.dateTime = LocalDateTime.now(); // Inicialização correta da data
    }

    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}