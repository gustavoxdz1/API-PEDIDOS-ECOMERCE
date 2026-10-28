package com.ecommerce.pedidos.core.usecases;

import com.ecommerce.pedidos.core.domain.entities.Pedido;
import com.ecommerce.pedidos.core.domain.repositories.PedidoRepository;

import java.util.UUID;

public class BuscarPedidoPorId {

    private final PedidoRepository repository;

    // Injeção de dependência pelo construtor
    public BuscarPedidoPorId(PedidoRepository repository) {
        this.repository = repository;
    }

    public Pedido executar(UUID id) {
        // O método orElseThrow faz oque o segundo teste pede:
        // Lança a IllegalArgumentException se nao achar ID no banco
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado para o ID: " + id));
    }
}
