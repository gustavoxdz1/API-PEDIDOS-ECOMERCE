package com.ecommerce.pedidos.infrastructure.config;

import com.ecommerce.pedidos.core.domain.repositories.PedidoRepository;
import com.ecommerce.pedidos.core.usecases.BuscarPedidoPorId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    // O @Bean ensina o Spring a criar o objeto.
    // Como o Spring já sabe criar o PedidoRepositoryImpl (pois colocámos @Component lá),
    // ele passa o repositório automaticamente aqui como parâmetro.
    @Bean
    public BuscarPedidoPorId buscarPedidoPorIdUseCase(PedidoRepository repository) {
        return new BuscarPedidoPorId(repository);
    }
}