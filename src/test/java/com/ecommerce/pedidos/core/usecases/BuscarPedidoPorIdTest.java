package com.ecommerce.pedidos.core.usecases;

import com.ecommerce.pedidos.core.domain.entities.Pedido;
import com.ecommerce.pedidos.core.domain.enums.StatusPedido;
import com.ecommerce.pedidos.core.domain.repositories.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarPedidoPorIdTest {

    @Mock
    private PedidoRepository repository;

    @InjectMocks
    private BuscarPedidoPorId useCase;

    @Test
    void deveEncontrarEVoltarOPedidoQuandoOIdExistir() {
        // 1. Arrange
        final var idValido = UUID.randomUUID();

        final var pedidoSimulado = new Pedido(
                idValido,
                UUID.randomUUID(),
                List.of(),
                BigDecimal.TEN,
                StatusPedido.AGUARDANDO_PAGAMENTO,
                LocalDateTime.now()
        );

        when(repository.buscarPorId(idValido)).thenReturn(Optional.of(pedidoSimulado));

        // 2. Act
        final var resultado = useCase.executar(idValido);

        // 3. Assert
        assertNotNull(resultado, "O pedido não deve ser nulo");
        assertEquals(idValido, resultado.getId(), "O ID do pedido retornado deve ser o mesmo buscado");
        verify(repository, times(1)).buscarPorId(idValido);
    }

    @Test
    void deveLancarExcecaoQuandoOPedidoNaoExistir() {
        // 1. Arrange
        final var idInvalido = UUID.randomUUID();
        when(repository.buscarPorId(idInvalido)).thenReturn(Optional.empty());

        // 2 & 3. Act & Assert
        final var excecao = assertThrows(IllegalArgumentException.class, () -> {
            useCase.executar(idInvalido);
        });

        assertEquals("Pedido não encontrado para o ID: " + idInvalido, excecao.getMessage());
        verify(repository, times(1)).buscarPorId(idInvalido);
    }
}