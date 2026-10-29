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
class AtualizarStatusPedidoTest {

    @Mock
    private PedidoRepository repository;

    @InjectMocks
    private AtualizarStatusPedido useCase;

    @Test
    void deveAprovarPedidoAguardandoPagamentoComSucesso() {
        final var id = UUID.randomUUID();
        final var pedido = new Pedido(id, UUID.randomUUID(), List.of(), BigDecimal.TEN, StatusPedido.AGUARDANDO_PAGAMENTO, LocalDateTime.now());

        when(repository.buscarPorId(id)).thenReturn(Optional.of(pedido));

        useCase.executar(id, StatusPedido.APROVADO);

        assertEquals(StatusPedido.APROVADO, pedido.getStatus(), "O status do pedido deve ser APROVADO");
        verify(repository, times(1)).salvar(pedido);
    }

    @Test
    void deveLancarExcecaoAoTentarAprovarPedidoJaAprovado() {
        final var id = UUID.randomUUID();
        // Arranjo: O pedido já nasce aprovado nesta simulação
        final var pedidoJaAprovado = new Pedido(id, UUID.randomUUID(), List.of(), BigDecimal.TEN, StatusPedido.APROVADO, LocalDateTime.now());

        when(repository.buscarPorId(id)).thenReturn(Optional.of(pedidoJaAprovado));

        final var excecao = assertThrows(IllegalStateException.class, () -> {
            useCase.executar(id, StatusPedido.APROVADO);
        });

        assertEquals("Apenas pedidos aguardando pagamento podem ser aprovados", excecao.getMessage());

        // Garante que o banco não salvou uma atualização inválida
        verify(repository, never()).salvar(any());
    }
}