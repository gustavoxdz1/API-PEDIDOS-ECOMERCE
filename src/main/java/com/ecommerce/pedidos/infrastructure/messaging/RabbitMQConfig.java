package com.ecommerce.pedidos.infrastructure.messaging;

import org.springframework.amqp.core.Queue; // <-- O IMPORT CORRETO AQUI
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue filaPedidos() {
        // Cria uma fila durável (true) que não se perde se o RabbitMQ reiniciar
        return new Queue("pedidos.criados", true);
    }
}