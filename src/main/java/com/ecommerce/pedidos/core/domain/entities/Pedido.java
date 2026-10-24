package com.ecommerce.pedidos.core.domain.entities;

import com.ecommerce.pedidos.core.domain.enums.StatusPedido;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Pedido {
    private final UUID id;
    private final UUID usuarioId;
    private final LocalDateTime dataCriacao;
    private final List<ItemPedido> itens;
    private  BigDecimal valorTotal;
    private  StatusPedido status;

    // Construtor 1: Criação (Usado pelo Caso de Uso para NOVO pedido)
    public Pedido(UUID usuarioId, List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve ter pelo menos um item");
        }
        this.id = UUID.randomUUID(); // Regra: Gera ID na criação
        this.usuarioId = usuarioId;
        this.itens = new ArrayList<>(itens);
        this.status = StatusPedido.AGUARDANDO_PAGAMENTO; // Regra: Nasce sempre aguardando
        this.dataCriacao = LocalDateTime.now();
        this.calcularValorTotal(); // Regra: Calcula o total na criação
    }

    // Construtor para reconstruir um pedido já existente vindo do banco de dados
    public Pedido(UUID id, UUID usuarioId, List<ItemPedido> itens, BigDecimal valorTotal, StatusPedido status, LocalDateTime dataCriacao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.itens = itens;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    // Regra de Negócio: Somar o subtotal de todos os itens
    private void calcularValorTotal() {
        this.valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void aprovar() {
        if (this.status != StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new IllegalStateException("Apenas pedidos aguardando pagamento podem ser aprovados");
        }
        this.status = StatusPedido.APROVADO;
    }

    public void recusar() {
        if (this.status != StatusPedido.AGUARDANDO_PAGAMENTO) {
            throw new IllegalStateException("Apenas pedidos aguardando pagamento podem ser recusados");
        }
        this.status = StatusPedido.RECUSADO;
    }
}