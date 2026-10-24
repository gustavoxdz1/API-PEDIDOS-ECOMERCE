package com.ecommerce.pedidos.core.usecases;

import com.ecommerce.pedidos.core.domain.entities.ItemPedido;
import com.ecommerce.pedidos.core.domain.entities.Pedido;
import com.ecommerce.pedidos.core.domain.repositories.PedidoRepository;
import com.ecommerce.pedidos.core.domain.services.MensageriaService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CriarPedido {
    private final PedidoRepository pedidoRepository;
    private final MensageriaService mensageriaService;

    // Injeção de Dependências: O Caso de Uso não sabe qual é a base de dados ou o mensageiro,
    // apenas confia nos contratos (interfaces) passados no construtor.
    public CriarPedido(PedidoRepository pedidoRepository, MensageriaService mensageriaService) {
        this.pedidoRepository = pedidoRepository;
        this.mensageriaService = mensageriaService;
    }

    public Pedido executar(CriarPedidoCommand command) {
        // 1. Mapear o DTO para a Entidade de Domínio (tudo já é UUID)
        List<ItemPedido> itens = command.itens().stream()
                .map(item -> new ItemPedido(
                        null, item.produtoId(),
                        item.quantidade(),
                        item.precoUnitario(),
                        java.time.LocalDateTime.now()
                ))
                .toList();

        // 2. Instanciar o Pedido
        Pedido novoPedido = new Pedido(command.usuarioId(), itens);

        // 3. Persistir o Pedido na base de dados
        Pedido pedidoSalvo = pedidoRepository.salvar(novoPedido);

        // 4. Publicar o evento para o RabbitMQ
        mensageriaService.publicarEventoPedidoCriado(pedidoSalvo);

        return pedidoSalvo;
    }
}
