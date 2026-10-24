package com.ecommerce.pedidos.infrastructure.web;

import com.ecommerce.pedidos.core.usecases.CriarPedido;
import com.ecommerce.pedidos.core.usecases.CriarPedidoCommand;
import com.ecommerce.pedidos.core.domain.entities.Pedido;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final CriarPedido criarPedidoUseCase;

    public PedidoController(CriarPedido criarPedidoUseCase) {
        this.criarPedidoUseCase = criarPedidoUseCase;
    }

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

    public record PedidoResponseDTO(String id, String valorTotal, String status) {}
}