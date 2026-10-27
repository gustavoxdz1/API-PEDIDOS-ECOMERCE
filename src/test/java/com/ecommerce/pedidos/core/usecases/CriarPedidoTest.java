package com.ecommerce.pedidos.core.usecases;

import com.ecommerce.pedidos.core.domain.entities.Pedido;
import com.ecommerce.pedidos.core.domain.repositories.PedidoRepository;
import com.ecommerce.pedidos.core.domain.services.MensageriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarPedidoTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private MensageriaService publisher;

    @InjectMocks
    private CriarPedido useCase; // Esta é a sua classe real de Caso de Uso que será testada

    @Test
    void deveCriarEProcessarUmPedidoComSucesso() {
        // 1. Arrange (Preparação 100% imutável)
        final var usuarioId = UUID.randomUUID();
        final var produtoId = UUID.randomUUID();

        // Simulando a entrada de 2 unidades de um produto de R$ 50,00
        final var itemCommand = new CriarPedidoCommand.ItemCommand(
                produtoId,
                2,
                new BigDecimal("50.00")
        );

        // Montando o comando final que o Controller enviaria para o UseCase
        final var comandoDeEntrada = new CriarPedidoCommand(usuarioId, List.of(itemCommand));

        // Ensinando o Mockito a interceptar o salvamento e devolver o próprio objeto gerado na memória!
        when(repository.salvar(any(Pedido.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0); // Captura e retorna o Pedido real que o UseCase criou
        });

        // 2. Act (Ação: a regra de negócio entra em cena)
        final var resultado = useCase.executar(comandoDeEntrada);

        // 3. Assert (Verificações para garantir que não teremos surpresas ou NullPointerExceptions)
        assertNotNull(resultado, "O resultado da criação do pedido não deveria ser nulo");

        // Verificando as regras rígidas da Arquitetura Limpa:
        // O repositório e o publicador de fila PRECISAM ser chamados exatamente 1 vez cada
        verify(repository, times(1)).salvar(any(Pedido.class));
        verify(publisher, times(1)).publicarEventoPedidoCriado(any(Pedido.class));
    }

    @Test
    void naoDeveCriarPedidoSemItens() {
        // 1. Arrange: Criamos um comando com uma lista de itens VAZIA
        final var usuarioId = UUID.randomUUID();
        final var comandoInvalido = new CriarPedidoCommand(usuarioId, List.of());

        // 2 & 3. Act & Assert: Capturamos a exceção
        final var excecao = assertThrows(IllegalArgumentException.class, () -> {
            useCase.executar(comandoInvalido);
        });

        //Validamos se a mensagem de erro é exatamente a que o seu Domínio definiu
        assertEquals("O pedido deve ter pelo menos um item", excecao.getMessage());

        // A prova de fogo da Arquitetura Limpa: Garantimos que o banco e a fila NUNCA foram chamados
        verify(repository, never()).salvar(any());

        // Ajuste o nome do método abaixo para o nome real da sua interface (o mesmo do teste anterior)
        verify(publisher, never()).publicarEventoPedidoCriado(any());
    }
}