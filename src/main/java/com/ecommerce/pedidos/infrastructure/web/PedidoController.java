package com.ecommerce.pedidos.infrastructure.web;

import com.ecommerce.pedidos.core.domain.entities.Pedido;
import com.ecommerce.pedidos.core.usecases.CriarPedido;
import com.ecommerce.pedidos.core.usecases.BuscarPedidoPorId;
import com.ecommerce.pedidos.core.usecases.CriarPedidoCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

// @RestController diz ao Spring "Esta classe é a porta de entrada para a web (API Rest)"
@RestController
// @RequestMapping define o endereço base.
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    // 1. Injeção da Regra(O Caso de Uso)
    private final BuscarPedidoPorId buscarPedidoPorId;

    // Construtor: É aqui que o Spring Boot "injeta" as classes automaticamente para nós.
    public PedidoController(BuscarPedidoPorId buscarPedidoPorId, CriarPedido criarPedidoUseCase) {
        this.buscarPedidoPorId = buscarPedidoPorId;
        this.criarPedidoUseCase = criarPedidoUseCase;
    }


    // @GetMapping indica que esta rota responde ao método HTTP GET (Leitura).
    @GetMapping("/{id}")
    // @PathVariable extrai o 'id' da URL (ex: /api/v1/pedidos/123e4567...) e converte para UUID.
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        try {
            final var pedido = buscarPedidoPorId.executar(id);

            // Se pedido encontrado,Spring converte automaticamente objeto para JSON e volta com 200ok
            return ResponseEntity.ok(pedido);

        } catch (IllegalArgumentException e) {

            // para um status HTTP 404 (Not Found) amigável, evitando erros feios no servidor.
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    private final CriarPedido criarPedidoUseCase;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody CriarPedidoCommand command) {
        Pedido pedidoCriado = criarPedidoUseCase.executar(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedidoCriado.getId())
                .toUri();

        PedidoResponseDTO responseDTO = new PedidoResponseDTO(
                pedidoCriado.getId().toString(),
                pedidoCriado.getValorTotal().toString(),
                pedidoCriado.getStatus().name()
        );

        return ResponseEntity.created(location).body(responseDTO);
    }

    public record PedidoResponseDTO(String id, String valorTotal, String status) {
    }
}