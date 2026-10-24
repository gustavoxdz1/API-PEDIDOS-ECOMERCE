package com.ecommerce.pedidos.core.usecases;

import com.ecommerce.pedidos.core.domain.entities.Pedido;
import com.ecommerce.pedidos.core.domain.repositories.PedidoRepository;

import java.util.UUID;

public class AtualizarStatusPedido {
    private final PedidoRepository pedidoRepository;

    public AtualizarStatusPedido(PedidoRepository pedidoRepository){
        this.pedidoRepository = pedidoRepository;
    }

    public void executar(UUID pedidoId, boolean pagamentoAprovado) {
        // 1. Buscar o pedido existente na base de dados. Lança exceção se não for encontrado.
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + pedidoId));

        // 2. Executar a regra de negócio da máquina de estados na entidade
        if (pagamentoAprovado) {
            pedido.aprovar();
        } else {
            pedido.recusar();
        }

        // 3. Salvar o estado atualizado na base de dados
        pedidoRepository.salvar(pedido);
    }
}
