package com.ecommerce.pedidos.core.usecases;

import com.ecommerce.pedidos.core.domain.entities.Pedido;
import com.ecommerce.pedidos.core.domain.enums.StatusPedido;
import com.ecommerce.pedidos.core.domain.repositories.PedidoRepository;

import java.util.UUID;

public class AtualizarStatusPedido {
    private final PedidoRepository pedidoRepository;

    public AtualizarStatusPedido(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido executar(UUID pedidoId, StatusPedido novoStatus) {
        // 1. Buscar o pedido existente na base de dados. Lança exceção se não for encontrado.
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + pedidoId));

        // 2. Executar a regra de negócio: funcao do dominio mudar o status
        // o caso so repassa a ordem o 'Pedido' que lanca o IllegalStateException
        if (novoStatus == StatusPedido.APROVADO) {
            pedido.aprovar();
        } else if (novoStatus == StatusPedido.RECUSADO) {
            pedido.recusar();
        } else {
            throw new IllegalArgumentException("Transiçao para o status" + novoStatus + "não é suportada");
        }

        // 3. Salvar o estado atualizado na base de dados
        return pedidoRepository.salvar(pedido);
    }
}