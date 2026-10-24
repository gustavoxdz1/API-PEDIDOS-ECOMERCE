package com.ecommerce.pedidos.infrastructure.messaging;

import com.ecommerce.pedidos.core.domain.entities.Pedido;
import com.ecommerce.pedidos.core.domain.services.MensageriaService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPedidoPublisher implements MensageriaService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQPedidoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicarPedidoCriado(Pedido pedido) {
        // Pega o ID do Pedido do Domínio e envia para a fila do RabbitMQ
        String mensagem = "Pedido gerado com sucesso: " + pedido.getId();
        rabbitTemplate.convertAndSend("pedidos.criados", mensagem);
    }

    @Override
    public void publicarEventoPedidoCriado(Pedido pedidoSalvo) {
        // Como a interface tem dois métodos parecidos,
        // vamos reaproveitar a lógica para não dar erro de compilação.
        this.publicarPedidoCriado(pedidoSalvo);
    }
}